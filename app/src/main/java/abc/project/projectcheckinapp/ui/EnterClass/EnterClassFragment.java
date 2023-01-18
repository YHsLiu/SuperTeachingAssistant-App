package abc.project.projectcheckinapp.ui.EnterClass;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import abc.project.projectcheckinapp.databinding.FragmentEnterclassBinding;
import abc.project.projectcheckinapp.databinding.FragmentInputcoursecodeBinding;

public class EnterClassFragment extends Fragment {

    private FragmentEnterclassBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EnterClassViewModel galleryViewModel =
                new ViewModelProvider(this).get(EnterClassViewModel.class);

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