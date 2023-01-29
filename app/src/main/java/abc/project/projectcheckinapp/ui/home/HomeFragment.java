package abc.project.projectcheckinapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import abc.project.projectcheckinapp.LoginActivity;
import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.RegistrationActivity;
import abc.project.projectcheckinapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private NavController navController;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        binding.btnMainLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
            }
        });


        binding.btnToA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 NavController 切換到 classTableFragement
                // 請使用 Mobile_navigation 中的 Action ID
                navController.navigate(R.id.nav_stdMainPage);
            }
        });
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
}