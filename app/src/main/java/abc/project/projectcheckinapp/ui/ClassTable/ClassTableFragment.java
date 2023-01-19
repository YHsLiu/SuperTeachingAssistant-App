package abc.project.projectcheckinapp.ui.ClassTable;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import abc.project.projectcheckinapp.databinding.FragmentEnterclassBinding;
import abc.project.projectcheckinapp.databinding.FragmentclasstableBinding;

public class ClassTableFragment extends Fragment {

    private FragmentclasstableBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ClassTableViewModel galleryViewModel =
                new ViewModelProvider(this).get(ClassTableViewModel.class);

        binding = FragmentclasstableBinding.inflate(inflater, container, false);
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