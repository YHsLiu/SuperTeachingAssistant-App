package abc.project.projectcheckinapp.ui.Student;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
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

    SQLiteDatabase db;
    private ContentValues contentValues ;
    private FragmentClasstableBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClasstableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //開資料庫

        db = getActivity().openOrCreateDatabase("ClassTableDB",MODE_PRIVATE,null);
        String drop_sql = "drop table if exists ClassTable;";
        String create_sql = "create table ClassTable (_id text primary key,Form_Name text);";
        db.execSQL(drop_sql);
        db.execSQL(create_sql);


        binding.t01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("修改課程");
                builder.setView(editText);
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.t01.setText(editText.getText());

                        //寫入資料庫
                        contentValues = new ContentValues();
                        contentValues.put("Form_Name",binding.t01.getText().toString());
                        db.update("ClassTable", contentValues, "_id = " + editText.getId(), null);

                        Toast.makeText(getActivity(), "課名寫入資料庫", Toast.LENGTH_SHORT).show();
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