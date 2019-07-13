package com.tech_sim.mymasjidapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.Masjiidmodel;
import com.tech_sim.mymasjidapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    public ArrayList<Masjiidmodel> masjiidmodels;
    private ItemClickListener mClickListener;
    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, ArrayList<Masjiidmodel> masjiidmodels) {
        this.mInflater = LayoutInflater.from(context);
        this.masjiidmodels=masjiidmodels;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.search_masji_itemview, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Masjiidmodel model=masjiidmodels.get(position);
        holder.novemver.setText(String.valueOf(position+1));
        holder.mas_address.setText(model.getAddress());
        holder.mas_name.setText(model.getName());
        final String id=model.getId();
        final String city=model.getCity();
        Constants.city=city;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onMasjidName(holder.mas_name.getText().toString(),id,city);
                }
            }
        });
    }
    // total number of rows
    @Override
    public int getItemCount() {
        return masjiidmodels.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView novemver,mas_name, mas_address;
        ViewHolder(View itemView) {
            super(itemView);
            novemver = itemView.findViewById(R.id.txt_num);
            mas_name = itemView.findViewById(R.id.txt_name);
            mas_address = itemView.findViewById(R.id.txt_address);
        }

        @Override
        public void onClick(View view) {
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public  void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onMasjidName(String name, String id, String city);
    }
}
