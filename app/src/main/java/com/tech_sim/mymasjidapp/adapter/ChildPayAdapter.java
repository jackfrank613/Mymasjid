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
import com.tech_sim.mymasjidapp.activity.PayByCash;
import com.tech_sim.mymasjidapp.activity.PayForChildActivity;
import com.tech_sim.mymasjidapp.model.UserPayModel;
import com.tech_sim.mymasjidapp.utils.Constants;

import java.util.ArrayList;

public class ChildPayAdapter extends RecyclerView.Adapter<ChildPayAdapter.ViewHolder> {

    private ArrayList<UserPayModel> userPayModels;
    private LayoutInflater layoutInflater;
    private Context context;
    public ChildPayAdapter(Context context,ArrayList<UserPayModel> userPayModels)
    {
        this.context=context;
        this.userPayModels=userPayModels;
        this.layoutInflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ChildPayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.item_child_pay,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildPayAdapter.ViewHolder viewHolder, int i) {
          final UserPayModel model=userPayModels.get(i);
          viewHolder.txt_name.setText(model.getChildname());
          final String childid=model.getChildid();
          viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(Constants.pay_check) {
                      Intent intent = new Intent(context, PayForChildActivity.class);
                      intent.putExtra("childid", childid);
                      context.startActivity(intent);
                  }
                  else
                  {
                      Intent intent = new Intent(context, PayByCash.class);
                      intent.putExtra("childid", childid);
                      context.startActivity(intent);
                  }
              }
          });

    }

    @Override
    public int getItemCount() {
        return userPayModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView txt_name,txt_age,txt_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             txt_name=itemView.findViewById(R.id.type);
             txt_age=itemView.findViewById(R.id.decription);
             txt_date=itemView.findViewById(R.id.date);
        }
    }
}
