package abc.project.projectcheckinapp.Fragment.Teacher;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentTeacherMainBinding;

public class TeacherMainFragment extends Fragment {
    FragmentTeacherMainBinding binding;
    NavController navController;

    public TeacherMainFragment() {
    }

    public static TeacherMainFragment newInstance(String param1, String param2) {
        TeacherMainFragment fragment = new TeacherMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTeacherMainBinding.inflate(inflater,container,false);
        binding.btnTecMCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_tec_main_to_nav_tec_newclass);
            }
        });
        binding.btnTecMSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_nav_tec_main_to_selectRoomFragment);
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