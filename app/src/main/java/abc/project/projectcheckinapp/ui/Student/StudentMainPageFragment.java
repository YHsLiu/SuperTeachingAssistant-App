package abc.project.projectcheckinapp.ui.Student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentStudentmainpageBinding;

public class StudentMainPageFragment extends Fragment implements View.OnClickListener{

    FragmentStudentmainpageBinding binding;
    NavController navController;

    public StudentMainPageFragment() {
        // Required empty public constructor
    }

    public static StudentMainPageFragment newInstance(String param1, String param2) {
        StudentMainPageFragment fragment = new StudentMainPageFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStudentmainpageBinding.inflate(inflater, container, false);
        binding.btnGoclass.setOnClickListener(this);
        binding.btnHome.setOnClickListener(this);
        binding.btnInputclasscode.setOnClickListener(this);
        binding.btnGotoclass.setOnClickListener(this);
        binding.btnClasstable.setOnClickListener(this);
        binding.btnRevisedata.setOnClickListener(this);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_goclass:
                navController.navigate(R.id.nav_EnterClass);
                break;
            case R.id.btn_home:
                getActivity().onBackPressed();
                break;
            case R.id.btn_inputclasscode:
                navController.navigate(R.id.nav_inputCourseCode);
                break;
            case R.id.btn_gotoclass:
                navController.navigate(R.id.nav_EnterClass);
                break;
            case R.id.btn_classtable:
                navController.navigate(R.id.nav_classTable);
                break;
            case R.id.btn_revisedata:
                navController.navigate(R.id.nav_reviseStdData);
                break;
        }
    }
}