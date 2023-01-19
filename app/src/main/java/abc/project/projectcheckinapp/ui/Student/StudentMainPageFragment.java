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
        binding.btnToB.setOnClickListener(this);
        binding.btnHome.setOnClickListener(this);

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
            case R.id.btn_toB:
                navController.navigate(R.id.nav_EnterClass);
                break;
            case R.id.btn_home:
                getActivity().onBackPressed();
                break;

        }
    }
}