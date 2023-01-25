package abc.project.projectcheckinapp.rawData;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import abc.project.projectcheckinapp.R;

public class AdapterAllStu extends RecyclerView.Adapter<AdapterAllStu.ViewHolder>{

    SQLiteDatabase db;
    ArrayList<StuModel> result;
    ClickListener click;
    int cid;

    public AdapterAllStu() {
    }

    public AdapterAllStu(SQLiteDatabase db, ClickListener click, int cid) {
        this.db = db;
        this.click = click;
        this.cid = cid;
        result = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+cid+"_allstu;",null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                StuModel stuModel = new StuModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2));
                result.add(stuModel);
            }while ( ( cursor.moveToNext()));
        }
        cursor.close();
    }

    public void selectAllColumn(String select){
        // 輸入查詢 姓名 或 科系 或 學號
        result.clear();
        Cursor cursor;
        if (select.equals("")){
            cursor = db.rawQuery("select * from "+cid+"_allstu;",null);
        } else {
            cursor = db.rawQuery("select * from "+cid+"_allstu where name=? or depart=? or stuId=?;",new String[]{select});
        }
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                StuModel stuModel = new StuModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );
                result.add(stuModel);
            }while (cursor.moveToNext());
            cursor.close();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.studentName.setText(result.get(position).getStuname());
        holder.department.setText(result.get(position).getStudepart());
        holder.studentName.setText(result.get(position).getStuid());
        holder.studentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = holder.getAdapterPosition();
                String name  = result.get(pos).getStuname();
                String depart = result.get(pos).getStudepart();
                String id = result.get(pos).getStuid();
                click.onCliskForAllStuList(pos,name,depart,id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView studentName, department, idNum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.txt_tec1_stuCard_name);
            department = itemView.findViewById(R.id.txt_tec1_stuCard_depart);
            idNum = itemView.findViewById(R.id.txt_tec1_stuCard_id);
        }
    }
}
