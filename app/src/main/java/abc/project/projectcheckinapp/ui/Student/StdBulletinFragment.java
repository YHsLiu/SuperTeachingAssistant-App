package abc.project.projectcheckinapp.ui.Student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentReviseStdDataBinding;
import abc.project.projectcheckinapp.databinding.FragmentStdBulletinBinding;


public class StdBulletinFragment extends Fragment {

    FragmentStdBulletinBinding binding;
    NavController navController;

    public StdBulletinFragment() {
        // Required empty public constructor
    }


    public static StdBulletinFragment newInstance(String param1, String param2) {
        StdBulletinFragment fragment = new StdBulletinFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStdBulletinBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}