package abc.project.projectcheckinapp.ui.Teacher;

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
import abc.project.projectcheckinapp.databinding.FragmentNewClassBinding;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewClassFragment extends Fragment {
    FragmentNewClassBinding binding;
    NavController navController;
    SharedPreferences preferences;
    SharedPreferences.Editor contextEditor;
    ExecutorService executor;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Handler createClassResultHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (bundle.getInt("status" )==11) // 新增課程成功
            {   builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("新增成功");
                builder.setMessage("直接進入教室嗎?");
                builder.setPositiveButton("進教室", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        preferences = getActivity().getSharedPreferences("userInfo",Context.MODE_PRIVATE);
                        contextEditor = preferences.edit();
                        contextEditor.putInt("cid",bundle.getInt("cid")).apply();
                        navController.navigate(R.id.action_nav_tec_newclass_to_nav_tec_enter);
                    }
                });
                builder.setNegativeButton("關閉", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {    }
                });
                dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(getActivity(), "新增失敗,代碼已被使用", Toast.LENGTH_LONG).show();
            }
        }
    };
    public NewClassFragment() {
        // Required empty public constructor
    }

    public static NewClassFragment newInstance(String param1, String param2) {
        NewClassFragment fragment = new NewClassFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewClassBinding.inflate(inflater,container,false);
        preferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int tid = preferences.getInt("tid",0);
        binding.btnTecNCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject packet = new JSONObject();
                JSONObject data = new JSONObject();
                SimpleDateFormat year = new SimpleDateFormat("yyyy");
                int semester;  // 判斷學年
                if (Integer.parseInt(new SimpleDateFormat("MM").format(new Date()))>=9) {
                    semester = Integer.parseInt(year.format(new Date())) - 1911;
                } else {
                    semester = Integer.parseInt(year.format(new Date())) - 1912;
                }
                try {
                    packet.put("type",1);
                    packet.put("status",10);
                    data.put("tid",tid);
                    data.put("className",binding.txtTecNName.getText().toString());
                    data.put("classCode",binding.txtTecNCord.getText().toString());
                    data.put("semester",semester);
                    packet.put("classInfo",data);
                } catch (JSONException e) {
                    Log.e("error","packet");
                }
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(packet.toString(),mediaType);
                Request request= new Request.Builder()
                        .url("http://192.168.255.62:8864/api/createclass")
                        .post(body)
                        .build();
                SimpleAPIWorker apiCaller = new SimpleAPIWorker(request);
                executor.execute(apiCaller);
            }
        });

        return binding.getRoot();
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
                JSONObject result = new JSONObject(response.body().string());
                Message m = createClassResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("status",result.getInt("status") );
                bundle.putInt("cid",result.getInt("cid") );
                m.setData(bundle);
                createClassResultHandler.sendMessage(m);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}