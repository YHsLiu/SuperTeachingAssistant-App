package abc.project.projectcheckinapp.Fragment.Teacher;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentTeacherEnterClassBinding;
import abc.project.projectcheckinapp.Other.ActionBarTitleSetter;

public class TeacherEnterClassFragment extends Fragment {

    FragmentTeacherEnterClassBinding binding;
    NavController navController;
    SharedPreferences sharedPreferences;

    public TeacherEnterClassFragment() {
        // Required empty public constructor
    }

    public static TeacherEnterClassFragment newInstance(String param1, String param2) {
        TeacherEnterClassFragment fragment = new TeacherEnterClassFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = FragmentTeacherEnterClassBinding.inflate(inflater, container, false);
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String classname = sharedPreferences.getString("classname","無資料");
        ((ActionBarTitleSetter)getActivity()).setTitle(classname);

        binding.btnLottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_tec_enter_to_nav_tec_lottery);
            }
        });
        binding.btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_tec_enter_to_recordFragment);
            }
        });
        binding.btnRollcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_tec_enter_to_rollCallFragment);
            }
        });
        binding.btnStuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_tec_enter_to_nav_tec_stulist);
            }
        });

        if (sharedPreferences.getBoolean("RollCalling",false)) {
            Toast.makeText(getActivity(), "已關閉點名", Toast.LENGTH_SHORT).show();
            sharedPreferences.edit().putBoolean("RollCalling",false);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}