package abc.project.projectcheckinapp.rawData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import abc.project.projectcheckinapp.R;

public class AdapterAllStu extends RecyclerView.Adapter<AdapterAllStu.ViewHolder>{

    JSONArray stuInfos;

    public AdapterAllStu() {
    }

    public AdapterAllStu(JSONArray stuInfos) {
        this.stuInfos = stuInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject info = stuInfos.getJSONObject(position);
            holder.studentName.setText(info.getString("姓名"));
            holder.department.setText(info.getString("科系"));
            holder.studentName.setText(info.getInt("學號"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return stuInfos.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView studentName, department, idNum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.txt_tecL1_stuCard_name);
            department = itemView.findViewById(R.id.txt_tecL1_stuCard_depart);
            idNum = itemView.findViewById(R.id.txt_tecL1_stuCard_id);
        }
    }
}
