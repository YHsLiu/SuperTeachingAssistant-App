package abc.project.projectcheckinapp.ui.Teacher;

import static android.content.Context.MODE_PRIVATE;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import abc.project.projectcheckinapp.databinding.FragmentRecordBinding;
import abc.project.projectcheckinapp.rawData.AdapterRecord;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordFragment extends Fragment {

    FragmentRecordBinding binding;
    NavController navController;
    SharedPreferences preferences;
    ExecutorService executor;
    RecyclerView recyclerView;
    AdapterRecord adapter;
    SQLiteDatabase db;
    int cid;
    Handler RecordHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            preferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
            int cid = preferences.getInt("cid",0);
            JSONArray stuInfos;
            db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null);
            if (bundle.getInt("type") == 2) {  // semester紀錄
                db.execSQL("drop table if exists " + cid + "_record_semester;");
                db.execSQL("create table " + cid + "_record_semester(date text, stuId text,name text);");
                try { stuInfos = new JSONArray(bundle.getString("list"));
                      for (int i =0; i<stuInfos.length(); i++) {
                        JSONObject stuInfo = stuInfos.getJSONObject(i);
                        db.execSQL("insert into "+cid+"_record_semester values (?,?,?);",
                                new Object[] { stuInfo.getInt("日期"),
                                        stuInfo.getString("學號"),
                                        stuInfo.getString("姓名")});  }
                } catch (JSONException e) {  throw new RuntimeException(e);   }
            } else if (bundle.getInt("type") == 3) {  // 今日點名紀錄
                db.execSQL("drop table if exists " + cid + "_record_today;");
                db.execSQL("create table " + cid + "_record_today(stuId text,name text);");
                try { stuInfos = new JSONArray(bundle.getString("list"));
                    for (int i =0; i<stuInfos.length(); i++) {
                        JSONObject stuInfo = stuInfos.getJSONObject(i);
                        db.execSQL("insert into "+cid+"_record_today values (?,?);",
                                new Object[] { stuInfo.getString("學號"),
                                               stuInfo.getString("姓名")});  }
                } catch (JSONException e) {  throw new RuntimeException(e);   }
            }
            db.close();
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
        preferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
        cid = preferences.getInt("cid",0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String date = formatter.format(new Date());
        MediaType mediaType = MediaType.parse("application/json");
        JSONObject packet = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            packet.put("type", 1);
            packet.put("status", 11);
            data.put("cid", cid);
            data.put("date", date);
            packet.put("data", data);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(packet.toString(), mediaType);
        binding.btnSemeseterRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request.Builder()
                        .url("http://192.168.255.62:8864/api/record/semester")
                        .post(body)
                        .build();
                SimpleAPIWorker apiCaller = new SimpleAPIWorker(request);
                executor.execute(apiCaller);
                db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null);
                adapter = new AdapterRecord(db,cid);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                db.close();
            }
        });
        binding.btnTodayRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request.Builder()
                        .url("http://192.168.255.62:8864/api/record/today")
                        .post(body)
                        .build();
                SimpleAPIWorker apiCaller = new SimpleAPIWorker(request);
                executor.execute(apiCaller);
                db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null);
                adapter = new AdapterRecord(db,date,cid);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                db.close();
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