package com.tech_sim.mymasjidapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.ChildDateModel;

import java.util.ArrayList;

public class ChildAdatper extends RecyclerView.Adapter<ChildAdatper.ViewHolder> {
    private ArrayList<ChildDateModel> dateModels;
    private LayoutInflater mInflater;
    private Context context;
    public ChildAdatper(Context context, ArrayList<ChildDateModel> dateModels)
    {
        this.context=context;
        this.dateModels=dateModels;
        this.mInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ChildAdatper.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_child_date_view, viewGroup, false);
        return new ChildAdatper.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ChildDateModel childDateModel=dateModels.get(i);
        viewHolder.txt_detail.setText(childDateModel.getDetail());
        viewHolder.txt_date.setText(childDateModel.getDate());

    }
    @Override
    public int getItemCount() {
        return dateModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_date,txt_detail;
        ViewHolder(View itemView) {
            super(itemView);
            txt_date=itemView.findViewById(R.id.date);
            txt_detail=itemView.findViewById(R.id.txt_detail);
        }

    }
}
