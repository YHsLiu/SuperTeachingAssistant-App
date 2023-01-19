package abc.project.projectcheckinapp.ui.Student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import abc.project.projectcheckinapp.databinding.FragmentEnterclassBinding;

public class EnterClassFragment extends Fragment {

    private FragmentEnterclassBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEnterclassBinding.inflate(inflater, container, false);
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