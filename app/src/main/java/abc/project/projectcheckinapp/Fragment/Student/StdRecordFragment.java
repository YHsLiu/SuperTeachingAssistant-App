package abc.project.projectcheckinapp.Fragment.Student;

import static android.content.Context.MODE_PRIVATE;

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
import abc.project.projectcheckinapp.databinding.FragmentStdRecordBinding;
import abc.project.projectcheckinapp.Other.ActionBarTitleSetter;
import abc.project.projectcheckinapp.Other.AdapterTeacherRecord;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StdRecordFragment extends Fragment {

    FragmentStdRecordBinding binding;
    NavController navController;
    SharedPreferences preferences;
    ExecutorService executor;
    RecyclerView recyclerView;
    AdapterTeacherRecord adapter;
    SQLiteDatabase db;
    int cid, sid;

    public StdRecordFragment() {
        // Required empty public constructor
    }

    Handler RecordHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            preferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
            int cid = preferences.getInt("cid",0);
            JSONArray stuRrcord;
            db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null);
            // semester紀錄
            db.execSQL("drop table if exists record_student_"+cid+";");
            db.execSQL("create table record_student_"+cid+"(sid int,rc_date text);");
            try { stuRrcord = new JSONArray(bundle.getString("list"));
                for (int i =0; i<stuRrcord.length(); i++) {
                    JSONObject recordInfo = stuRrcord.getJSONObject(i);
                    db.execSQL("insert into record_student_"+cid+" values (?,?);",
                            new Object[] { recordInfo.getInt("sid"),
                                    recordInfo.getString("日期")});  }
            } catch (JSONException e) {  throw new RuntimeException(e);   }
            adapter = new AdapterTeacherRecord(db,cid,sid);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    };
    public static StdRecordFragment newInstance(String param1, String param2) {
        StdRecordFragment fragment = new StdRecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //時間存至共用sharedPreferences
        //preferences.edit().putString("data",currentDate).apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStdRecordBinding.inflate(inflater, container, false);
        preferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
        cid = preferences.getInt("cid",0);
        sid = preferences.getInt("sid",0);
        String classname = preferences.getString("classname","0");
        ((ActionBarTitleSetter)getActivity()).setTitle(classname);
        recyclerView = binding.recyclerView;
        executor = Executors.newSingleThreadExecutor();

        binding.btnStdRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaType mediaType = MediaType.parse("application/json");
                JSONObject packet = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    packet.put("type", 1);
                    packet.put("status", 11);
                    data.put("cid", cid);
                    data.put("sid", sid);
                    Log.w("sid","學生id:"+sid);
                    packet.put("data",data);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                RequestBody body = RequestBody.create(packet.toString(), mediaType);
                Request request = new Request.Builder()
                        .url("http://20.2.232.79:8864/api/record/student")
                        .post(body)
                        .build();
                SimpleAPIWorker apiCaller = new SimpleAPIWorker(request);
                executor.execute(apiCaller);
            }
        });

        binding.imageRecoRollcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_nav_stdRecord_to_nav_stdRollCall);

            }
        });



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
                String fromAPI = response.body().string();
                Log.e("fromapi","fromapi:"+fromAPI);
                JSONObject result = new JSONObject(fromAPI);
                Message m = RecordHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("list", result.getString("list"));
                m.setData(bundle);
                RecordHandler.sendMessage(m);
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