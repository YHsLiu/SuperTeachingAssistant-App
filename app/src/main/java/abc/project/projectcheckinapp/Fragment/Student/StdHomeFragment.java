package abc.project.projectcheckinapp.Fragment.Student;

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
import abc.project.projectcheckinapp.databinding.FragmentStdHomeBinding;

public class StdHomeFragment extends Fragment implements View.OnClickListener {

    private FragmentStdHomeBinding binding;
    private NavController navController;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStdHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.btnInputclasscode.setOnClickListener(this);
        binding.btnGotoclass.setOnClickListener(this);
        binding.btnClasstable.setOnClickListener(this);
        binding.btnRevisedata.setOnClickListener(this);

        return root;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

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