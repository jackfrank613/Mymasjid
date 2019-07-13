package com.tech_sim.mymasjidapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.activity.UpdateSalahTime;
import com.tech_sim.mymasjidapp.model.MasjidSalahTimeModel;
import com.tech_sim.mymasjidapp.model.SalahTimeModel;

import java.util.ArrayList;
import java.util.List;

public class SlahTimeAdapter extends RecyclerView.Adapter<SlahTimeAdapter.ViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;
    public ArrayList<MasjidSalahTimeModel> masjiidmodels;
    private SlahTimeAdapter.ItemClickListener mClickListener;
    private Context context;
    // data is passed into the constructor
    public SlahTimeAdapter(Context context, ArrayList<MasjidSalahTimeModel> masjiidmodels) {
        this.mInflater = LayoutInflater.from(context);
        this.masjiidmodels=masjiidmodels;
        this.context=context;
    }

    // inflates the row layout from xml when needed
    @Override
    public SlahTimeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_view_salah_time, parent, false);
        return new SlahTimeAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final SlahTimeAdapter.ViewHolder holder, int position) {
        final MasjidSalahTimeModel model=masjiidmodels.get(position);
        holder.novemver.setText(String.valueOf(position+1));
        holder.e_time.setText(model.getE_time());
        holder.s_time.setText(model.getS_time());
        holder.s_name.setText(model.getS_name());
        holder.j_time.setText(model.getJ_time());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UpdateSalahTime.class);
                intent.putExtra("salahname",model.getS_name());
                intent.putExtra("starttime",model.getS_time());
                intent.putExtra("jamahtime",model.getJ_time());
                intent.putExtra("endtime",model.getE_time());
                context.startActivity(intent);
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
        TextView novemver,s_name, s_time,j_time,e_time;

        ViewHolder(View itemView) {
            super(itemView);
            novemver = itemView.findViewById(R.id.number);
            s_name=itemView.findViewById(R.id.salah);
            s_time=itemView.findViewById(R.id.start);
            j_time=itemView.findViewById(R.id.jamah);
            e_time=itemView.findViewById(R.id.end);
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
    public  void setClickListener(SlahTimeAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onMasjidName(String name, String id);
    }


}
