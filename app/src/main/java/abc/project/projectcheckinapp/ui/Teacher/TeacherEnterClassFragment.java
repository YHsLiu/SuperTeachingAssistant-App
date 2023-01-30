package abc.project.projectcheckinapp.ui.Teacher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentTeacherEnterClassBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeacherEnterClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeacherEnterClassFragment extends Fragment {

    FragmentTeacherEnterClassBinding binding;
    NavController navController;

    public TeacherEnterClassFragment() {
        // Required empty public constructor
    }


    public static TeacherEnterClassFragment newInstance(String param1, String param2) {
        TeacherEnterClassFragment fragment = new TeacherEnterClassFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_enter_class, container, false);
    }
}