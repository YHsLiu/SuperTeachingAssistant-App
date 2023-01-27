package abc.project.projectcheckinapp.rawData;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import abc.project.projectcheckinapp.R;

public class AdapterNoRcStu extends RecyclerView.Adapter<AdapterNoRcStu.ViewHolder>{

    SQLiteDatabase db;
    ArrayList<StuModel> result;
    ClickListener click;
    int cid;

    public AdapterNoRcStu() {
    }

    public AdapterNoRcStu(SQLiteDatabase db, ClickListener click, int cid) {
        this.db = db;
        this.click = click;
        this.cid = cid;
        result = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+cid+"_no_rcstu;",null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                StuModel stuModel = new StuModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
                result.add(stuModel);
            }while ( ( cursor.moveToNext()));
        }
        cursor.close();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_rollcall_list_row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.studentName.setText(result.get(position).getStuname());
        holder.department.setText(result.get(position).getStudepart());
        holder.studentName.setText(result.get(position).getStuid());
        holder.RollCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = holder.getAdapterPosition();
                int sid = result.get(pos).getSid();
                click.onClickForNoRcStuList(pos,sid);
                holder.RollCall.setEnabled(false);
                holder.RollCall.setAlpha(0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
    TextView studentName, department, idNum;
    Button RollCall;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        studentName = itemView.findViewById(R.id.txt_tec2_NoCallCard_name);
        department = itemView.findViewById(R.id.txt_tec2_noCallCard_depart);
        idNum = itemView.findViewById(R.id.txt_tec2_noCallCard_id);
        RollCall = itemView.findViewById(R.id.btn_tec2_noCallCard);
    }
}
}
