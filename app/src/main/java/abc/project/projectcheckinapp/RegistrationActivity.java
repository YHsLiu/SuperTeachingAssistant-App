package abc.project.projectcheckinapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import abc.project.projectcheckinapp.databinding.ActivityRegistrationBinding;
import abc.project.projectcheckinapp.Other.UniversityArray;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    ExecutorService executor;
    Intent Intent;      //註冊後跳轉頁面
    String url=null;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 103;
    private ContentResolver resolver;

    Handler regResultHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if( bundle.getInt("status")== 11) {
                Toast.makeText(RegistrationActivity.this, "註冊成功", Toast.LENGTH_SHORT).show();
                Intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(Intent);
            } else {
                Toast.makeText(RegistrationActivity.this, bundle.getString("mesg"), Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = this.getPreferences(MODE_PRIVATE);
        editor = preferences.edit();
        executor = Executors.newSingleThreadExecutor();
        resolver = this.getContentResolver();

        // spinner 設定
        SQLiteDatabase db = openOrCreateDatabase("UniversitySpinner",MODE_PRIVATE,null);
        Spinner spinner = binding.spinnerRegSchool;
        UniversityArray ua = new UniversityArray();

        ArrayList< String > universityArray;
        Cursor UniversityCursor = ua.SpinnerFromDB(db);
        universityArray = ua.SpinnerArrayFromDB(db);
        String[] univName = new String[]{"univ_name"};
        int[] adapterRowViews = new int[]{android.R.id.text1};
        SimpleCursorAdapter SpinnerAdapter = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item
                ,UniversityCursor,univName,adapterRowViews,0);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner.setAdapter(SpinnerAdapter);
        db.close();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String univer = universityArray.get(position);
                editor.putString("univer",univer).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        //處理註冊帳號按鈕
        binding.btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject packet = new JSONObject();
                String univer = preferences.getString("univer","");  //spinner 選的大學內容
                try {
                    packet.put("type", 1);
                    packet.put("status", 10);
                    packet.put("mesg", "註冊資料 測試封包");
                    JSONObject data = new JSONObject();
                    data.put("univ",univer);
                    data.put("acc", binding.txtRegAcc.getText().toString());
                    data.put("name", binding.txtRegName.getText().toString());
                    data.put("pwd", binding.txtRegPwd.getText().toString());
                    data.put("email", binding.txtRegMail.getText().toString());
                    if(binding.radioGroupIdentity.getCheckedRadioButtonId() == R.id.radio_reg_std){
                        data.put("department", binding.txtRegDepart.getText().toString());
                        url = "http://20.2.232.79:8864/api/project/registration/student";
                    } else {
                        url = "http://20.2.232.79:8864/api/project/registration/teacher";
                    }
                    packet.put("data", data);
                } catch (JSONException e) {
                    Log.w("JSON", "The JSON file is packaged incorrectly, packet:"+packet);
                }
                //使用網路通訊 Header+Body
                MediaType mytp = MediaType.parse("application/json");
                RequestBody rb = RequestBody.create(packet.toString(),mytp);
                Request request = new Request.Builder()
                        .url(url)
                        .post(rb)
                        .build();
                RegisterAPIWorker apiCaller = new RegisterAPIWorker(request);
                executor.execute(apiCaller);
            }
        });

        //處理頭象
        binding.imgReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                builder.setTitle("修改大頭貼");
                builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        askCameraPermissions();
                    }
                });
                builder.setNeutralButton("開啟相簿", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentToG = new Intent();
                        intentToG.setType("image/*");
                        intentToG.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intentToG,GALLERY_REQUEST_CODE);
                    }
                });
                builder.show();
            }
        });

    }
    private void askCameraPermissions() {
        //檢查有沒有跟使用者要權限
        //如果使用者「同意權限」PERMISSION_GRANTED、「拒絕權限」PERMISSION_DENIED
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //要求使用者給予權限
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CAMERA},CAMERA_PERM_CODE);
            Log.e("context的permission", String.valueOf(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)));
            Log.e("PackageManager的permission", String.valueOf(PackageManager.PERMISSION_GRANTED));
            //第三個參數 自定義的請求代碼
        }
        else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case CAMERA_PERM_CODE:
                //如果允許了(用int值檢查)
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                    //Log.e("requestCode", String.valueOf(requestCode));
                    Log.e("grantResults", String.valueOf(grantResults[0]));
                    Log.e("PackageManager的permission", String.valueOf(PackageManager.PERMISSION_GRANTED));
                }
                //如果拒絕了
                else
                    Toast.makeText(this, "Camera permission is required to use camera", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    //requestCode的意思是接收從哪個跳轉頁面的指令發出的訊息
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w("requestCode", String.valueOf(requestCode));
        Log.w("resultCode", String.valueOf(resultCode));
        Log.w("resultOK", String.valueOf(RESULT_OK));
        switch(requestCode){

            case CAMERA_REQUEST_CODE : //102
                if(resultCode == RESULT_OK){
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    binding.imgReg.setImageBitmap(image);
                }
                else if(resultCode == RESULT_CANCELED){
                    Toast.makeText(RegistrationActivity.this, "您取消了照相機", Toast.LENGTH_SHORT).show();
                }
                break;

            case GALLERY_REQUEST_CODE: //103
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();  //取得相片路徑
                    try { //將該路徑的圖片轉成bitmap
                          Bitmap imageFromG = BitmapFactory.decodeStream(resolver.openInputStream(uri));
                          binding.imgReg.setImageBitmap(imageFromG);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
        }
    }


    class RegisterAPIWorker implements  Runnable {
        OkHttpClient client;
        Request request ;

        public RegisterAPIWorker(Request request) {
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