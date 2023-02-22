package abc.project.projectcheckinapp.Fragment.Teacher;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import abc.project.projectcheckinapp.databinding.FragmentStudentListBinding;
import abc.project.projectcheckinapp.Other.AdapterAllStudent;
import abc.project.projectcheckinapp.Other.ClickListener;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StudentListFragment extends Fragment {
    FragmentStudentListBinding binding;
    NavController navController;
    SharedPreferences sharedPreferences;
    ExecutorService executor;
    RecyclerView recyclerView;
    SQLiteDatabase db;
    int cid;
    MediaType mediaType;
    ClickListener clickListener;
    AdapterAllStudent adapter;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    public StudentListFragment() {
        // Required empty public constructor
    }

    public static StudentListFragment newInstance(String param1, String param2) {
        StudentListFragment fragment = new StudentListFragment();
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
            sharedPreferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
            db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null); // 將學生資訊存進裝置的DB
            // 每個跟RecyclerView有關的資料都存進裝置名為 allList 的DB中，以table名稱來區別每個list
            db.execSQL("drop table if exists allstu_"+cid+";");
            db.execSQL("create table allstu_"+cid+"(sid integer,name text, depart text, stuId text);");
            try {
                stuInfos =new JSONArray( bundle.getString("list") );
                for (int i = 0; i<stuInfos.length();i++){
                    JSONObject stuInfo = stuInfos.getJSONObject(i);
                    db.execSQL("insert into allstu_"+cid+" values (?,?,?,?);",
                            new Object[] { stuInfo.getInt("sid"),
                                           stuInfo.getString("學生姓名"),
                                           stuInfo.getString("科系"),
                                           stuInfo.getString("學號")}); }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            clickListener = new ClickListener() {
                @Override
                public void onClickForAllStuList(int position,int sid, String stuname, String studepart, String stuid) {
                    builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("學生姓名："+stuname+"\r\n科系："+studepart+"\r\n學號："+stuid);
                    builder.setNegativeButton("關閉", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {        }
                    });
                    dialog = builder.create();
                    dialog.show();
                }
                @Override
                public void onClickForClassroom(int position, int cid,String classname) {  }
                @Override
                public void onClickForNoRcStuList(int position, int sid) {   }
            };
            adapter = new AdapterAllStudent(db,clickListener,cid);
            binding.btnTecLSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.selectAllColumn(binding.txtTecL1Select.getText().toString());
                    adapter.notifyDataSetChanged();
                }
            });
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentListBinding.inflate(inflater,container,false);
        sharedPreferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
        executor = Executors.newSingleThreadExecutor();
        builder = new AlertDialog.Builder(getActivity());
        cid = sharedPreferences.getInt("cid",0);
        recyclerView = binding.RecyclerStuAll;

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
                .url("http://20.2.232.79:8864/api/list/allStu")
                .post(body).build();
        StudentListAPIWorker apiWorker = new StudentListAPIWorker(request);
        executor.execute(apiWorker);

        return binding.getRoot();
    }

    class StudentListAPIWorker implements Runnable{
        OkHttpClient client;
        Request request;
        public StudentListAPIWorker(Request request) {
            this.request = request;
            client = new OkHttpClient();
        }
        @Override
        public void run() {
            try {
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();
                JSONObject result = new JSONObject(jsonString);
                Message m = studentListResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                if ( result.getInt("type")==2){
                    String stuInfo = result.getJSONArray("list").toString();
                    bundle.putString("list",stuInfo);
                } else {
                    Log.e("api","There is a problem with the api response.");
                }
                m.setData(bundle);
                studentListResultHandler.sendMessage(m);
            } catch (Exception e) {
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