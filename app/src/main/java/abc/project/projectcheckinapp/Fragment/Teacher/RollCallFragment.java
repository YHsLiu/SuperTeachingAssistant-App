package abc.project.projectcheckinapp.Fragment.Teacher;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import abc.project.projectcheckinapp.databinding.FragmentRollCallBinding;
import abc.project.projectcheckinapp.Other.AdapterNoRollCallStudent;
import abc.project.projectcheckinapp.Other.ClickListener;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RollCallFragment extends Fragment {
    static int RollCallSwitch = 0; // 點名開關
    FragmentRollCallBinding binding;
    SharedPreferences sharedPreferences;
    int cid;
    ExecutorService executor;
    SQLiteDatabase db;
    String date;
    JSONObject packet,data;
    MediaType mediaType;
    RequestBody body;
    Request request;
    RecyclerView recyclerView;
    ClickListener clickListener;
    AdapterNoRollCallStudent adapter;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Boolean leave;
    public RollCallFragment() {
        // Required empty public constructor
    }

    Handler OpenResultHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle.getInt("status")==12){
                sharedPreferences.edit().putBoolean("RollCalling",true).apply();
                binding.btnRollCallSwitch.setText("結束點名");
                RollCallSwitch = 1;
            } else if(bundle.getInt("status")==13) {
                builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("今日已點名，是否刪除紀錄後重新點名?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request = new Request.Builder()
                                .url("http://20.2.232.79:8864/api/rollcall/teacher/open/again")
                                .post(body)
                                .build();
                        OpenRollCallAPIWorker apiCaller = new OpenRollCallAPIWorker(request);
                        executor.execute(apiCaller);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  RollCallSwitch = 0;  }
                });
                dialog = builder.create();
                dialog.show();
            }
        }
    };
    Handler CloseResultHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            binding.btnRollCallSwitch.setText("已點名");
            binding.btnRollCallSwitch.setEnabled(false);
            RollCallSwitch = 0;

            JSONArray stuNoRCInfos;
            db = getActivity().openOrCreateDatabase("allList",MODE_PRIVATE,null);
            db.execSQL("drop table if exists no_rc_stu_"+cid+";");
            db.execSQL("create table no_rc_stu_"+cid+"(sid integer,name text, depart text, stuId text);");
            try {
                stuNoRCInfos =new JSONArray( bundle.getString("list") );
                for (int i = 0; i<stuNoRCInfos.length();i++){
                    JSONObject stuInfo = stuNoRCInfos.getJSONObject(i);
                    db.execSQL("insert into no_rc_stu_"+cid+" values (?,?,?,?);",
                            new Object[] { stuInfo.getInt("sid"),
                                    stuInfo.getString("學生姓名"),
                                    stuInfo.getString("科系"),
                                    stuInfo.getString("學號")}); }
                adapter = new AdapterNoRollCallStudent(db,clickListener,cid);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            } catch (JSONException e) {
                Log.e("sqlite","The process of putting data into sqlite is wrong.");
            }
        }
    };

    public static RollCallFragment newInstance(String param1, String param2) {
        RollCallFragment fragment = new RollCallFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRollCallBinding.inflate(inflater, container, false);
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("RollCalling",false).apply();
        executor = Executors.newSingleThreadExecutor();
        cid = sharedPreferences.getInt("cid",0);
        recyclerView = binding.RecyclerStuAbsence;
        if (cid == 0){
            Toast.makeText(getActivity(), "請重新進入教室", Toast.LENGTH_SHORT).show();
        }

        date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        mediaType = MediaType.parse("application/json");
        packet = new JSONObject();
        data = new JSONObject();
        try {
            packet.put("type", 1);
            packet.put("status", 11);
            data.put("cid", cid);
            data.put("date", date);
            packet.put("data", data);
        } catch (JSONException e) {
            Log.e("JSON","There is an error in the process of packaging the JSON file, packet:"+packet);
        }
        body = RequestBody.create(packet.toString(), mediaType);
        binding.btnRollCallSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RollCallSwitch == 0) {
                    request = new Request.Builder()
                            .url("http://20.2.232.79:8864/api/rollcall/teacher/open")
                            .post(body)
                            .build();
                    OpenRollCallAPIWorker apiCaller = new OpenRollCallAPIWorker(request);
                    executor.execute(apiCaller);
                } else {
                    sharedPreferences.edit().putBoolean("RollCalling",false).apply();
                    request = new Request.Builder()
                            .url("http://20.2.232.79:8864/api/rollcall/teacher/close")
                            .post(body)
                            .build();
                    CloseRollCallAPIWorker apiCaller2 = new CloseRollCallAPIWorker(request);
                    executor.execute(apiCaller2);
                }
            }
        });
        clickListener = new ClickListener() {
            @Override
            public void onClickForAllStuList(int position, int sid, String stuname, String studepart, String stuid) {  }
            @Override
            public void onClickForClassroom(int position, int cid,String classname) {  }
            @Override
            public void onClickForNoRcStuList(int position, int sid) {
                JSONObject ManualRollCallInfo = new JSONObject();
                try {
                    ManualRollCallInfo.put("cid", cid);
                    ManualRollCallInfo.put("sid",sid);
                    ManualRollCallInfo.put("date", date);
                } catch (JSONException e) {
                    Log.e("JSON","There is an error in the process of packaging the JSON file, ManualRollCallInfo:"+ManualRollCallInfo);
                }
                request = new Request.Builder()
                        .url("http://20.2.232.79:8864/api/rollcall/manual/call")
                        .post(RequestBody.create(ManualRollCallInfo.toString(), mediaType))
                        .build();
                ManualRollCallAPIWorker apiCaller = new ManualRollCallAPIWorker(request);
                executor.execute(apiCaller);
            }
        };
        return binding.getRoot();
    }

    class OpenRollCallAPIWorker implements Runnable {
        OkHttpClient client;
        Request request;
        public OpenRollCallAPIWorker(Request request) {
            this.request = request;
            client = new OkHttpClient();
        }
        @Override
        public void run() {
            try {
                Response response = client.newCall(request).execute();
                JSONObject result = new JSONObject(response.body().string());
                Message m = OpenResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("status", result.getInt("status"));
                m.setData(bundle);
                OpenResultHandler.sendMessage(m);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    class CloseRollCallAPIWorker implements Runnable {
        OkHttpClient client;
        Request request;
        public CloseRollCallAPIWorker (Request request) {
            this.request = request;
            client = new OkHttpClient();
        }
        @Override
        public void run() {
            try {
                Response response = client.newCall(request).execute();
                String apidata = response.body().string();
                Log.w("api","Close api回應:"+apidata);
                JSONObject result = new JSONObject(apidata);
                Message m = CloseResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                if ( result.getInt("type")==2){
                    String stuNoRCInfo = result.getJSONArray("list").toString();
                    bundle.putString("list",stuNoRCInfo);
                    m.setData(bundle);
                    CloseResultHandler.sendMessage(m);
                } else {
                    Log.e("error","api回應有問題");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    class BackCheckAPIWorker implements Runnable {
        OkHttpClient client;
        Request request;
        public BackCheckAPIWorker (Request request) {
            this.request = request;
            client = new OkHttpClient();
        }
        @Override
        public void run() {
            try {
                client.newCall(request).execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    class ManualRollCallAPIWorker implements Runnable {
        OkHttpClient client;
        Request request;
        public ManualRollCallAPIWorker (Request request) {
            this.request = request;
            client = new OkHttpClient();
        }
        @Override
        public void run() {
            try {
                client.newCall(request).execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    // back & home 鍵的ClickListener事件設定
                    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME){
                        if (RollCallSwitch == 1){
                            builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("點名中無法離開此頁面，是否結束點名?");
                            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RollCallSwitch = 0;
                                    leave = false;
                                    request = new Request.Builder()
                                            .url("http://20.2.232.79:8864/api/rollcall/teacher/close")
                                            .post(body)
                                            .build();
                                    CloseRollCallAPIWorker apiCaller2 = new CloseRollCallAPIWorker(request);
                                    executor.execute(apiCaller2);
                                }
                            });
                            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {    } });
                            dialog = builder.create();
                            dialog.show();
                        } else {
                            sharedPreferences.edit().putBoolean("RollCalling",false).apply();
                            return false;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        getParentFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (RollCallSwitch == 1) {
                    RollCallSwitch = 0;
                    request = new Request.Builder()
                            .url("http://20.2.232.79:8864/api/rollcall/teacher/close")
                            .post(body)
                            .build();
                    BackCheckAPIWorker apiCaller2 = new BackCheckAPIWorker(request);
                    executor.execute(apiCaller2);
                }
            }
        });
    }
}