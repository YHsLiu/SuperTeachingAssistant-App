package abc.project.projectcheckinapp.Fragment.Teacher;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import abc.project.projectcheckinapp.databinding.FragmentRecordBinding;
import abc.project.projectcheckinapp.Other.AdapterTeacherRecord;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecordFragment extends Fragment {

    FragmentRecordBinding binding;
    NavController navController;
    SharedPreferences sharedPreferences;
    MediaType mediaType;
    ExecutorService executor;
    RecyclerView recyclerView;
    AdapterTeacherRecord adapter;
    SQLiteDatabase db;
    SimpleDateFormat formatter;
    int cid;
    String date;
    JSONObject packet , data;
    Handler RecordHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            JSONArray stuInfos;
            db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null);
            if (bundle.getInt("type") == 2) {  // semester紀錄
                db.execSQL("drop table if exists record_semester_"+cid+";");
                db.execSQL("create table record_semester_"+cid+"(rc_date text, stuId text,name text);");
                try { stuInfos = new JSONArray(bundle.getString("list"));
                      for (int i =0; i<stuInfos.length(); i++) {
                        JSONObject stuInfo = stuInfos.getJSONObject(i);
                        db.execSQL("insert into record_semester_"+cid+" values (?,?,?);",
                                new Object[] { stuInfo.getString("日期"),
                                        stuInfo.getString("學號"),
                                        stuInfo.getString("學生姓名")});  }
                    db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null);
                    adapter = new AdapterTeacherRecord(db,cid);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                } catch (JSONException e)
                {  throw new RuntimeException(e);   }
            } else if (bundle.getInt("type") == 3) {  // 今日點名紀錄
                db.execSQL("drop table if exists record_today_"+cid+";");
                db.execSQL("create table record_today_"+cid+"(stuId text,name text);");
                try { stuInfos = new JSONArray(bundle.getString("list"));
                    for (int i =0; i<stuInfos.length(); i++) {
                        JSONObject stuInfo = stuInfos.getJSONObject(i);
                        db.execSQL("insert into record_today_"+cid+" values (?,?);",
                                new Object[] { stuInfo.getString("學號"),
                                               stuInfo.getString("學生姓名")});  }
                } catch (JSONException e)
                {  throw new RuntimeException(e);   }
                db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null);
                adapter = new AdapterTeacherRecord(db,date,cid);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }
    };
    public RecordFragment() {
        // Required empty public constructor
    }
    public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
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
        binding = FragmentRecordBinding.inflate(inflater, container, false);
        recyclerView = binding.RecyclerRecord;
        executor = Executors.newSingleThreadExecutor();
        sharedPreferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
        cid = sharedPreferences.getInt("cid",0);

        mediaType = MediaType.parse("application/json");
        packet = new JSONObject();
        data = new JSONObject();
        formatter = new SimpleDateFormat("yyyyMMdd");
        binding.btnSemeseterRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    packet.put("type", 1);
                    packet.put("status", 11);
                    data.put("cid", cid);
                    packet.put("data", data);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Request request = new Request.Builder()
                        .url("http://20.2.232.79:8864/api/record/semester")
                        .post(RequestBody.create(packet.toString(), mediaType))
                        .build();
                RecordAPIWorker apiCaller = new RecordAPIWorker(request);
                executor.execute(apiCaller);
            }
        });

        binding.btnTodayRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = formatter.format(new Date());
                try {
                    packet.put("type", 1);
                    packet.put("status", 11);
                    data.put("cid", cid);
                    data.put("date",date);
                    packet.put("data", data);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Request request = new Request.Builder()
                        .url("http://20.2.232.79:8864/api/record/today")
                        .post(RequestBody.create(packet.toString(), mediaType))
                        .build();
                RecordAPIWorker apiCaller = new RecordAPIWorker(request);
                executor.execute(apiCaller);

            }
        });
        return binding.getRoot();
    }
    class RecordAPIWorker implements Runnable {
        OkHttpClient client;
        Request request;
        public RecordAPIWorker(Request request) {
            this.request = request;
            client = new OkHttpClient();
        }
        @Override
        public void run() {
            try {
                Response response = client.newCall(request).execute();
                JSONObject result = new JSONObject(response.body().string());
                Message m = RecordHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("type", result.getInt("type"));
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