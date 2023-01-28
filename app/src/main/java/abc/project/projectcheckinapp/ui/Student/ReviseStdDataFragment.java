package abc.project.projectcheckinapp.ui.Student;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

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

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentReviseStdDataBinding;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ReviseStdDataFragment extends Fragment {

    FragmentReviseStdDataBinding binding;
    NavController navController;
    SharedPreferences preferences;
    ExecutorService executor;
    JSONObject packet, data ;

    Handler GetDataHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            binding.txtReviseDepart.setText(bundle.getString("department"));
            binding.txtReviseSTDacc.setText(bundle.getString("acc"));
            binding.txtReviseName.setText(bundle.getString("name"));
            binding.txtRevisePwd.setText(bundle.getString("pwd"));
            binding.txtReviseMail.setText(bundle.getString("email"));

        }
    };

    Handler UpdateDataHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if(bundle.getInt("status")==11)
                Toast.makeText(getActivity(), bundle.getString("mesg"), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(), bundle.getString("mesg"), Toast.LENGTH_SHORT).show();

        }
    };

    public ReviseStdDataFragment() {
        // Required empty public constructor
    }

    public static ReviseStdDataFragment newInstance(String param1, String param2) {
        ReviseStdDataFragment fragment = new ReviseStdDataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    //contextEditor.putInt("sid",bundle.getInt("userID"));
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviseStdDataBinding.inflate(inflater, container, false);
        preferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int sid = preferences.getInt("sid",0);
        packet = new JSONObject();
        try {
            packet.put("type",1);
            packet.put("status",10);
            data.put("sid",sid);
            packet.put("data",data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RequestBody rb = RequestBody.create(packet.toString(),MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url("http://192.168.255.67:8864/api/project/GetData/student")
                .post(rb)
                .build();
        GetDataAPI getDataAPI = new GetDataAPI(request);
        executor.execute(getDataAPI);

        //處理修改資料按鈕事件
        binding.btnReviseAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject revPacket = new JSONObject();
                JSONObject revData = new JSONObject();
                try {
                    revPacket.put("type",1);
                    revPacket.put("status",10);
                    revData.put("sid",sid);
                    revData.put("department",binding.txtReviseDepart.getText());
                    revData.put("acc",binding.txtReviseSTDacc.getText());
                    revData.put("name",binding.txtReviseName.getText());
                    revData.put("pwd",binding.txtRevisePwd.getText());
                    revData.put("email",binding.txtReviseMail.getText());
                    revPacket.put("data",revData);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                RequestBody rb = RequestBody.create(revPacket.toString(),MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url("http://192.168.255.67:8864/api/project/UpdataData/student")
                        .post(rb)
                        .build();
                UpdateDataAPI updateDataAPI = new UpdateDataAPI(request);
                executor.execute(updateDataAPI);
            }
        });

        return binding.getRoot();
    }





     class GetDataAPI implements Runnable{

        OkHttpClient client;
        Request request;
        public GetDataAPI(Request request){
            client = new OkHttpClient();
            this.request = request ;}
        @Override
        public void run() {

            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                Log.w("api回應",responseBody);
                JSONObject objectFromAPI = new JSONObject(responseBody);
                JSONObject data = objectFromAPI.getJSONObject("data");
                Message m = GetDataHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("name",data.getString("學生姓名"));
                bundle.putString("univ",data.getString("學校"));
                bundle.putString("department",data.getString("科系"));
                bundle.putString("email",data.getString("信箱"));
                bundle.putString("acc",data.getString("學號"));
                bundle.putString("pwd",data.getString("密碼"));
                m.setData(bundle);
                GetDataHandler.sendMessage(m);


            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }
    }


    class UpdateDataAPI implements Runnable{

        OkHttpClient client;
        Request request;
        public UpdateDataAPI(Request request){
            client = new OkHttpClient();
            this.request = request ;}
        @Override
        public void run() {

            try {
                Bundle bundle = new Bundle();
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    bundle.putInt("status",11);
                    bundle.putString("mesg", "資料更新成功");
                } else {
                    bundle.putInt("status",12);
                    bundle.putString("mesg", "資料更新失敗");
                }
                Message m = UpdateDataHandler.obtainMessage();
                m.setData(bundle);
                UpdateDataHandler.sendMessage(m);
            }catch (Exception e) {
                throw new RuntimeException(e);
            }


        }
    }





}