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
import abc.project.projectcheckinapp.Other.UniversityArray;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    ExecutorService executor;
    Intent intent;
    SharedPreferences preferences, sharedPreferences;
    SharedPreferences.Editor editor, sharedEditor;
    Spinner spinner;
    ArrayList< String > universityArray;

    Handler loginResultHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            sharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
            sharedEditor = sharedPreferences.edit();
            if (bundle.getInt("status" )==11) // 登入成功
            {   sharedEditor.putBoolean("Login",true);
                // 跳轉到 老師 /學生 頁面
                if (binding.radioLoginStudent.isChecked()){
                    intent = new Intent(LoginActivity.this, StudentActivity.class );
                    sharedEditor.putInt("sid",bundle.getInt("userID"));
                    sharedEditor.apply();
                    startActivity(intent);
                } else {
                    intent = new Intent(LoginActivity.this, TeacherActivity.class );
                    sharedEditor.putInt("tid",bundle.getInt("userID"));
                    sharedEditor.apply();
                    startActivity(intent);
                }
            } else {
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
        editor = preferences.edit();

        // spinner 設定
        spinner = binding.spinnerLoginSchool;
        editor.putBoolean("InitialSpinner",true).apply();
        UniversityArray ua = new UniversityArray();
        SQLiteDatabase db = openOrCreateDatabase("UniversitySpinner",MODE_PRIVATE,null); //
        if ( preferences.getBoolean("InitialSpinner",true) ) {
            universityArray = ua.InitializeSpinner(getResources().openRawResource(R.raw.university),db);
            editor.putBoolean("InitialSpinner",false).apply();
            db.close();
            ArrayAdapter InitialSpinnerAdapter = new ArrayAdapter(LoginActivity.this
                    , android.R.layout.simple_spinner_item, universityArray);
            InitialSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
            spinner.setAdapter(InitialSpinnerAdapter);
        } else {
            Cursor UniversityCursor = ua.SpinnerFromDB(db);
            universityArray = ua.SpinnerArrayFromDB(db);
            String[] univName = new String[]{"univ_name"};
            int[] adapterRowViews = new int[]{android.R.id.text1};
            SimpleCursorAdapter SpinnerAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item
                    ,UniversityCursor,univName,adapterRowViews,0);
            SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
            spinner.setAdapter(SpinnerAdapter);
            db.close();
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String univer = universityArray.get(position);
                editor.putString("univer",univer).apply();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        // checkbox
        Boolean Remember = preferences.getBoolean("Remember",false);
        if (Remember){
            binding.txtLoginAcc.setText(preferences.getString("account",""));
            binding.txtLoginPwd.setText(preferences.getString("password",""));
            spinner.setSelection(preferences.getInt("university",0));
            binding.checkLoginRemenber.setChecked(true);
        } else {
            binding.txtLoginAcc.setText("");
            binding.txtLoginPwd.setText("");
            spinner.setSelection(0);
            binding.checkLoginRemenber.setChecked(false);
        }

        binding.btnLoginCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    data.put("acc",binding.txtLoginAcc.getText().toString());
                    data.put("pwd",binding.txtLoginPwd.getText().toString());
                    data.put("univ",univer);
                    packet.put("data",data);
                } catch (JSONException e) {
                    Log.w("api","請檢查click的cord");
                }
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(packet.toString(),mediaType);
                Request request;
                if (binding.radioLoginStudent.isChecked()){
                    request = new Request.Builder()
                            .url("http://20.2.232.79:8864/api/member/login/student")
                            .post(body)
                            .build();
                } else {
                    request = new Request.Builder()
                            .url("http://20.2.232.79:8864/api/member/login/teacher")
                            .post(body)
                            .build();
                }
                SimpleAPIWorker apiCaller = new SimpleAPIWorker(request);
                executor.execute(apiCaller);

                if (binding.checkLoginRemenber.isChecked()){
                    editor.putString("account",binding.txtLoginAcc.getText().toString());
                    editor.putString("password",binding.txtLoginPwd.getText().toString());
                    editor.putInt("university",spinner.getSelectedItemPosition());
                    editor.putBoolean("isStudent",binding.radioLoginStudent.isChecked());
                    editor.putBoolean("Remember",true);
                } else {
                    editor.putString("account","");
                    editor.putString("password","");
                    editor.putInt("university",0);
                    editor.putBoolean("isStudent",true);
                    editor.putBoolean("Remember",false);
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