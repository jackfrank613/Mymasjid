package com.tech_sim.mymasjidapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.MarkChildModel;

import java.util.ArrayList;

public class MarkChildAdatper extends RecyclerView.Adapter<MarkChildAdatper.ViewHolder> {
    private ArrayList<MarkChildModel> viewAudioModels;
    private LayoutInflater mInflater;
    private Context context;
    private String value="";
    private Attendace attendace;

    public MarkChildAdatper(Context context, ArrayList<MarkChildModel> audioModels) {
        this.mInflater = LayoutInflater.from(context);
        this.viewAudioModels = audioModels;
        this.context = context;
    }

    public interface Attendace{
        void checkAttendance(String child_id);
    }

    public void setAttendace(Attendace attendace)
    {
        this.attendace=attendace;
    }
    @NonNull
    @Override
    public MarkChildAdatper.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.mark_item_child, viewGroup, false);
        return new MarkChildAdatper.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MarkChildAdatper.ViewHolder holder, final int position){
        final MarkChildModel markChildModel=viewAudioModels.get(position);
        holder.child.setText(markChildModel.getChild_name());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value+=markChildModel.getChild_id()+",";
                attendace.checkAttendance(value);
            }
        });

    }
    @Override
    public int getItemCount() {
        return viewAudioModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView child;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            child=itemView.findViewById(R.id.name);
            checkBox=itemView.findViewById(R.id.check);
        }

    }



    }
