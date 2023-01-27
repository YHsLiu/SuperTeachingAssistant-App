package abc.project.projectcheckinapp.rawData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import abc.project.projectcheckinapp.R;

public class AdapterStdRecord extends RecyclerView.Adapter<AdapterNoRcStu.ViewHolder>{


    @NonNull
    @Override
    public AdapterNoRcStu.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.std_rollcall_record_list_item,parent,false);
        return null;       //要改
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNoRcStu.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
