package abc.project.projectcheckinapp.ui.Teacher;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentTeacherEnterClassBinding;
import abc.project.projectcheckinapp.rawData.ActionBarTitleSetter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeacherEnterClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherEnterClassFragment extends Fragment {

    FragmentTeacherEnterClassBinding binding;
    NavController navController;
    SharedPreferences preferences;
    Bundle bundle;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTeacherEnterClassBinding.inflate(inflater, container, false);
        preferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String classname = preferences.getString("classname","無資料");
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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}