package models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placement.R;

import java.util.ArrayList;

public class applicant_adapter extends RecyclerView.Adapter<applicant_adapter.MyViewHolder> {

    private Context context;
    private ArrayList<application> application;

    public applicant_adapter(Context c, ArrayList<application> i)
    {
        context = c;
        application = i;
    }

    @NonNull
    @Override
    public applicant_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.appl_card_comp,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull applicant_adapter.MyViewHolder holder, int position) {
        holder.s_name.setText(application.get(position).getStudentname());
        holder.s_email.setText(application.get(position).getStudentemail());
        holder.off_pos.setText(application.get(position).getAppliedposition());
        holder.off_type.setText(application.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return application.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView s_name,s_email,off_type,off_pos;

        MyViewHolder(View itemView) {
            super(itemView);
            s_name = itemView.findViewById(R.id.stu_name);
            s_email = itemView.findViewById(R.id.stu_email);
            off_type = itemView.findViewById(R.id.off_type);
            off_pos = itemView.findViewById(R.id.off_position);
        }
    }
}
