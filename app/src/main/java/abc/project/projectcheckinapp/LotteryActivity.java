package abc.project.projectcheckinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    Handler loginResultHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle.getInt("status" )==11){
                contextEditor = LoginActivity.this.getSharedPreferences("user_info",MODE_PRIVATE).edit();
                contextEditor.putString("userID",binding.txtLoginAcc.getText().toString());
                contextEditor.putBoolean("isLogin",true);
                contextEditor.apply();
                preferences = getSharedPreferences("app_config",MODE_PRIVATE);
                // 跳轉到 老師 /學生 頁面
                /*if (binding.radioLoginStudent.isChecked()){
                    intent = new Intent(LoginActivity.this, 學生Activity.class );
                    // 好像用SharedPreference比較好
                    //Bundle bundleToOther = new Bundle();
                    //bundleToOther.putString("stuId",binding.txtLoginAcc.getText().toString()); // 這是學號
                    //intent.putExtras(bundleToOther); // 學號帶到學生畫面
                    preferences.edit().putString("學號",binding.txtLoginAcc.toString()).apply();
                    startActivity(intent);
                } else {
                    intent = new Intent(LoginActivity.this, 老師Activity.class );
                    preferences.edit().putString("教師編號",binding.txtLoginAcc.toString()).apply();
                    startActivity(intent);
                }*/
            } else {
                contextEditor = LoginActivity.this.getSharedPreferences("user_info",MODE_PRIVATE).edit();
                contextEditor.putString("userID","");
                contextEditor.putBoolean("isLogin",false);
                contextEditor.apply();
                // 加錯誤小視窗
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
                Message m = loginResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                if ( result.getInt("status")==11){
                    bundle.putString("mes",result.getString("mes"));
                    bundle.putInt("status",result.getInt("status") );
                } else {
                    bundle.putString("mes", "登入失敗");
                    bundle.putInt("status",result.getInt("status") );
                }
                m.setData(bundle);
                loginResultHandler.sendMessage(m);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}