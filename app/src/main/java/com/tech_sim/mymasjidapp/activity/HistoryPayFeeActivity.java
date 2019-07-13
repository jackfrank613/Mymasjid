package com.tech_sim.mymasjidapp.activity;

import android.app.DownloadManager;
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
import com.tech_sim.mymasjidapp.adapter.HistoryAdapter;
import com.tech_sim.mymasjidapp.model.History;
import com.tech_sim.mymasjidapp.model.Masjiidmodel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryPayFeeActivity extends AppCompatActivity {
    private Button btn_cancel;
    private RecyclerView recyclerView;
    private Constants constants;
    private ArrayList<History> histories;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history);
        constants=new Constants(this);
        btn_cancel=findViewById(R.id.btncancellChildList);
        recyclerView=findViewById(R.id.viewChildList);
        getData();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void getData()
    {

        String url="http://mymasjid.space/mobileApp/admin/history_children_pay";
        final String user_id=PreferenceManager.getUserid();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
               url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        histories=new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("error").equals("false"))
                            {
                                JSONArray array=jsonObject.getJSONArray("result");
                                for (int i=0;i<array.length();i++)
                                {
                                    JSONObject object=array.getJSONObject(i);
                                    String payment=object.getString("payment_method");
                                    String data=object.getString("created");
                                    String amount=object.getString("amount");
                                    String child_name=object.getString("child_name");
                                    History history=new History(payment,data,amount,child_name);
                                    histories.add(history);
                                }
                                HistoryAdapter adapter=new HistoryAdapter(HistoryPayFeeActivity.this,histories);
                                recyclerView.setLayoutManager(new LinearLayoutManager(HistoryPayFeeActivity.this));
                                recyclerView.setAdapter(adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",user_id);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }


}
