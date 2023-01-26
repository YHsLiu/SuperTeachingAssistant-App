package abc.project.projectcheckinapp.ui.Student;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentClasstableBinding;

public class ClassTableFragment extends Fragment implements View.OnClickListener{

    SQLiteDatabase db;
    private FragmentClasstableBinding binding;
    String name;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClasstableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Cursor cursor = db.rawQuery("select * from ClassTable",null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            String[] i = new String[20] ;
            int x = 0 ;
            do{
                name = cursor.getString(1);
                i[x]=name;
                x++;

            }while(cursor.moveToNext());
            binding.t01.setText(i[0]);
            binding.t02.setText(i[1]);
            binding.t03.setText(i[2]);
            binding.t04.setText(i[3]);
            binding.t05.setText(i[4]);
            binding.t06.setText(i[5]);
            binding.t07.setText(i[6]);
            binding.t08.setText(i[7]);
            binding.t09.setText(i[8]);
            binding.t10.setText(i[9]);
            binding.t11.setText(i[10]);
            binding.t12.setText(i[11]);
            binding.t13.setText(i[12]);
            binding.t14.setText(i[13]);
            binding.t15.setText(i[14]);
            binding.t16.setText(i[15]);
            binding.t17.setText(i[16]);
            binding.t18.setText(i[17]);
            binding.t19.setText(i[18]);
            binding.t20.setText(i[19]);


        }


        //開資料庫

        db = getActivity().openOrCreateDatabase("ClassTableDB",MODE_PRIVATE,null);
        String drop_sql = "drop table if exists ClassTable;";
        String create_sql = "create table ClassTable (_id text primary key,Form_Name text);";
        db.execSQL(drop_sql);
        db.execSQL(create_sql);
        db.close();




        binding.t01.setOnClickListener(this);
        binding.t02.setOnClickListener(this);
        binding.t03.setOnClickListener(this);
        binding.t04.setOnClickListener(this);
        binding.t05.setOnClickListener(this);
        binding.t06.setOnClickListener(this);
        binding.t07.setOnClickListener(this);
        binding.t08.setOnClickListener(this);
        binding.t09.setOnClickListener(this);
        binding.t10.setOnClickListener(this);
        binding.t11.setOnClickListener(this);
        binding.t12.setOnClickListener(this);
        binding.t13.setOnClickListener(this);
        binding.t14.setOnClickListener(this);
        binding.t15.setOnClickListener(this);
        binding.t16.setOnClickListener(this);
        binding.t17.setOnClickListener(this);
        binding.t18.setOnClickListener(this);
        binding.t19.setOnClickListener(this);
        binding.t20.setOnClickListener(this);

        return root;
    }



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
                String insert_sql = "insert into ClassTable values (?,?)";
                db.execSQL(insert_sql,new String[]{String.valueOf(binding.t01.getId()),editText.getText().toString()});
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}