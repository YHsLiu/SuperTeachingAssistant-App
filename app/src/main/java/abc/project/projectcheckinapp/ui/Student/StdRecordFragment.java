package abc.project.projectcheckinapp.ui.Student;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.ExecutorService;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentReviseStdDataBinding;
import abc.project.projectcheckinapp.databinding.FragmentStdRecordBinding;
import abc.project.projectcheckinapp.ui.Teacher.RecordFragment;
import okhttp3.Request;
import okhttp3.RequestBody;

public class StdRecordFragment extends Fragment {

    FragmentStdRecordBinding binding;
    NavController navController;
    SharedPreferences preferences;
    ExecutorService executor;
    RecyclerView recyclerView;
    int cid, sid;

    public StdRecordFragment() {
        // Required empty public constructor
    }

    public static StdRecordFragment newInstance(String param1, String param2) {
        StdRecordFragment fragment = new StdRecordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //時間存至共用sharedPreferences
        //preferences.edit().putString("data",currentDate).apply();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStdRecordBinding.inflate(inflater, container, false);
        preferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
        cid = preferences.getInt("cid",0);
        sid = preferences.getInt("sid",0);


        binding.btnStdRecord.setOnClickListener(new View.OnClickListener() {
            @Override        //要改
            public void onClick(View v) {
                RequestBody body = RequestBody.create(packet.toString(), mediaType);
                Request request = new Request.Builder()
                        .url("http://192.168.255.67:8864/api/project/stdRecord")
                        .post(body)
                        .build();
                RecordFragment.SimpleAPIWorker apiCaller = new RecordFragment.SimpleAPIWorker(request);
                executor.execute(apiCaller);
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