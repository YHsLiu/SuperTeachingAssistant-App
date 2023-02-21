package abc.project.projectcheckinapp.Other;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import abc.project.projectcheckinapp.R;

public class AdapterClassroom extends RecyclerView.Adapter<AdapterClassroom.ViewHolder>{

    SQLiteDatabase db;
    ArrayList<RoomModel> result;
    ClickListener click;

    public AdapterClassroom(SQLiteDatabase db, ClickListener click) {
        this.db = db;
        this.click = click;
        result = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from allroom;",null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                RoomModel roomModel = new RoomModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2));
                result.add(roomModel);
            }while ( ( cursor.moveToNext()));
        }
        cursor.close();
    }

    public void selectRoom(String select){
        // 輸入查詢 課名 或 代碼
        result.clear();
        Cursor cursor;
        if (select.equals("")){
            cursor = db.rawQuery("select * from allroom;",null);
        } else {
            cursor = db.rawQuery("select * from allroom where classname=? or classcode=?;",new String[]{select,select});
        }
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                RoomModel roomModel = new RoomModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2));
                result.add(roomModel);
            }while (cursor.moveToNext());
            cursor.close();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_list_row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.className.setText(result.get(position).getClassname());
        holder.classCode.setText(result.get(position).getClasscode());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = holder.getAdapterPosition();
                int cid = result.get(pos).getCid();
                String classname = result.get(pos).getClassname();
                click.onClickForClassroom(pos,cid,classname);
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView className, classCode;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.txt_tecS_RoomCard_name);
            classCode = itemView.findViewById(R.id.txt_tecS_RoomCard_code);
            cardView = itemView.findViewById(R.id.card_tecS);
        }
    }
}
