package abc.project.projectcheckinapp.ui.Teacher;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.ExecutorService;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentLotteryBinding;
import abc.project.projectcheckinapp.databinding.FragmentStudentListBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentListFragment extends Fragment {
    FragmentStudentListBinding binding;
    NavController navController;
    SharedPreferences preferences;
    ExecutorService executor;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    public StudentListFragment() {
        // Required empty public constructor
    }


    public static StudentListFragment newInstance(String param1, String param2) {
        StudentListFragment fragment = new StudentListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudentListBinding.inflate(inflater,container,false);
        preferences = getActivity().getSharedPreferences("userInfo",MODE_PRIVATE);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}