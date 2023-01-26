package abc.project.projectcheckinapp.ui.Student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import abc.project.projectcheckinapp.R;

public class StdRollCallFragment extends Fragment {


    public StdRollCallFragment() {

    }


    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_std_roll_call, container, false);
    }
}