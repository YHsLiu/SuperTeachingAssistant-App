package abc.project.projectcheckinapp.ui.Student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import abc.project.projectcheckinapp.databinding.FragmentInputcoursecodeBinding;

public class InputCourseCodeFragment extends Fragment {

    private FragmentInputcoursecodeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInputcoursecodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       // final TextView textView = binding.textGallery;
       // galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}