package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.ViewChildrenAdapter;
import com.tech_sim.mymasjidapp.model.ChildItem;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewChildActivity extends AppCompatActivity {
    RecyclerView listViewChild;

    Button btncancelviewChildList;
    public static ChildItem childItem;

    ArrayList<ChildItem> childItemslist;
    ArrayAdapter<ChildItem> adapter;
    private Constants constants;
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
        setContentView(R.layout.view_child_activity);
        constants=new Constants(this);
        listViewChild = findViewById(R.id.viewChildList);
        btncancelviewChildList = findViewById(R.id.btncancellChildList);
        getData();
        btncancelviewChildList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                startActivity(new Intent(ViewChild.this, ManageChild.class));
            }
        });

    }
    void getData() {
        final String masjid_id= PreferenceManager.getMasjid();
        String url="http://mymasjid.space/mobileApp/admin/getChildrenMasjid";
        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        childItemslist = new ArrayList<>();
                        try {
                            // Log.e("Response : ", response);

                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("error").equals("false")) {
                                JSONArray reader = jsonObject.getJSONArray("data");
                                //Log.e("Response ", response)
                                for (int i = 0; i < reader.length(); i++) {
                                    JSONObject obj = reader.getJSONObject(i);
                                    //Log.e("Response ", String.valueOf(obj));
                                    String ChildName = obj.getString("child_name");
                                    String FatherName = obj.getString("father_name");
                                    String Age = obj.getString("age");
                                    String pin_number=obj.getString("childNumber");
                                    String ContectNo = obj.getString("contact_no");
                                    String ClassDays = obj.getString("class_days");
                                    String ChildMasjid = obj.getString("masjid");
                                    String ChildID = obj.getString("id");
                                    String DateC = obj.getString("date");
                                    childItemslist.add(new ChildItem(ChildName, FatherName, Age, ContectNo, ClassDays, ChildMasjid, ChildID, DateC,pin_number));
                                }
                                ViewChildrenAdapter viewChildrenAdapter=new ViewChildrenAdapter(ViewChildActivity.this,childItemslist);
                                listViewChild.setLayoutManager(new LinearLayoutManager(ViewChildActivity.this));
                                listViewChild.setAdapter(viewChildrenAdapter);
                            }
                            else {

                                Toast.makeText(ViewChildActivity.this,"Please,there is no registered children",Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
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
                params.put("masjid",masjid_id);
                return params;
            }
        };
        r.add(stringRequest);
    }
}
