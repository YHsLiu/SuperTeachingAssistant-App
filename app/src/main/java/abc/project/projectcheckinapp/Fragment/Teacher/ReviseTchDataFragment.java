package abc.project.projectcheckinapp.Fragment.Teacher;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import abc.project.projectcheckinapp.databinding.FragmentReviseTchDataBinding;
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
    int tid;

    Handler GetTchDataHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                try {

                    JSONObject tchData = new JSONObject(bundle.getString("Tchdata"));
                    Log.e("api:",bundle.getString("Tchdata"));
                    binding.txtTCHreviseTCHacc.setText(tchData.getString("教師編號"));
                    binding.txtTCHreviseName.setText(tchData.getString("教師姓名"));
                    binding.txtTCHrevisePwd.setText(tchData.getString("密碼"));
                    binding.txtTCHreviseMail.setText(tchData.getString("信箱"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


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
        executor = Executors.newSingleThreadExecutor();
        tid = preferences.getInt("tid",0);
        packet = new JSONObject();
        data = new JSONObject();
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
                .url("http://20.2.232.79:8864/api/project/GetData/teacher")
                .post(rb)
                .build();
        GetTchDataAPI getTchDataAPI = new GetTchDataAPI(request);
        executor.execute(getTchDataAPI);

        //處理修改資料按鈕事件
        binding.btnUpdateTChData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tid = preferences.getInt("tid",0);
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
                        .url("http://20.2.232.79:8864/api/project/UpdataData/teacher")
                        .post(rb)
                        .build();
                UpdateTchDataAPI updateTchDataAPI = new UpdateTchDataAPI(request);
                executor.execute(updateTchDataAPI);



            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
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
                Message m = GetTchDataHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("Tchdata",objectFromAPI.toString());
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