package abc.project.projectcheckinapp.ui.Student;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import abc.project.projectcheckinapp.databinding.FragmentClasstableBinding;

public class ClassTableFragment extends Fragment {

    private FragmentClasstableBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClasstableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.text11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("修改課程");
                builder.setView(editText);
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.text11.setText(editText.getText());
                        Toast.makeText(getActivity(), "確認修改課程", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getActivity(), "你按了取消", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();

            }
        });





        return root;
    }







    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}