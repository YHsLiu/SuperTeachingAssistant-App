package abc.project.projectcheckinapp.ui.Student;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentEnterclassBinding;
import abc.project.projectcheckinapp.rawData.AdapterClassroom;
import abc.project.projectcheckinapp.rawData.ClickListener;
import abc.project.projectcheckinapp.ui.Teacher.SelectRoomFragment;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EnterClassFragment extends Fragment {

    private FragmentEnterclassBinding binding;
    NavController navController;
    int sid;
    SQLiteDatabase db;
    SharedPreferences preferences;
    RecyclerView recyclerView;
    ClickListener clickListener;
    SharedPreferences.Editor contextEditor;
    ExecutorService executor;

    public EnterClassFragment() {
    }

    Handler enterClassResultHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            preferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
            sid = preferences.getInt("sid",0);
            db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null); // 將學生資訊存進裝置的DB
            // 每個跟RecyclerView有關的資料都存進裝置名為 allList 的DB中，以table名稱來區別每個list
            db.execSQL("drop table if exists allroom;");
            db.execSQL("create table allroom(cid integer,classname text, classcode text);");
            try {
                JSONArray roomInfos =new JSONArray( bundle.getString("roomList") );
                for (int i = 0; i<roomInfos.length();i++){
                    JSONObject stuInfo = roomInfos.getJSONObject(i);
                    db.execSQL("insert into allroom values (?,?,?);",
                            new Object[] { stuInfo.getInt("cid"),
                                    stuInfo.getString("課程名稱"),
                                    stuInfo.getString("課程代碼")}); }
                AdapterClassroom adapter = new AdapterClassroom(db,clickListener);
                binding.btnStuSQuery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.selectRoom(binding.txtStuSSelect.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                });
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };
    public static EnterClassFragment newInstance(String param1, String param2) {
        EnterClassFragment fragment = new EnterClassFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEnterclassBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        preferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        executor = Executors.newSingleThreadExecutor();
        sid = preferences.getInt("sid",0);
        recyclerView =binding.RecyclerStuClassroom;
        JSONObject packet = new JSONObject();
        try {
            packet.put("type",1);
            packet.put("status",11);
            packet.put("sid",sid);
        } catch (JSONException e) {
            Log.e("error","packet");
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(packet.toString(),mediaType);
        Request request= new Request.Builder()
                .url("http://192.168.255.62:8864/api/list/student/classroom")
                .post(body)
                .build();
        EnterClassAPIWorker apiCaller = new EnterClassAPIWorker(request);
        executor.execute(apiCaller);

        clickListener = new ClickListener() {
            @Override
            public void onClickForAllStuList(int position, int sid, String stuname, String studepart, String stuid) {   }
            @Override
            public void onClickForClassroom(int position, int cid,String classname) {
                contextEditor = preferences.edit();
                contextEditor.putInt("cid",cid);
                contextEditor.putString("classname",classname);
                contextEditor.apply();
                navController.navigate(R.id.nav_stdRollCall);  // 你看要連哪個action
            }
            @Override
            public void onClickForNoRcStuList(int position, int sid) {   }
        };
        return root;
    }

    class EnterClassAPIWorker implements Runnable {
        OkHttpClient client;
        Request request;

        public EnterClassAPIWorker(Request request) {
            this.request = request;
            client = new OkHttpClient();
        }

        @Override
        public void run() {
            try {
                Response response = client.newCall(request).execute();
                String data = response.body().string();
                Log.w("api","學生進教室 api回傳:"+data);
                JSONObject result = new JSONObject(data);
                Message m = enterClassResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("status", result.getInt("type"));
                bundle.putString("roomList", result.getString("list"));
                m.setData(bundle);
                enterClassResultHandler.sendMessage(m);
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