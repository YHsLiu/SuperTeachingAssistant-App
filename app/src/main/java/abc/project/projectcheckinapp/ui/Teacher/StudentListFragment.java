package abc.project.projectcheckinapp.ui.Teacher;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import abc.project.projectcheckinapp.databinding.FragmentStudentListBinding;
import abc.project.projectcheckinapp.rawData.AdapterAllStu;
import abc.project.projectcheckinapp.rawData.ClickListener;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentListFragment extends Fragment {
    FragmentStudentListBinding binding;
    NavController navController;
    SharedPreferences preferences;
    ExecutorService executor;
    RecyclerView recyclerView;
    SQLiteDatabase db;
    int cid;
    MediaType mediaType;
    ClickListener clickListener;
    AdapterAllStu adapter;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    public StudentListFragment() {
        // Required empty public constructor
    }


    public static StudentListFragment newInstance(String param1, String param2) {
        StudentListFragment fragment = new StudentListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    Handler studentListResultHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            JSONArray stuInfos;
            preferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
            cid = preferences.getInt("cid",0);
            db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null); // 將學生資訊存進裝置的DB
            // 每個跟RecyclerView有關的資料都存進裝置名為 allList 的DB中，以table名稱來區別每個list
            db.execSQL("drop table if exists "+cid+"_allstu;");
            db.execSQL("create table "+cid+"_allstu(sid integer,name text, depart text, stuId text);");
            try {
                stuInfos =new JSONArray( bundle.getString("list") );
                for (int i = 0; i<stuInfos.length();i++){
                    JSONObject stuInfo = stuInfos.getJSONObject(i);
                    db.execSQL("insert into "+cid+"_allstu values (?,?,?,?);",
                            new Object[] { stuInfo.getInt("sid"),
                                           stuInfo.getString("姓名"),
                                           stuInfo.getString("科系"),
                                           stuInfo.getString("學號")}); }
                db.close();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };

    Handler ManualCheckHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            JSONObject packet2 = new JSONObject();
            try {
                packet2.put("type",1);
                packet2.put("status",11);
                packet2.put("cid",bundle.getInt("cid"));
                packet2.put("sid",bundle.getInt("sid"));
                packet2.put("date",bundle.getString("date"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            mediaType = MediaType.parse("application/json");
            RequestBody body2 = RequestBody.create(packet2.toString(),mediaType);
            if (bundle.getInt("status") == 12){
                builder.setPositiveButton("點名", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 設定click後的動作
                        Request request = new Request.Builder()
                                .url("http://192.168.255.62:8864/api/rollcall/manual/call")
                                .post(body2).build();
                        ManualRollCallAPIWorker manualAPIWorker = new ManualRollCallAPIWorker(request);
                        executor.execute(manualAPIWorker);
                    }
                });
            } else {
                builder.setPositiveButton("取消點名", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 設定click後的動作
                        Request request = new Request.Builder()
                                .url("http://192.168.255.62:8864/api/rollcall/manual/cancel")
                                .post(body2).build();
                        ManualRollCallAPIWorker manualAPIWorker = new ManualRollCallAPIWorker(request);
                        executor.execute(manualAPIWorker);
                    }
                });
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentListBinding.inflate(inflater,container,false);
        preferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
        builder = new AlertDialog.Builder(getActivity());
        cid = preferences.getInt("cid",0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String date = formatter.format(new Date());

        JSONObject packet = new JSONObject();
        try {
            packet.put("type",1);
            packet.put("cid",cid);
        } catch (JSONException e) {
            Log.w("error","packet");
        }
        mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(packet.toString(),mediaType);
        Request request = new Request.Builder()
                .url("http://192.168.255.62:8864/api/list/allStu")
                .post(body).build();
        SimpleAPIWorker simpleAPIWorker = new SimpleAPIWorker(request);
        executor.execute(simpleAPIWorker);

        db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null);
        recyclerView =binding.RecyclerStuAll;
        clickListener = new ClickListener() {
            @Override
            public void onClickForAllStuList(int position,int sid, String stuname, String studepart, String stuid) {
                builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("學生姓名："+stuname+"\r\n科系："+studepart+"\r\n學號："+stuid);
                JSONObject packet1 = new JSONObject();
                try {
                    packet1.put("type",1);
                    packet1.put("status",11);
                    packet1.put("cid",cid);
                    packet1.put("sid",sid);
                    packet1.put("date",date);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                RequestBody body1 = RequestBody.create(packet1.toString(),mediaType);
                Request request1 = new Request.Builder()
                        .url("http://192.168.255.62:8864/api/rollcall/manual/check")
                        .post(body1).build();
                ManualCheckAPIWorker manualAPIWorker = new ManualCheckAPIWorker(request1);
                executor.execute(manualAPIWorker);
                builder.setNegativeButton("關閉", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {        }
                });
                dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onClickForClassroom(int position, int cid) {  }
        };
        adapter = new AdapterAllStu(db,clickListener,cid);
        binding.btnTecLSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.selectAllColumn(binding.txtTecL1Select.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }
    class SimpleAPIWorker implements Runnable{
        OkHttpClient client;
        Request request;
        public SimpleAPIWorker(Request request) {
            this.request = request;
            client = new OkHttpClient();
        }
        @Override
        public void run() {
            try {
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();
                Log.w("api","API回應:"+jsonString);
                JSONObject result = new JSONObject(jsonString);
                Message m = studentListResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                if ( result.getInt("type")==2){
                    String stuInfo = result.getJSONArray("list").toString();
                    bundle.putString("list",stuInfo);
                } else {
                    Log.e("error","api回應有問題");
                }
                m.setData(bundle);
                studentListResultHandler.sendMessage(m);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class ManualCheckAPIWorker implements Runnable{
        OkHttpClient client;
        Request request;
        public ManualCheckAPIWorker(Request request) {
            this.request = request;
            client = new OkHttpClient();
        }
        @Override
        public void run() {
            try {
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();
                Log.w("api","API回應:"+jsonString);
                JSONObject result = new JSONObject(jsonString);
                Message m = ManualCheckHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("status",result.getInt("status"));
                bundle.putInt("cid",result.getInt("cid"));
                bundle.putInt("sid",result.getInt("sid"));
                bundle.putString("date",result.getString("date"));
                m.setData(bundle);
                ManualCheckHandler.sendMessage(m);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class ManualRollCallAPIWorker implements Runnable{
        OkHttpClient client;
        Request request;
        public ManualRollCallAPIWorker(Request request) {
            this.request = request;
            client = new OkHttpClient();
        }
        @Override
        public void run() {
            try {
                client.newCall(request).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}