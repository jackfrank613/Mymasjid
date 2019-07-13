package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.UserPayModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PayForChildActivity extends AppCompatActivity {
    private EditText et_amount,et_card,et_cvc,et_month,et_year;
    private String childrenid="";
    private Button btn_cancel,btn_pay;
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
        setContentView(R.layout.activity_child_for_pay);
        constants=new Constants(this);
        Intent intent=getIntent();
        childrenid=intent.getStringExtra("childid");
        initXml();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount=et_amount.getText().toString().trim();
                String card=et_card.getText().toString().trim();
                String cvccode=et_cvc.getText().toString().trim();
                String month=et_month.getText().toString().trim();
                String year=et_year.getText().toString().trim();
                if(amount.equals("")||card.equals("")||cvccode.equals("")||month.equals("")||year.equals(""))
                {
                    Toast.makeText(PayForChildActivity.this,"Please enter the correct values.",Toast.LENGTH_SHORT).show();
                }
                else {
                    payFunction(childrenid,amount,card,cvccode,month,year);
                }
            }
        });


    }
    public void initXml()
    {
        et_amount=(EditText)findViewById(R.id.amount);
        et_card=(EditText)findViewById(R.id.cardnmuber);
        et_cvc=(EditText)findViewById(R.id.cvvcode);
        et_month=(EditText)findViewById(R.id.month);
        et_year=(EditText)findViewById(R.id.year);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_pay=(Button)findViewById(R.id.btn_pay);
    }
    public void payFunction(final String childrenid, final String amount, final String card, final String cvcard, final String month,final String year)
    {
        String url=Constants.Transaction_Children;
        final String userid=PreferenceManager.getUserid();
        final String masjid=PreferenceManager.getMasjid();
        String tag="PAY";
        constants.kpHUD.show();
        VolleyMultipartRequest req = new VolleyMultipartRequest(com.android.volley.Request.Method.POST,
                url, new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                constants.kpHUD.dismiss();
                String responseStr=new String(response.data);
                try {
                    JSONObject responseobject=new JSONObject(responseStr);
                    if (responseobject.getString("error").equals("false")) {
                        Toast.makeText(PayForChildActivity.this, responseobject.getString("result"), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                    else {
                        Toast.makeText(PayForChildActivity.this, responseobject.getString("result"), Toast.LENGTH_SHORT).show();
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
                params.put("card_num",card);
                params.put("cvc",cvcard);
                params.put("exp_year",year);
                params.put("exp_month",month);
                params.put("amount",amount);
                params.put("masjid",masjid);
                params.put("currency","usd");
                params.put("children_id",childrenid);
                params.put("user_id",userid);
                return params;
            }

        };
        // add the request object to the queue to be executed
        PreferenceManager.getInstance().addToRequestQueue(req,tag);
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



    }
}
