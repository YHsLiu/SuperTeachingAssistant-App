package abc.project.projectcheckinapp.ui.Student;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

    //SQLiteDatabase db;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FragmentClasstableBinding binding;
    String name;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClasstableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        preferences = getActivity().getPreferences(MODE_PRIVATE);
        editor = preferences.edit();

        //開資料庫
        //db = getActivity().openOrCreateDatabase("ClassTableDB",MODE_PRIVATE,null);
        //String drop_sql = "drop table if exists ClassTable;";
        //String create_sql = "create table if not exists ClassTable (_id text,Form_Name text);";
        //db.execSQL(drop_sql);
        //db.execSQL(create_sql);
        binding.t01.setOnClickListener(this);
        binding.t01.setText(preferences.getString("t01",""));
        binding.t02.setOnClickListener(this);
        binding.t02.setText(preferences.getString("t02",""));
        binding.t03.setOnClickListener(this);
        binding.t03.setText(preferences.getString("t03",""));
        binding.t04.setOnClickListener(this);
        binding.t04.setText(preferences.getString("t04",""));
        binding.t05.setOnClickListener(this);
        binding.t05.setText(preferences.getString("t05",""));
        binding.t06.setOnClickListener(this);
        binding.t06.setText(preferences.getString("t06",""));
        binding.t07.setOnClickListener(this);
        binding.t07.setText(preferences.getString("t07",""));
        binding.t08.setOnClickListener(this);
        binding.t08.setText(preferences.getString("t08",""));
        binding.t09.setOnClickListener(this);
        binding.t09.setText(preferences.getString("t09",""));
        binding.t10.setOnClickListener(this);
        binding.t10.setText(preferences.getString("t10",""));
        binding.t11.setOnClickListener(this);
        binding.t11.setText(preferences.getString("t11",""));
        binding.t12.setOnClickListener(this);
        binding.t12.setText(preferences.getString("t12",""));
        binding.t13.setOnClickListener(this);
        binding.t13.setText(preferences.getString("t13",""));
        binding.t14.setOnClickListener(this);
        binding.t14.setText(preferences.getString("t14",""));
        binding.t15.setOnClickListener(this);
        binding.t15.setText(preferences.getString("t15",""));
        binding.t16.setOnClickListener(this);
        binding.t16.setText(preferences.getString("t16",""));
        binding.t17.setOnClickListener(this);
        binding.t17.setText(preferences.getString("t17",""));
        binding.t18.setOnClickListener(this);
        binding.t18.setText(preferences.getString("t18",""));
        binding.t19.setOnClickListener(this);
        binding.t19.setText(preferences.getString("t19",""));
        binding.t20.setOnClickListener(this);
        binding.t20.setText(preferences.getString("t20",""));

        /*Cursor cursor = db.rawQuery("select * from ClassTable",null);
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


        }*/

        return root;
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        final EditText editText = new EditText(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("修改課程");
        builder.setView(editText);
        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //改變課名
                switch (id) {
                    case R.id.t01:
                        binding.t01.setText(editText.getText());
                        editor.putString("t01",editText.getText().toString()).apply();
                        break;
                    case R.id.t02:
                        binding.t02.setText(editText.getText());
                        editor.putString("t02",editText.getText().toString()).apply();
                        break;
                    case R.id.t03:
                        binding.t03.setText(editText.getText());
                        editor.putString("t03",editText.getText().toString()).apply();
                        break;
                    case R.id.t04:
                        binding.t04.setText(editText.getText());
                        editor.putString("t04",editText.getText().toString()).apply();
                        break;
                    case R.id.t05:
                        binding.t05.setText(editText.getText());
                        editor.putString("t05",editText.getText().toString()).apply();
                        break;
                    case R.id.t06:
                        binding.t06.setText(editText.getText());
                        editor.putString("t06",editText.getText().toString()).apply();
                        break;
                    case R.id.t07:
                        binding.t07.setText(editText.getText());
                        editor.putString("t07",editText.getText().toString()).apply();
                        break;
                    case R.id.t08:
                        binding.t08.setText(editText.getText());
                        editor.putString("t08",editText.getText().toString()).apply();
                        break;
                    case R.id.t09:
                        binding.t09.setText(editText.getText());
                        editor.putString("t09",editText.getText().toString()).apply();
                        break;
                    case R.id.t10:
                        binding.t10.setText(editText.getText());
                        editor.putString("t10",editText.getText().toString()).apply();
                        break;
                    case R.id.t11:
                        binding.t11.setText(editText.getText());
                        editor.putString("t11",editText.getText().toString()).apply();
                        break;
                    case R.id.t12:
                        binding.t12.setText(editText.getText());
                        editor.putString("t12",editText.getText().toString()).apply();
                        break;
                    case R.id.t13:
                        binding.t13.setText(editText.getText());
                        editor.putString("t13",editText.getText().toString()).apply();
                        break;
                    case R.id.t14:
                        binding.t14.setText(editText.getText());
                        editor.putString("t14",editText.getText().toString()).apply();
                        break;
                    case R.id.t15:
                        binding.t15.setText(editText.getText());
                        editor.putString("t15",editText.getText().toString()).apply();
                        break;
                    case R.id.t16:
                        binding.t16.setText(editText.getText());
                        editor.putString("t16",editText.getText().toString()).apply();
                        break;
                    case R.id.t17:
                        binding.t17.setText(editText.getText());
                        editor.putString("t17",editText.getText().toString()).apply();
                        break;
                    case R.id.t18:
                        binding.t18.setText(editText.getText());
                        editor.putString("t18",editText.getText().toString()).apply();
                        break;
                    case R.id.t19:
                        binding.t19.setText(editText.getText());
                        editor.putString("t19",editText.getText().toString()).apply();
                        break;
                    case R.id.t20:
                        binding.t20.setText(editText.getText());
                        editor.putString("t20",editText.getText().toString()).apply();
                        break;

                }
                //String insert_sql = "insert into ClassTable values (?,?)";
                //db.execSQL(insert_sql,new String[]{String.valueOf(v.getId()),editText.getText().toString()});
            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        //db.close();
    }








}