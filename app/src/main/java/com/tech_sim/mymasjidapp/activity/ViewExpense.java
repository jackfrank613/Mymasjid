package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.ViewExpenseAdapter;
import com.tech_sim.mymasjidapp.model.ExpenseModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewExpense extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Constants constants;
    private ArrayList<ExpenseModel> expenseModels;
    private Button btn_cancel,back_btn;
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list_expense_layout);
        constants=new Constants(this);
        btn_cancel=(Button)findViewById(R.id.btn_cancel) ;
        recyclerView=(RecyclerView)findViewById(R.id.viewChildList);
        getViewExpense();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    public void getViewExpense(){
     String url="http://mymasjid.space/mobileApp/admin/get_expense_incoming_list_masjid";
      final String mas_id=PreferenceManager.getMasjid();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        expenseModels=new ArrayList<>();
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("error").equals("false")) {
                                JSONArray array=obj.getJSONArray("result");
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject object=array.getJSONObject(i);
                                    String id=object.getString("id");
                                    String type=object.getString("type");
                                    String amount=object.getString("amount");
                                    String date=object.getString("date");
                                    String detail=object.getString("detail");
                                    ExpenseModel expenseModel=new ExpenseModel(id,type,amount,detail,date);
                                    expenseModels.add(expenseModel);

                                }
                                ViewExpenseAdapter adapter=new ViewExpenseAdapter(ViewExpense.this,expenseModels,constants);
                                recyclerView.setLayoutManager(new LinearLayoutManager(ViewExpense.this));
                                recyclerView.setAdapter(adapter);

                            } else {

                                Toast.makeText(getApplicationContext(),
                                        obj.getString("result"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("masjid",mas_id);
                return params;
            }
        };

        PreferenceManager.getInstance().addToRequestQueue(stringRequest);

    }
}
