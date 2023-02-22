package abc.project.projectcheckinapp.Fragment.Teacher;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentSelectRoomBinding;
import abc.project.projectcheckinapp.Other.AdapterClassroom;
import abc.project.projectcheckinapp.Other.ClickListener;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SelectRoomFragment extends Fragment {

    FragmentSelectRoomBinding binding;
    NavController navController;
    int tid;
    SQLiteDatabase db;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    ClickListener clickListener;
    SharedPreferences.Editor sharedEditor;
    ExecutorService executor;
    AdapterClassroom adapter;
    Handler selectClassResultHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            sharedPreferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
            tid = sharedPreferences.getInt("tid",0);
            db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null);
            // 每個跟RecyclerView有關的資料都存進裝置名為 allList 的DB中，以table名稱來區別每個list
            db.execSQL("drop table if exists allroom;");
            db.execSQL("create table allroom(cid integer,classname text, classcode text);");
            Log.e("sqlite","create table allroom");
            try {
                JSONArray roomInfos =new JSONArray( bundle.getString("roomList") );
                for (int i = 0; i<roomInfos.length();i++){
                    JSONObject stuInfo = roomInfos.getJSONObject(i);
                    db.execSQL("insert into allroom values (?,?,?);",
                            new Object[] { stuInfo.getInt("cid"),
                                    stuInfo.getString("課程名稱"),
                                    stuInfo.getString("課程代碼")}); }
                adapter = new AdapterClassroom(db,clickListener);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.btnTecSQuery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.selectRoom(binding.txtTecSName.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };
    public SelectRoomFragment() {
        // Required empty public constructor
    }


    public static SelectRoomFragment newInstance(String param1, String param2) {
        SelectRoomFragment fragment = new SelectRoomFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSelectRoomBinding.inflate(inflater,container,false);
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        executor = Executors.newSingleThreadExecutor();
        tid = sharedPreferences.getInt("tid",0);
        recyclerView =binding.RecyclerClassroom;
        JSONObject packet = new JSONObject();
        try {
            packet.put("type",1);
            packet.put("status",11);
            packet.put("tid",tid);
        } catch (JSONException e) {
            Log.e("error","packet");
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(packet.toString(),mediaType);
        Request request= new Request.Builder()
                .url("http://20.2.232.79:8864/api/list/classroom")
                .post(body)
                .build();
        SelectRoomAPIWorker apiCaller = new SelectRoomAPIWorker(request);
        executor.execute(apiCaller);

        clickListener = new ClickListener() {
            @Override
            public void onClickForAllStuList(int position, int sid, String stuname, String studepart, String stuid) {   }
            @Override
            public void onClickForClassroom(int position, int cid,String classname) {
                sharedEditor = sharedPreferences.edit();
                sharedEditor.putInt("cid",cid);
                sharedEditor.putString("classname",classname);
                sharedEditor.apply();
                navController.navigate(R.id.action_selectRoomFragment_to_nav_tec_enter);
            }
            @Override
            public void onClickForNoRcStuList(int position, int sid) {   }
        };
        return binding.getRoot();
    }

    class SelectRoomAPIWorker implements Runnable {
        OkHttpClient client;
        Request request;

        public SelectRoomAPIWorker(Request request) {
            this.request = request;
            client = new OkHttpClient();
        }

        @Override
        public void run() {
            try {
                Response response = client.newCall(request).execute();
                String res = response.body().string();
                JSONObject result = new JSONObject(res);
                Message m = selectClassResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("status", result.getInt("type"));
                bundle.putString("roomList", result.getString("list"));
                m.setData(bundle);
                selectClassResultHandler.sendMessage(m);
            } catch (Exception e) {
                Log.e("api", "API responds with problems");
            }
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}