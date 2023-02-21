package abc.project.projectcheckinapp.Fragment.Student;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentInputcoursecodeBinding;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InputCourseCodeFragment extends Fragment {

    private FragmentInputcoursecodeBinding binding;
    ExecutorService executor;
    NavController navController;    //Global宣告, 在onViewCreated方法中找出navController
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    Handler inputCodeHandler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            Bundle bundle2 = msg.getData();
            editor = preferences.edit();
            if(bundle2.getInt("status")==16){
                editor.putString("classname",bundle2.getString("mesg")).commit();           //課程名稱共用給其他Activity

                builder.setTitle("成功新增課程");
                builder.setPositiveButton("進入課程", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//跳轉至課程頁面
                        editor.putInt("cid",bundle2.getInt("cid",0)).apply();              //課程代碼cid共用
                        navController.navigate(R.id.action_nav_inputCourseCode_to_nav_EnterClass);
                    }
                });
                builder.setNeutralButton("關閉", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
            else{
                binding.txtStuAErrorMesg.setVisibility(View.VISIBLE);
            }
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInputcoursecodeBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();
        preferences = getActivity().getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        executor = Executors.newSingleThreadExecutor();
        //處理Continue按鈕
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// 取得輸入的代碼 傳給IJ確認資料庫是否存在 (Y):跳出Dialog 顯示新增成功+btn跳轉至教室頁 (N):跳出紅字 顯示無此課程
                JSONObject packet = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    packet.put("type",1);
                    packet.put("status",15);
                    packet.put("mesg","課程代碼封包傳送中");
                    data.put("code",binding.txtStuACord.getText().toString());
                    data.put("sid", preferences.getInt("sid",0));
                    packet.put("data",data);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                MediaType mtyp = MediaType.parse("application/json");
                RequestBody rb = RequestBody.create(packet.toString(),mtyp);
                Request request = new Request.Builder()
                        .url("http://20.2.232.79:8864/api/project/InputClassCode")
                        .post(rb)
                        .build();
                simpleAPIworker api = new simpleAPIworker(request);
                executor.execute(api);

            }
        });


        return binding.getRoot();

    }

    @Override
    //當view已經建好 才可以執行會改變UI畫面的行為
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);                             //找出navController

    }





    class simpleAPIworker implements Runnable{
        OkHttpClient client;
        Request request;

        public simpleAPIworker(Request request){
            client = new OkHttpClient();
            this.request = request ;
        }

        @Override
        public void run() {
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                Log.w("api回應", responseBody);
                JSONObject result = new JSONObject(responseBody);
                Message m = inputCodeHandler.obtainMessage();
                Bundle bundle = new Bundle();
                if(result.getInt("status")==16){                         //成功加入課程
                    bundle.putString("mesg",result.getString("mesg"));   //會抓出課程名稱
                    bundle.putInt("status",result.getInt("status"));
                    bundle.putInt("cid",result.getInt("cid"));
                }
                else {
                    bundle.putString("mesg","查無此課程");
                    bundle.putInt("status",result.getInt("status"));
                }
                m.setData(bundle);
                inputCodeHandler.sendMessage(m);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}

