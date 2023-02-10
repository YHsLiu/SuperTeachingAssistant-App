package abc.project.projectcheckinapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import abc.project.projectcheckinapp.rawData.UniversityArray;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding bindingR;
    ExecutorService executor;
    Intent IntentR;      //註冊後跳轉頁面
    String url=null;
    SharedPreferences preferences, preferences2;
    SharedPreferences.Editor contextEditor;
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
                //判斷是老師or學生 決定跳轉頁
                IntentR = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(IntentR);


                //*****需要寫sharepreference 後再判斷身分跳轉*****
                /*if(bindingR.radioRegStd.isChecked()){
                    IntentR = new Intent(RegistrationActivity.this, StudentActivity.class);
                    startActivity(IntentR);
                }
                if(bindingR.radioRegTch.isChecked()){
                    IntentR = new Intent(RegistrationActivity.this,TeacherActivity.class);
                    startActivity(IntentR);
                }*/



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
        preferences = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //contextEditor = preferences2.edit();
        executor = Executors.newSingleThreadExecutor();
        resolver = this.getContentResolver();

        // spinner 設定
        SQLiteDatabase db = openOrCreateDatabase("UniversityInfo2",MODE_PRIVATE,null);
        Spinner spinner = bindingR.spinnerRegSchool;
        UniversityArray ua = new UniversityArray();

        ArrayList< String > universityArray;
        Cursor mycursor = ua.GetSpinnerFromDB(db);
        universityArray = ua.GetSpinnerArrayFromDB(db);
        String[] univName = new String[]{"univ_name"};
        int[] adapterRowViews = new int[]{android.R.id.text1};
        SimpleCursorAdapter adapter1 = new SimpleCursorAdapter(this,android.R.layout.simple_spinner_item
                ,mycursor,univName,adapterRowViews,0);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        spinner.setAdapter(adapter1);
        db.close();
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


        // RadioGroup 的事件處理
        bindingR.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            int identity = 0;
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton StdOrTch = (RadioButton)  findViewById(checkedId);
                //Toast.makeText(RegistrationActivity.this, "你選取的身分是: " + StdOrTch.getText() , Toast.LENGTH_SHORT).show();

            }
        });

        //處理註冊帳號按鈕
        bindingR.btnCreateAcc.setOnClickListener(new View.OnClickListener() {
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
                    data.put("acc", bindingR.txtRegAcc.getText().toString());
                    data.put("name", bindingR.txtRegName.getText().toString());
                    data.put("pwd", bindingR.txtRegPwd.getText().toString());
                    data.put("email", bindingR.txtRegMail.getText().toString());
                    if(bindingR.radioRegStd.isChecked()){
                        data.put("department", bindingR.txtRegDepart.getText().toString());
                        url = "http://20.2.232.79:8864/api/project/registration/student";
                    }
                    if(bindingR.radioRegTch.isChecked()){
                        url = "http://20.2.232.79/api/project/registration/teacher";
                    }
                    packet.put("data", data);
                    Log.w("API格式", packet.toString(4));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //使用網路通訊 Header+Body
                MediaType mytp = MediaType.parse("application/json");
                RequestBody rb = RequestBody.create(packet.toString(),mytp);
                Request request = new Request.Builder()
                        .url(url)
                        .post(rb)
                        .build();

                SimpaleAPIWorker apiCaller = new SimpaleAPIWorker(request);
                executor.execute(apiCaller);

            }
        });

        //處理頭象
        bindingR.imgReg.setOnClickListener(new View.OnClickListener() {
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
        //

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
                    bindingR.imgReg.setImageBitmap(image);
                }
                else if(resultCode == RESULT_CANCELED){
                    Toast.makeText(RegistrationActivity.this, "您取消了照相機", Toast.LENGTH_SHORT).show();
                }
                break;

            case GALLERY_REQUEST_CODE: //103
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();  //取得相片路徑
                    try {
                        //將該路徑的圖片轉成bitmap
                        Bitmap imageFromG = BitmapFactory.decodeStream(resolver.openInputStream(uri));
                        bindingR.imgReg.setImageBitmap(imageFromG);

                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }

                break;
        }

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