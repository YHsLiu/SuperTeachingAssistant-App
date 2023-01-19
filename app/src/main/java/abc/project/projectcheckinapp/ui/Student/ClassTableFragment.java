package abc.project.projectcheckinapp.ui.Student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import abc.project.projectcheckinapp.databinding.FragmentClasstableBinding;

public class ClassTableFragment extends Fragment {

    private FragmentClasstableBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClasstableBinding.inflate(inflater, container, false);
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