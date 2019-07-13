package com.tech_sim.mymasjidapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.History;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ArrayList<History> histories;
    private LayoutInflater layoutInflater;
    private Context context;
    public HistoryAdapter(Context context,ArrayList<History> histories)
    {
        this.histories=histories;
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.history_children_layout,viewGroup,false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder viewHolder, int i) {
       final History history=histories.get(i);
       viewHolder.txt_payment.setText(history.getPayment());
       viewHolder.txt_name.setText(history.getChild_name());
       viewHolder.txt_amount.setText(history.getAmount());
       viewHolder.txt_date.setText(history.getCreate());
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txt_name,txt_amount,txt_payment,txt_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_amount=itemView.findViewById(R.id.amount);
            txt_name=itemView.findViewById(R.id.type);
            txt_payment=itemView.findViewById(R.id.payment);
            txt_date=itemView.findViewById(R.id.date);

        }
    }
}
