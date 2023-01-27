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
import abc.project.projectcheckinapp.databinding.FragmentEnterclassBinding;

public class EnterClassFragment extends Fragment {

    private FragmentEnterclassBinding binding;
    NavController navController;

    public EnterClassFragment() {
    }

    public static EnterClassFragment newInstance(String param1, String param2) {
        EnterClassFragment fragment = new EnterClassFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEnterclassBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 NavController 切換到 classTableFragement
                // 請使用 Mobile_navigation 中的 Action ID
                navController.navigate(R.id.nav_stdRollCall);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

}