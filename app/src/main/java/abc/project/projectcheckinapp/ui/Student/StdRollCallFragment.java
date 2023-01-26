package abc.project.projectcheckinapp.ui.Student;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentReviseStdDataBinding;
import abc.project.projectcheckinapp.databinding.FragmentStdRollCallBinding;

public class StdRollCallFragment extends Fragment {

    FragmentStdRollCallBinding binding;
    NavController navController;
    SharedPreferences preferences;
    String classname;

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
        //把課程名稱從sharedPreferences中取出並顯示
        classname = getActivity().getSharedPreferences("classcode",MODE_PRIVATE).getString("classname","");
        binding.txtStu1ClassName.setText(classname);
        //設定簽到按鈕
        binding.btnStu1Checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStdRollCallBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}