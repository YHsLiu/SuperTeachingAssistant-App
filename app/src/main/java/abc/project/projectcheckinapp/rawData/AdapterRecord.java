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

public class AdapterRecord extends RecyclerView.Adapter<AdapterRecord.ViewHolder>{

    SQLiteDatabase db;
    String dateR;
    int cid;
    ArrayList<RecordModel> result;

    public AdapterRecord(SQLiteDatabase db, int cid) {  // 學期紀錄
        this.db = db;
        this.cid = cid;
        result = new ArrayList<>();
        Cursor cursor = db.rawQuery("select distinct date from "+cid+"_record_semester;",null);
        Cursor cursor1;
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                dateR = cursor.getString(0);
                cursor1 = db.rawQuery("select stuId,name from "+cid+"_record_semester where date="+dateR+" order by stuId;", null);
                String stuInfo = "";
                cursor1.moveToFirst();
                do {
                    stuInfo += cursor1.getString(0) + "  " + cursor1.getString(1) + "\r\n";
                } while ((cursor1.moveToNext()));
                RecordModel recordModel = new RecordModel(dateR, stuInfo);
                result.add(recordModel);
            }while (cursor.moveToNext());
            cursor1.close();
        }
        cursor.close();
    }

    public AdapterRecord(SQLiteDatabase db, String dateR, int cid) {  // 今日紀錄
        this.db = db;
        this.dateR = dateR;
        this.cid = cid;
        result = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from "+cid+"_record_today order by stuId;", null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            String stuInfo = "";
            do {
                stuInfo += cursor.getString(0) + "  " + cursor.getString(1) + "\r\n";
            }while (cursor.moveToNext());
            RecordModel recordModel = new RecordModel(dateR, stuInfo);
            result.add(recordModel);
        }
        cursor.close();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(result.get(position).getDate());
        holder.record.setText(result.get(position).getStuInfo());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView date, record;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_tec4_recordCard_date);
            record = itemView.findViewById(R.id.txt_tec4_recordCard_record);
        }
    }
}
