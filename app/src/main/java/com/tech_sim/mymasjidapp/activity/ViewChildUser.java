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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.ChildNameAdapter;
import com.tech_sim.mymasjidapp.model.ViewChildItem;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewChildUser extends AppCompatActivity {
    Button btncancelviewChildListUser;
    RecyclerView listViewChildUser;
    ArrayList<ViewChildItem> childItemslistUser;
    Constants constants;
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
        setContentView(R.layout.activity_view_child);
        constants=new Constants(this);
        listViewChildUser = findViewById(R.id.viewChildListUser);
        getData();
        btncancelviewChildListUser = findViewById(R.id.btncancellChildListUser);
        btncancelviewChildListUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                startActivity(new Intent(ViewChild.this, ManageChild.class));
            }
        });

    }
    void getData() {
        String url="http://mymasjid.space/mobileApp/admin/getchildren_user";
        final String user_id= PreferenceManager.getUserid();
        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        childItemslistUser = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                JSONArray reader = jsonObject.getJSONArray("result");

                                for (int i = 0; i < reader.length(); i++) {
                                    JSONObject obj = reader.getJSONObject(i);
                                    String ChildName = obj.getString("child_name");
                                    String ChildID = obj.getString("id");
                                    ViewChildItem viewChildItem = new ViewChildItem(ChildID, ChildName);
                                    childItemslistUser.add(viewChildItem);
                                }
                                ChildNameAdapter adapter = new ChildNameAdapter(ViewChildUser.this, childItemslistUser);
                                listViewChildUser.setLayoutManager(new LinearLayoutManager(ViewChildUser.this));
                                listViewChildUser.setAdapter(adapter);
                            }
                            else {
                                Toast.makeText(ViewChildUser.this,"There is not registered child",Toast.LENGTH_SHORT).show();
                            }


                            } catch(Exception e){
                                // dialog.dismiss();
                            }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                //dialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",user_id);
                return params;
            }
        };
        r.add(stringRequest);
    }
}
