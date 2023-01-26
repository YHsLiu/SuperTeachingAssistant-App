package abc.project.projectcheckinapp.ui.Student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentReviseStdDataBinding;
import abc.project.projectcheckinapp.databinding.FragmentStdRecordBinding;

public class StdRecordFragment extends Fragment {

    FragmentStdRecordBinding binding;
    NavController navController;

    public StdRecordFragment() {
        // Required empty public constructor
    }

    public static StdRecordFragment newInstance(String param1, String param2) {
        StdRecordFragment fragment = new StdRecordFragment();
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
        binding = FragmentStdRecordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}