package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.ChildPayAdapter;
import com.tech_sim.mymasjidapp.model.UserPayModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChildPayfeeActivity extends AppCompatActivity {
        private RecyclerView recyclerView;
        private Constants constants;
        private ArrayList<UserPayModel> payModels;
        private Button btn_cancel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.children_payfee_layout);
        constants=new Constants(this);
        recyclerView=(RecyclerView)findViewById(R.id.viewChildList);
        btn_cancel=(Button)findViewById(R.id.btncancellChildList);
        getChildren();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void getChildren()
    {
        String url= Constants.User_get_payfee_url;
        final String userid= PreferenceManager.getUserid();
        String tag="PAY";
        constants.kpHUD.show();
        VolleyMultipartRequest req = new VolleyMultipartRequest(com.android.volley.Request.Method.POST,
                url, new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                constants.kpHUD.dismiss();
                payModels=new ArrayList<>();
                String responseStr=new String(response.data);
                try {
                    JSONObject responseobject=new JSONObject(responseStr);
                    if (responseobject.getString("error").equals("false")) {
                        JSONArray array=responseobject.getJSONArray("result");
                        for(int i=0;i<array.length();i++)
                        {
                            JSONObject object=array.getJSONObject(i);
                            String childname=object.getString("child_name");
                            String id=object.getString("id");
                            UserPayModel userPayModel=new UserPayModel(id,childname);
                            payModels.add(userPayModel);

                        }
                        ChildPayAdapter childPayAdapter=new ChildPayAdapter(ChildPayfeeActivity.this,payModels);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ChildPayfeeActivity.this));
                        recyclerView.setAdapter(childPayAdapter);


                    }
                    else {
                        Toast.makeText(ChildPayfeeActivity.this, responseobject.getString("result"), Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException ignored){}
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Log.d("Error.Response","that is not");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                HashMap<String,String> params=new HashMap<String,String>();
                params.put("user_id",userid);
                return params;
            }

        };
        // add the request object to the queue to be executed
        PreferenceManager.getInstance().addToRequestQueue(req,tag);


    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
