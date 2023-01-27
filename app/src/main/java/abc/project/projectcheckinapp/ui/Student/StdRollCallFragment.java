package abc.project.projectcheckinapp.ui.Student;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentReviseStdDataBinding;
import abc.project.projectcheckinapp.databinding.FragmentStdRollCallBinding;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StdRollCallFragment extends Fragment {

    FragmentStdRollCallBinding binding;
    NavController navController;
    String classname;
    SimpleDateFormat dateFormat ;
    Date date ;
    String currentDate ;
    ExecutorService executor;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    Handler StdRollCallHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            super.handleMessage(msg);
            Bundle bundle2 = msg.getData();
            if(bundle2.getInt("status")==21) {
                //關閉按鈕
                binding.btnStu1Checkin.setEnabled(false);
            }
            else{
                Toast.makeText(getActivity(), "簽到失敗請再試一次", Toast.LENGTH_SHORT).show();
            }

        }
    };

    public StdRollCallFragment() {

    }

    public static StdRollCallFragment newInstance(String param1, String param2) {
        StdRollCallFragment fragment = new StdRollCallFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStdRollCallBinding.inflate(inflater, container, false);
        //binding.btnStu1Checkin.setEnabled(true);
        //把課程名稱從sharedPreferences中取出並顯示
        preferences = getActivity().getSharedPreferences("claacode",Context.MODE_PRIVATE);
        //classname = preferences.getString("classname","0");
        //binding.txtStu1ClassName.setText(classname);
        //設定簽到按鈕
        binding.btnStu1Checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //顯示簽到時間
                dateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
                date = new Date(System.currentTimeMillis());
                currentDate = dateFormat.format(date);
                binding.btnStu1Checkin.setText(currentDate+"已簽到");
                //時間存至共用sharedPreferences
                preferences.edit().putString("data",currentDate).apply();

                //時間存至DB
                JSONObject packet = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    packet.put("type",1);
                    packet.put("status",20);
                    data.put("stdDate",currentDate);
                    packet.put("data",data);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody rb = RequestBody.create(packet.toString(),mediaType);
                Request request = new Request.Builder()
                        .url("http://192.168.255.67:8864/api/project/StdRollCall")
                        .post(rb)
                        .build();
                StdRollCallFragment.simpleAPIworker api = new StdRollCallFragment.simpleAPIworker(request);
                executor.execute(api);

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
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
                Message m = StdRollCallHandler.obtainMessage();
                Bundle bundle = new Bundle();
                if(result.getInt("status")==21){                         //日期成功寫入資料庫
                    bundle.putString("mesg",result.getString("mesg"));
                    bundle.putInt("status",result.getInt("status"));
                }
                else {
                    bundle.putString("mesg","日期存取失敗");
                    bundle.putInt("status",result.getInt("status"));
                }
                m.setData(bundle);
                StdRollCallHandler.sendMessage(m);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }
    }



}