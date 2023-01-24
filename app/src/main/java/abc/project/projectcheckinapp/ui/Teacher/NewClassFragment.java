package abc.project.projectcheckinapp.ui.Teacher;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import abc.project.projectcheckinapp.databinding.FragmentNewClassBinding;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewClassFragment extends Fragment {
    FragmentNewClassBinding binding;
    NavController navController;
    SharedPreferences preferences;
    ExecutorService executor;
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
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}