package com.tech_sim.mymasjidapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.ChildPresentModel;

import java.util.ArrayList;

public class ChildPresentAdapter extends RecyclerView.Adapter<ChildPresentAdapter.ViewHolder> {

    private ArrayList<ChildPresentModel> presentModels;
    private LayoutInflater mInflater;
    private Context context;

    public ChildPresentAdapter(Context context, ArrayList<ChildPresentModel> models)
    {
        this.context=context;
        this.presentModels=models;
        this.mInflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ChildPresentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_child_date_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildPresentAdapter.ViewHolder viewHolder, int i) {
        final ChildPresentModel childDateModel=presentModels.get(i);
        viewHolder.txt_detail.setText(childDateModel.getPresent());
        viewHolder.txt_date.setText(childDateModel.getDate());
    }

    @Override
    public int getItemCount() {
        return presentModels.size();
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
