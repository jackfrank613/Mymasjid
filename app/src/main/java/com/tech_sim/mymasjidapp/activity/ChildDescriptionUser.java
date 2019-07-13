package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.ChildAdatper;
import com.tech_sim.mymasjidapp.adapter.ChildPresentAdapter;
import com.tech_sim.mymasjidapp.model.ChildDateModel;
import com.tech_sim.mymasjidapp.model.ChildPresentModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChildDescriptionUser extends AppCompatActivity {

    TextView txt_name;
    RecyclerView repot_recylerview,attendance_recylerview;
    Constants constants;
    private String userid;
    private ArrayList<ChildDateModel> dateModels;
    private ArrayList<ChildPresentModel> presentModels;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_description);
        Intent intent=getIntent();
        userid=intent.getStringExtra("child_id");
        constants=new Constants(this);
        txt_name=(TextView)findViewById(R.id.name);
        repot_recylerview=(RecyclerView)findViewById(R.id.report);
        attendance_recylerview=(RecyclerView)findViewById(R.id.att_recyclerview);
        getRequest();
        btn_cancel=findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        }
        public void getRequest(){
        String url="http://mymasjid.space/mobileApp/admin/getchildren_user";
        final String user_id= PreferenceManager.getUserid();
        constants.kpHUD.show();
            RequestQueue r = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            constants.kpHUD.dismiss();
                            dateModels=new ArrayList<>();
                            presentModels=new ArrayList<>();
                            try {
                                JSONObject object = new JSONObject(response);
                                JSONArray array=object.getJSONArray("result");
                                for(int i=0;i<array.length();i++)
                                {
                                    JSONObject jsonObject=array.getJSONObject(i);
                                    String id=jsonObject.getString("id");
                                    if(id.equals(userid))
                                    {
                                        String name=jsonObject.getString("child_name");
                                        txt_name.setText(name);
                                        JSONArray array1=jsonObject.getJSONArray("report");
                                        for(int j=0;j<array1.length();j++)
                                        {
                                            JSONObject jj=array1.getJSONObject(j);
                                            String date=jj.getString("date");
                                            String detail=jj.getString("detail");
                                            ChildDateModel dateModel=new ChildDateModel(date,detail);
                                            dateModels.add(dateModel);
                                        }
                                        JSONArray array2=jsonObject.getJSONArray("attendance");
                                        for(int ii=0;ii<array2.length();ii++)
                                        {
                                            JSONObject jo=array2.getJSONObject(ii);
                                            String dater=jo.getString("date");
                                            String present=jo.getString("status");
                                            ChildPresentModel presentModel=new ChildPresentModel(dater,present);
                                            presentModels.add(presentModel);
                                        }

                                    }
                                }
                                ChildAdatper adatper=new ChildAdatper(ChildDescriptionUser.this,dateModels);
                                repot_recylerview.setLayoutManager(new LinearLayoutManager(ChildDescriptionUser.this));
                                repot_recylerview.setAdapter(adatper);
                                ChildPresentAdapter presentAdapter=new ChildPresentAdapter(ChildDescriptionUser.this,presentModels);
                                attendance_recylerview.setLayoutManager(new LinearLayoutManager(ChildDescriptionUser.this));
                                attendance_recylerview.setAdapter(presentAdapter);


                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
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
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    }
