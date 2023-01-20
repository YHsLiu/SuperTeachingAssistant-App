package abc.project.projectcheckinapp.ui.Teacher;

import static android.content.Context.MODE_PRIVATE;

import android.app.VoiceInteractor;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import abc.project.projectcheckinapp.LoginActivity;
import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentLotteryBinding;
import abc.project.projectcheckinapp.databinding.FragmentStudentListBinding;
import abc.project.projectcheckinapp.rawData.AdapterAllStu;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentListFragment extends Fragment {
    FragmentStudentListBinding binding;
    NavController navController;
    SharedPreferences preferences;
    ExecutorService executor;
    RecyclerView recyclerView;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    public StudentListFragment() {
        // Required empty public constructor
    }


    public static StudentListFragment newInstance(String param1, String param2) {
        StudentListFragment fragment = new StudentListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    Handler studentListResultHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            JSONArray stuInfo;
            try {
                stuInfo =new JSONArray( bundle.getString("list"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            recyclerView =binding.RecyclerStuAll;
            recyclerView.setAdapter(new AdapterAllStu(stuInfo));
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentListBinding.inflate(inflater,container,false);
        preferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
        int cid = preferences.getInt("cid",0);
        JSONObject packet = new JSONObject();
        try {
            packet.put("type",1);
            packet.put("cid",cid);
        } catch (JSONException e) {
            Log.w("error","packet");
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(packet.toString(),mediaType);
        Request request = new Request.Builder()
                .url("http://192.168.255.62:8864/api/list/allStu")
                .post(body).build();
        SimpleAPIWorker simpleAPIWorker = new SimpleAPIWorker(request);
        executor.execute(simpleAPIWorker);

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
                String jsonString = response.body().string();
                Log.w("api","API回應:"+jsonString);
                JSONObject result = new JSONObject(jsonString);
                Message m = studentListResultHandler.obtainMessage();
                Bundle bundle = new Bundle();
                if ( result.getInt("type")==2){
                    String stuInfo = result.getJSONArray("list").toString();
                    bundle.putString("list",stuInfo);
                } else {
                    Log.e("api","api回應有問題");
                }
                m.setData(bundle);
                studentListResultHandler.sendMessage(m);
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