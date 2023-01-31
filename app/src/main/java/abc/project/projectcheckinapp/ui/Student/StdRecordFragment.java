package abc.project.projectcheckinapp.ui.Student;

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

import java.util.concurrent.ExecutorService;

import abc.project.projectcheckinapp.databinding.FragmentStdRecordBinding;
import abc.project.projectcheckinapp.rawData.AdapterRecord;
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
    AdapterRecord adapter;
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
            db.execSQL("drop table if exists " + cid + "_record_student;");
            db.execSQL("create table " + cid + "_record_student(sid int,rc_date text);");
            try { stuRrcord = new JSONArray(bundle.getString("list"));
                for (int i =0; i<stuRrcord.length(); i++) {
                    JSONObject recordInfo = stuRrcord.getJSONObject(i);
                    db.execSQL("insert into "+cid+"_record_student values (?,?);",
                            new Object[] { recordInfo.getInt("sid"),
                                    recordInfo.getString("日期")});  }
            } catch (JSONException e) {  throw new RuntimeException(e);   }
            db.close();
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
        recyclerView = binding.recyclerView;

        /*binding.btnStdRecord.setOnClickListener(new View.OnClickListener() {
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
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                RequestBody body = RequestBody.create(packet.toString(), mediaType);
                Request request = new Request.Builder()
                        .url("http://192.168.255.67:8864/api/record/student")
                        .post(body)
                        .build();
                SimpleAPIWorker apiCaller = new SimpleAPIWorker(request);
                executor.execute(apiCaller);
                db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null);
                adapter = new AdapterRecord(db,cid,sid);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                db.close();
            }
        });*/
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