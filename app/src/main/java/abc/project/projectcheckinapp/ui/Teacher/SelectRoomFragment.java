package abc.project.projectcheckinapp.ui.Teacher;

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

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentSelectRoomBinding;
import abc.project.projectcheckinapp.rawData.AdapterClassroom;
import abc.project.projectcheckinapp.rawData.ClickListener;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectRoomFragment extends Fragment {

    FragmentSelectRoomBinding binding;
    NavController navController;
    int tid;
    SQLiteDatabase db;
    SharedPreferences preferences;
    RecyclerView recyclerView;
    ClickListener clickListener;
    SharedPreferences.Editor contextEditor;
    ExecutorService executor;
    Handler selectClassResultHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            preferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
            tid = preferences.getInt("tid",0);
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
                db.close();
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
        preferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        tid = preferences.getInt("tid",0);
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
                .url("http://192.168.255.62:8864/api/list/classroom")
                .post(body)
                .build();
        SimpleAPIWorker apiCaller = new SimpleAPIWorker(request);
        executor.execute(apiCaller);

        db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null);
        recyclerView =binding.RecyclerClassroom;
        clickListener = new ClickListener() {
            @Override
            public void onClickForAllStuList(int position, int sid, String stuname, String studepart, String stuid) {   }
            @Override
            public void onClickForClassroom(int position, int cid) {
                contextEditor = preferences.edit();
                contextEditor.putInt("cid",cid);
                navController.navigate(R.id.action_selectRoomFragment_to_nav_tec_enter);
            }
            @Override
            public void onClickForNoRcStuList(int position, int sid) {   }
        };
        AdapterClassroom adapter = new AdapterClassroom(db,clickListener);
        binding.btnTecSQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.selectRoom(binding.txtTecSName.getText().toString());
                adapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return binding.getRoot();
    }

    class SimpleAPIWorker implements Runnable {
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
                JSONObject result = new JSONObject(response.body().string());
                Message m = selectClassResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("status", result.getInt("type"));
                bundle.putInt("roomList", result.getInt("list"));
                m.setData(bundle);
                selectClassResultHandler.sendMessage(m);
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