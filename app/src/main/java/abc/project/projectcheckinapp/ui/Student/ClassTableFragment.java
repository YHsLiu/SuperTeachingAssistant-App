package abc.project.projectcheckinapp.ui.Student;

import static android.content.Context.MODE_PRIVATE;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentClasstableBinding;

public class ClassTableFragment extends Fragment implements View.OnClickListener{

    SQLiteDatabase db;
    SharedPreferences preferences2;
    FragmentClasstableBinding binding;
    Cursor C, c;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClasstableBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //app共用(抓SID)
        preferences2 = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int sid = preferences2.getInt("sid",0);

        //開資料庫
        db = getActivity().openOrCreateDatabase("ClassTableDB",MODE_PRIVATE,null);
        String create_sql = "create table if not exists ClassTable (_id integer PRIMARY KEY AUTOINCREMENT,Num Integer, Name text, Sid Integer);";
        db.execSQL(create_sql);


        binding.t01.setOnClickListener(this);
        binding.t01.setText(getName (1));
        binding.t02.setOnClickListener(this);
        binding.t02.setText(getName (2));
        binding.t03.setOnClickListener(this);
        binding.t03.setText(getName (3));
        binding.t04.setOnClickListener(this);
        binding.t04.setText(getName (4));
        binding.t05.setOnClickListener(this);
        binding.t05.setText(getName (5));
        binding.t06.setOnClickListener(this);
        binding.t06.setText(getName (6));
        binding.t07.setOnClickListener(this);
        binding.t07.setText(getName (7));
        binding.t08.setOnClickListener(this);
        binding.t08.setText(getName (8));
        binding.t09.setOnClickListener(this);
        binding.t09.setText(getName (9));
        binding.t10.setOnClickListener(this);
        binding.t10.setText(getName (10));
        binding.t11.setOnClickListener(this);
        binding.t11.setText(getName (11));
        binding.t12.setOnClickListener(this);
        binding.t12.setText(getName (12));
        binding.t13.setOnClickListener(this);
        binding.t13.setText(getName (13));
        binding.t14.setOnClickListener(this);
        binding.t14.setText(getName (14));
        binding.t15.setOnClickListener(this);
        binding.t15.setText(getName (15));
        binding.t16.setOnClickListener(this);
        binding.t16.setText(getName (16));
        binding.t17.setOnClickListener(this);
        binding.t17.setText(getName (17));
        binding.t18.setOnClickListener(this);
        binding.t18.setText(getName (18));
        binding.t19.setOnClickListener(this);
        binding.t19.setText(getName (19));
        binding.t20.setOnClickListener(this);
        binding.t20.setText(getName (20));

        return root;
    }

    private String getName (int num){
        String Name = null;
        int sid = preferences2.getInt("sid",0);
        C = db.rawQuery("select * from ClassTable where Num=" +num+ " AND Sid="+sid,null);
        C.moveToFirst();
        if(C != null && C.isFirst()) {
            Name = C.getString(2);
            Log.w("cursor方法:",C.getString(2));
        }

        return Name;
    }



    @Override
    public void onClick(View v) {
        int sid = preferences2.getInt("sid",0);
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
                        binding.t01.setText(editText.getText().toString());
                        saveChange(1,sid,editText.getText().toString());
                        break;
                    case R.id.t02:
                        binding.t02.setText(editText.getText());
                        saveChange(2,sid,editText.getText().toString());
                        break;
                    case R.id.t03:
                        binding.t03.setText(editText.getText());
                        saveChange(3,sid,editText.getText().toString());
                        break;
                    case R.id.t04:
                        binding.t04.setText(editText.getText());
                        saveChange(4,sid,editText.getText().toString());
                        break;
                    case R.id.t05:
                        binding.t05.setText(editText.getText());
                        saveChange(5,sid,editText.getText().toString());
                        break;
                    case R.id.t06:
                        binding.t06.setText(editText.getText());
                        saveChange(6,sid,editText.getText().toString());
                        break;
                    case R.id.t07:
                        binding.t07.setText(editText.getText());
                        saveChange(7,sid,editText.getText().toString());
                        break;
                    case R.id.t08:
                        binding.t08.setText(editText.getText());
                        saveChange(8,sid,editText.getText().toString());
                        break;
                    case R.id.t09:
                        binding.t09.setText(editText.getText());
                        saveChange(9,sid,editText.getText().toString());
                        break;
                    case R.id.t10:
                        binding.t10.setText(editText.getText());
                        saveChange(10,sid,editText.getText().toString());
                        break;
                    case R.id.t11:
                        binding.t11.setText(editText.getText());
                        saveChange(11,sid,editText.getText().toString());
                        break;
                    case R.id.t12:
                        binding.t12.setText(editText.getText());
                        saveChange(12,sid,editText.getText().toString());
                        break;
                    case R.id.t13:
                        binding.t13.setText(editText.getText());
                        saveChange(13,sid,editText.getText().toString());
                        break;
                    case R.id.t14:
                        binding.t14.setText(editText.getText());
                        saveChange(14,sid,editText.getText().toString());
                        break;
                    case R.id.t15:
                        binding.t15.setText(editText.getText());
                        saveChange(15,sid,editText.getText().toString());
                        break;
                    case R.id.t16:
                        binding.t16.setText(editText.getText());
                        saveChange(16,sid,editText.getText().toString());
                        break;
                    case R.id.t17:
                        binding.t17.setText(editText.getText());
                        saveChange(17,sid,editText.getText().toString());
                        break;
                    case R.id.t18:
                        binding.t18.setText(editText.getText());
                        saveChange(18,sid,editText.getText().toString());
                        break;
                    case R.id.t19:
                        binding.t19.setText(editText.getText());
                        saveChange(19,sid,editText.getText().toString());
                        break;
                    case R.id.t20:
                        binding.t20.setText(editText.getText());
                        saveChange(20,sid,editText.getText().toString());
                        break;
                }
            }

        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void saveChange(int num, int sid, String newName){

        String insert_sql=null;
        c = db.rawQuery("select * from ClassTable where Num=" +num+ " AND Sid="+sid,null);
        c.moveToFirst();
        if(c.getCount() != 0 && c.isFirst()) {
            insert_sql ="UPDATE ClassTable SET " + "Name='" + newName+"' "+ "WHERE Num='" + num + "'"+"AND Sid='" + sid+"'";
            Log.e("更新資料庫:",insert_sql);
        }
        else {
            insert_sql = "INSERT INTO ClassTable (Num,Name,sid) values " + "('"+num+"','" +newName+"','"+sid+"')" ;
            Log.e("新寫進資料庫:",insert_sql);
        }

        db.execSQL(insert_sql);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        db.close();
    }








}