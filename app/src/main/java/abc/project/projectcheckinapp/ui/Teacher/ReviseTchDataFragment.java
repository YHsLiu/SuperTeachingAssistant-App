package abc.project.projectcheckinapp.ui.Teacher;

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

import java.util.concurrent.ExecutorService;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentReviseStdDataBinding;
import abc.project.projectcheckinapp.databinding.FragmentReviseTchDataBinding;
import abc.project.projectcheckinapp.ui.Student.ReviseStdDataFragment;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReviseTchDataFragment extends Fragment {

    FragmentReviseTchDataBinding binding;
    NavController navController;
    SharedPreferences preferences;
    ExecutorService executor;
    JSONObject packet, data ;

    Handler GetTchDataHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                binding.txtTCHreviseTCHacc.setText(bundle.getString("acc"));
                binding.txtTCHreviseName.setText(bundle.getString("name"));
                binding.txtTCHrevisePwd.setText(bundle.getString("pwd"));
                binding.txtTCHreviseMail.setText(bundle.getString("email"));

            }
        };

    Handler UpdateTchDataHandler = new Handler(Looper.getMainLooper()){
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


    public ReviseTchDataFragment() {
        // Required empty public constructor
    }

    public static ReviseTchDataFragment newInstance(String param1, String param2) {
        ReviseTchDataFragment fragment = new ReviseTchDataFragment();
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
        binding = FragmentReviseTchDataBinding.inflate(inflater, container, false);
        preferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int tid = preferences.getInt("tid",0);
        packet = new JSONObject();
        try {
            packet.put("type",1);
            packet.put("status",10);
            data.put("tid",tid);
            packet.put("data",data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RequestBody rb = RequestBody.create(packet.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url("http://192.168.255.67:8864/api/project/GetData/teacher")
                .post(rb)
                .build();
        GetTchDataAPI getTchDataAPI = new GetTchDataAPI(request);
        executor.execute(getTchDataAPI);
        //處理修改資料按鈕事件
        binding.btnUpdateTChData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject revPacket = new JSONObject();
                JSONObject revData = new JSONObject();
                try {
                    revPacket.put("type",1);
                    revPacket.put("status",10);
                    revData.put("tid",tid);
                    revData.put("acc",binding.txtTCHreviseTCHacc.getText());
                    revData.put("name",binding.txtTCHreviseName.getText());
                    revData.put("pwd",binding.txtTCHrevisePwd.getText());
                    revData.put("email",binding.txtTCHreviseMail.getText());
                    revPacket.put("data",revData);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                RequestBody rb = RequestBody.create(revPacket.toString(),MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url("http://192.168.255.67:8864/api/project/UpdataData/teacher")
                        .post(rb)
                        .build();
                UpdateTchDataAPI updateTchDataAPI = new UpdateTchDataAPI(request);
                executor.execute(updateTchDataAPI);



            }
        });


        return binding.getRoot();
    }




    class GetTchDataAPI implements Runnable{

        OkHttpClient client;
        Request request;
        public GetTchDataAPI(Request request){
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
                Message m = GetTchDataHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("name",data.getString("教師姓名"));
                bundle.putString("univ",data.getString("學校"));
                bundle.putString("email",data.getString("信箱"));
                bundle.putString("acc",data.getString("教師編號"));
                bundle.putString("pwd",data.getString("密碼"));
                m.setData(bundle);
                GetTchDataHandler.sendMessage(m);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }
    }

    class UpdateTchDataAPI implements Runnable{

        OkHttpClient client;
        Request request;
        public UpdateTchDataAPI(Request request){
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
                Message m = UpdateTchDataHandler.obtainMessage();
                m.setData(bundle);
                UpdateTchDataHandler.sendMessage(m);
            }catch (Exception e) {
                throw new RuntimeException(e);
            }


        }
    }
}