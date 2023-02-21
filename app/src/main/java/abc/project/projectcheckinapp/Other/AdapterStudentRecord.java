package abc.project.projectcheckinapp.Other;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import abc.project.projectcheckinapp.R;

public class AdapterStudentRecord extends RecyclerView.Adapter<AdapterNoRollCallStudent.ViewHolder>{


    @NonNull
    @Override
    public AdapterNoRollCallStudent.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.std_rollcall_record_list_item,parent,false);
        return null;       //要改
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNoRollCallStudent.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
