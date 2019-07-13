package com.tech_sim.mymasjidapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.activity.ChildDescriptionUser;
import com.tech_sim.mymasjidapp.model.ViewChildItem;

import java.util.ArrayList;

public class ChildNameAdapter extends RecyclerView.Adapter<ChildNameAdapter.ViewHolder> {

    private ArrayList<ViewChildItem> childItems;
    private LayoutInflater mInflater;
    private Context context;

    public ChildNameAdapter(Context context, ArrayList<ViewChildItem> childItems)
    {
        this.context=context;
        this.childItems=childItems;
        this.mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ChildNameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_child_view, viewGroup, false);
        return new ChildNameAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final  ViewChildItem item=childItems.get(i);
        viewHolder.name.setText(item.getCh_name());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChildDescriptionUser.class);
                intent.putExtra("child_id",item.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return childItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.c_name);
        }

    }
}
