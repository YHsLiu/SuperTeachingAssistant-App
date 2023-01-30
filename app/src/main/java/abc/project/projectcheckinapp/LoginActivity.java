package abc.project.projectcheckinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import abc.project.projectcheckinapp.databinding.ActivityLoginBinding;
import abc.project.projectcheckinapp.rawData.UniversityArray;
import abc.project.projectcheckinapp.ui.test.TeacherActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    ExecutorService executor;
    SharedPreferences preferences;
    Intent intent;
    SharedPreferences.Editor contextEditor;
    Spinner spinner;

    Handler loginResultHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            preferences = getSharedPreferences("userInfo",MODE_PRIVATE);
            contextEditor = LoginActivity.this.preferences.edit(); // 所有Activity共用
            if (bundle.getInt("status" )==11) // 登入成功
            {   contextEditor.putBoolean("isStudent",binding.radioLoginStudent.isChecked()); // 判別師/生
                contextEditor.putBoolean("isLogin",true); // 已登入
                // 跳轉到 老師 /學生 頁面
                if (binding.radioLoginStudent.isChecked()){
                    intent = new Intent(LoginActivity.this, StudentActivity.class );
                    contextEditor.putInt("sid",bundle.getInt("userID"));
                    contextEditor.apply();
                    startActivity(intent);
                } else {
                    intent = new Intent(LoginActivity.this, TeacherActivity.class );
                    contextEditor.putInt("tid",bundle.getInt("userID"));
                    contextEditor.apply();
                    startActivity(intent);
                }
            } else {
                Toast.makeText(LoginActivity.this, ""+bundle.getInt("status"), Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this, "無此帳號，請確認資料是否正確或建立帳號", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        executor = Executors.newSingleThreadExecutor();
        preferences = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // spinner 設定
        spinner = binding.spinnerLoginSchool;
        Boolean FirstTime = preferences.getBoolean("isFirstTime",true);
        UniversityArray ua = new UniversityArray();
        ArrayList< String > universityArray;
        SQLiteDatabase db = openOrCreateDatabase("UniversityInfo2",MODE_PRIVATE,null); //
        if ( FirstTime ) {
            universityArray = ua.FirstTimeGetSpinner(getResources().openRawResource(R.raw.university),db);
            preferences.edit().putBoolean("isFirstTime",false).apply();
            db.close();
            ArrayAdapter adapter = new ArrayAdapter(LoginActivity.this
                    , android.R.layout.simple_spinner_item, universityArray);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
            spinner.setAdapter(adapter);
        } else {
            Cursor mycursor = ua.GetSpinnerFromDB(db);
            universityArray = ua.GetSpinnerArrayFromDB(db);
            String[] univName = new String[]{"univ_name"};
            int[] adapterRowViews = new int[]{android.R.id.text1};
            SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item
                    ,mycursor,univName,adapterRowViews,0);
            adapter1.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
            spinner.setAdapter(adapter1);
            db.close();
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String univer = universityArray.get(position);
                editor.putString("univer",univer).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnLoginCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳轉道註冊畫面
                intent = new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });
        binding.btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject packet = new JSONObject();
                JSONObject data = new JSONObject();
                String univer = preferences.getString("univer","");
                try {
                    packet.put("type",1);
                    packet.put("status" , 10);
                    packet.put("msg","登錄資料打包中");
                    data.put("acc",binding.txtLoginAcc.getText().toString());
                    data.put("pwd",binding.txtLoginPwd.getText().toString());
                    data.put("univ",univer);
                    packet.put("data",data);
                    // Toast.makeText(LoginActivity.this, data.toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.w("api","請檢查click的cord");
                }
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(packet.toString(),mediaType);
                Request request;
                if (binding.radioLoginStudent.isChecked()){
                    request = new Request.Builder()
                            .url("http://192.168.1.162:8864/api/member/login/student")
                            .post(body)
                            .build();
                } else {
                    request = new Request.Builder()
                            .url("http://192.168.1.162:8864/api/member/login/teacher")
                            .post(body)
                            .build();
                }
                SimpleAPIWorker apiCaller = new SimpleAPIWorker(request);
                executor.execute(apiCaller);
            }
        });
        // checkbox
        Boolean Remember = preferences.getBoolean("isRemember",false);
        if (Remember){
            binding.txtLoginAcc.setText(preferences.getString("account",""));
            binding.txtLoginPwd.setText(preferences.getString("password",""));
            spinner.setSelection(preferences.getInt("university",0));
            binding.checkLoginRemenber.setChecked(true);
        }
        binding.checkLoginRemenber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    editor.putString("account",binding.txtLoginAcc.getText().toString());
                    editor.putString("password",binding.txtLoginPwd.getText().toString());
                    editor.putInt("university",spinner.getSelectedItemPosition());
                    editor.putBoolean("isStudent",binding.radioLoginStudent.isChecked());
                    editor.putBoolean("isRemember",true);
                } else {
                    editor.putString("account","");
                    editor.putString("password","");
                    editor.putInt("university",0);
                    editor.putBoolean("isStudent",true);
                    editor.putBoolean("isRemember",false);
                }
                editor.apply();
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
                    bundle.putInt("userID",result.getInt("userId"));
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