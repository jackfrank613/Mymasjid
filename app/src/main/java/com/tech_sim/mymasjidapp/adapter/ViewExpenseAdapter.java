package com.tech_sim.mymasjidapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.internal.Experimental;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.activity.AddSalahActivity;
import com.tech_sim.mymasjidapp.activity.ExpenseHistoryActivity;
import com.tech_sim.mymasjidapp.activity.ViewExpense;
import com.tech_sim.mymasjidapp.activity.VisitorActivity;
import com.tech_sim.mymasjidapp.model.ExpenseModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.T;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewExpenseAdapter extends RecyclerView.Adapter<ViewExpenseAdapter.ViewHolder> {

    private ArrayList<ExpenseModel> expenseModels;
    private LayoutInflater layoutInflater;
    private Context context;
    private Constants constants;
    public ViewExpenseAdapter(Context context, ArrayList<ExpenseModel> expenseModels,Constants constants)
    {
        this.expenseModels=expenseModels;
        this.layoutInflater=LayoutInflater.from(context);
        this.context=context;
        this.constants=constants;
    }

    @NonNull
    @Override
    public ViewExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.item_view_expense,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewExpenseAdapter.ViewHolder viewHolder, final int i) {
        final ExpenseModel expenseModel=expenseModels.get(i);
        viewHolder.txt_type.setText(expenseModel.getType());
        viewHolder.txt_amount.setText(expenseModel.getAmount());
        viewHolder.txt_date.setText(expenseModel.getDate());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ExpenseHistoryActivity.class);
                intent.putExtra("name",expenseModel.getType());
                intent.putExtra("amount",expenseModel.getAmount());
                intent.putExtra("detail",expenseModel.getDetail());
                context.startActivity(intent);
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialog_paycash_layout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.show();
                Button btn_yes=(Button)dialog.findViewById(R.id.btn_yes);
                Button btn_no=(Button)dialog.findViewById(R.id.btn_no);

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int item=viewHolder.getAdapterPosition();
                        expenseModels.remove(item);
                        notifyItemRemoved(item);
                        notifyItemRangeChanged(item, expenseModels.size());
                        sendRemove(expenseModel.getId(),expenseModel.getType());
                         dialog.dismiss();
                    }
                });
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return expenseModels.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
       private TextView txt_type,txt_amount,txt_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_type=itemView.findViewById(R.id.type);
            txt_amount=itemView.findViewById(R.id.decription);
            txt_date=itemView.findViewById(R.id.date);

        }
    }
    public void sendRemove(final String id, final String type)
    {
        String url= Constants.delete_url;
//        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                             if(obj.getString("error").equals("false"))
                             {
                                 Toast.makeText(context,obj.getString("result"),Toast.LENGTH_SHORT).show();
                             }
                             else {
                                 Toast.makeText(context,obj.getString("result"),Toast.LENGTH_SHORT).show();

                             }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type",type);
                params.put("id",id);
                return params;
            }
        };

        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }
}
