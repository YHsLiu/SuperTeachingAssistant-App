package abc.project.projectcheckinapp.ui.Student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import abc.project.projectcheckinapp.databinding.FragmentEnterclassBinding;

public class EnterClassFragment extends Fragment {

    private FragmentEnterclassBinding binding;
    NavController navController;



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

        return root;
    }

}