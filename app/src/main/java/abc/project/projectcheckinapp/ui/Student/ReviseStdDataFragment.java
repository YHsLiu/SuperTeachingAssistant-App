package abc.project.projectcheckinapp.ui.Student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentReviseStdDataBinding;


public class ReviseStdDataFragment extends Fragment {

    FragmentReviseStdDataBinding binding;
    NavController navController;

    public ReviseStdDataFragment() {
        // Required empty public constructor
    }

    public static ReviseStdDataFragment newInstance(String param1, String param2) {
        ReviseStdDataFragment fragment = new ReviseStdDataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //contextEditor.putInt("sid",bundle.getInt("userID"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviseStdDataBinding.inflate(inflater, container, false);
        binding.txtReviseTitle.setHint("");















        return binding.getRoot();
    }
}