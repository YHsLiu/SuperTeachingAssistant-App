package abc.project.projectcheckinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import abc.project.projectcheckinapp.databinding.ActivityRegistrationBinding;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding bindingR;
    ExecutorService executor;
    Intent IntentR;      //註冊後跳轉頁面

    Handler regResultHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if( bundle.getInt("status")== 11) {
                Toast.makeText(RegistrationActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegistrationActivity.this, bundle.getString("mesg"), Toast.LENGTH_LONG).show();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingR = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(bindingR.getRoot());

        executor = Executors.newSingleThreadExecutor();

        //處理註冊帳號按鈕
        bindingR.btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject packet = new JSONObject();
                try {
                    packet.put("type", 1);
                    packet.put("status", 10);
                    packet.put("mesg", "註冊資料 測試封包");
                    JSONObject data = new JSONObject();
                    data.put("department", bindingR.txtRegDepart.getText().toString());
                    data.put("acc", bindingR.txtRegAcc.getText().toString());
                    data.put("name", bindingR.txtRegName.getText().toString());
                    data.put("pwd", bindingR.txtRegPwd.getText().toString());
                    data.put("email", bindingR.txtRegMail.getText().toString());
                    packet.put("data", data);
                    Log.w("API格式", packet.toString(4));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //使用網路通訊 Header+Body
                MediaType mytp = MediaType.parse("application/json");
                RequestBody rb = RequestBody.create(packet.toString(),mytp);
                Request request = new Request.Builder()
                        .url("http://192.168.255.67:8864/api/project/registration")
                        .post(rb)
                        .build();

                SimpaleAPIWorker apiCaller = new SimpaleAPIWorker(request);
                executor.execute(apiCaller);

                //判斷是老師or學生 決定跳轉頁
                //IntentR = new Intent(RegistrationActivity.this,MainActivity.class);




            }
        });




    }

    class SimpaleAPIWorker implements  Runnable {
        OkHttpClient client;
        Request request ;

        public SimpaleAPIWorker(Request request) {
            client = new OkHttpClient();
            this.request = request;
        }

        @Override
        public void run() {
            try {
                Response response = client.newCall(request).execute();
                String responseString = response.body().string();
                Log.w("api回應", responseString);
                // Response 也是 JSON格式回傳後 由 app端進行分析 確認登入結果
                JSONObject result = new JSONObject(responseString);
                Message m = regResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                if( result.getInt("status")== 11) {                       //表示註冊成功
                    bundle.putString("mesg", result.getString("mesg"));
                    bundle.putInt("status",result.getInt("status") );
                } else {                                                         //表示重複註冊 已有帳號
                    bundle.putString("mesg", "帳號已存在, 請直接登入");
                    bundle.putInt("status",result.getInt("status") );
                }
                m.setData(bundle);
                regResultHandler.sendMessage(m);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





}