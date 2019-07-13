package com.tech_sim.mymasjidapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.AttendanceDetailModel;

import java.util.ArrayList;

public class AttendanceDetailAdapter extends RecyclerView.Adapter<AttendanceDetailAdapter.ViewHolder> {

    private ArrayList<AttendanceDetailModel> viewAudioModels;
    private LayoutInflater mInflater;
    private Context context;
    public AttendanceDetailAdapter(Context context, ArrayList<AttendanceDetailModel> audioModels) {
        this.mInflater = LayoutInflater.from(context);
        this.viewAudioModels=audioModels;
        this.context=context;
    }
    @NonNull
    @Override
    public AttendanceDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_attendance_detail, viewGroup, false);
        return new AttendanceDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AttendanceDetailAdapter.ViewHolder holder, final int position) {
        final AttendanceDetailModel detailModel=viewAudioModels.get(position);
        if(detailModel.getStatus().equals("Absent"))
        {
            holder.status.setTextColor(Color.RED);
        }
        else {
            holder.status.setTextColor(Color.WHITE);

        }
        holder.status.setText(detailModel.getStatus());
        holder.date.setText(detailModel.getDate());

    }
    @Override
    public int getItemCount() {
        return viewAudioModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView date,status;
        ViewHolder(View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.dater);
            status=itemView.findViewById(R.id.statuses);

        }

    }

}
