package abc.project.projectcheckinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;

import abc.project.projectcheckinapp.databinding.ActivityLotteryBinding;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LotteryActivity extends AppCompatActivity {

    ActivityLotteryBinding binding;
    Intent intent;
    Button lotteryBtn;
    SharedPreferences preferences;
    ExecutorService executor;
    Handler lotteryResultHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle.getInt("status" )==11){
                String name = bundle.getString("stuName");
                String id = bundle.getString("stuId");
                String depart = bundle.getString("stuDep");
                AlertDialog.Builder builder = new AlertDialog.Builder(LotteryActivity.this);
                builder.setTitle("抽到的是");
                builder.setMessage("學生姓名:"+name+"  學號:"+id+"  科系:"+depart);
                builder.setPositiveButton("關閉", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {                    }
                });
                builder.setNegativeButton("下一位", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JSONObject packet = new JSONObject();
                        // 在點名的地方要設Boolean(rollCall) & date 來check有無點名與點名紀錄
                        Boolean rollCall = preferences.getBoolean("rollCall",false);
                        int date = preferences.getInt("date",0);
                        int cid = preferences.getInt("cid",0);
                        try {
                            packet.put("type",1);
                            packet.put("status",10);
                            packet.put("rollCall",rollCall);
                            packet.put("cid",cid);
                            packet.put("date",date);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(packet.toString(),mediaType);
                        Request request;
                        request = new Request.Builder()
                                .url("http://192.168.255.62:8864/api/lottery/bingo")
                                .post(body)
                                .build();
                        SimpleAPIWorker apiCaller = new SimpleAPIWorker(request);
                        executor.execute(apiCaller);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(LotteryActivity.this);
                builder.setTitle("抽完了");
                builder.setMessage("學生都抽過囉!");
                builder.setPositiveButton("再抽一次", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        JSONObject packet = new JSONObject();
                        try {
                            packet.put("type",1);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        MediaType mediaType = MediaType.parse("application/json");
                        RequestBody body = RequestBody.create(packet.toString(),mediaType);
                        Request request = new Request.Builder()
                                .url("http://192.168.255.62:8864/api/lottery/clear")
                                .post(body)
                                .build();
                        ClearAPIWorker apiWorker = new ClearAPIWorker(request);
                        executor.execute(apiWorker);
                    }
                });
                builder.setNegativeButton("關閉", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                            }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLotteryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // 要從前面的SharedPreferences帶入cid
        preferences = this.getSharedPreferences("app_config",MODE_PRIVATE);
        int cid = preferences.getInt("cid",0);
        if ( cid == 0 ){
            Toast.makeText(this, "無課程資料，請先重新進入教室或回報問題", Toast.LENGTH_LONG).show();
        }
        lotteryBtn = binding.buttonLottery;
        lotteryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject packet = new JSONObject();
                // 在點名的地方要設Boolean(rollCall) & date 來check有無點名與點名紀錄
                Boolean rollCall = preferences.getBoolean("rollCall",false);
                int date = preferences.getInt("date",0);
                try {
                    packet.put("type",1);
                    packet.put("status",10);
                    packet.put("rollCall",rollCall);
                    packet.put("cid",cid);
                    packet.put("date",date);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(packet.toString(),mediaType);
                Request request;
                request = new Request.Builder()
                            .url("http://192.168.255.62:8864/api/lottery/bingo")
                            .post(body)
                            .build();
                SimpleAPIWorker apiCaller = new SimpleAPIWorker(request);
                executor.execute(apiCaller);
            }
        });
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
                JSONObject stuInfo = result.getJSONObject("stuInfo");
                Message m = lotteryResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                if ( result.getInt("status")==11){
                    bundle.putString("stuName",stuInfo.getString("學生姓名"));
                    bundle.putString("stuId",stuInfo.getString("學號"));
                    bundle.putString("stuDep",stuInfo.getString("科系"));
                    bundle.putInt("status",result.getInt("status") );
                } else {
                    bundle.putString("mes", "名單已抽完");
                    bundle.putInt("status",result.getInt("status") );
                }
                m.setData(bundle);
                lotteryResultHandler.sendMessage(m);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    class ClearAPIWorker implements Runnable{
        OkHttpClient client;
        Request request;
        public ClearAPIWorker() {        }

        public ClearAPIWorker(Request request) {
            this.request = request;
            client = new OkHttpClient();
        }
        @Override
        public void run() {
            try {
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();
                Log.w("api","API回應:"+jsonString);
                //JSONObject result = new JSONObject(jsonString);
                //Message m = lotteryResetResultHandler.obtainMessage();
                //Bundle bundle = new Bundle();
               // bundle.putString("type",result.getString("type"));
               // m.setData(bundle);
                //lotteryResetResultHandler.sendMessage(m);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}