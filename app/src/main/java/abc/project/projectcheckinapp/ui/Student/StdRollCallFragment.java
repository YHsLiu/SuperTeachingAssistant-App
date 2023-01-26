package abc.project.projectcheckinapp.ui.Student;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStdRollCallBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}