package com.tech_sim.mymasjidapp.activity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.DefaultRetryPolicy;
        import com.android.volley.NetworkResponse;
        import com.android.volley.VolleyError;
        import com.tech_sim.mymasjidapp.R;
        import com.tech_sim.mymasjidapp.utils.Constants;
        import com.tech_sim.mymasjidapp.utils.PreferenceManager;
        import com.tech_sim.mymasjidapp.utils.VolleyMultipartRequest;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;
        import java.util.Map;

public class VistorCharity extends AppCompatActivity {
    private EditText et_amount, et_exp, et_card, et_cvv, et_currency,et_moth,et_year;
    private Spinner spinner;
    private Button btn_cancel, btn_donate;
    private Constants constants;
    String masjidid="";
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
        setContentView(R.layout.activity_visitor_charity);
        constants=new Constants(this);
        masjidid=PreferenceManager.getMasjid();
        initxml();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = et_amount.getText().toString().trim();
                String cardnumber = et_card.getText().toString().trim();
                String month=et_moth.getText().toString().trim();
                String year=et_year.getText().toString().trim();
                String cvvcode = et_cvv.getText().toString().trim();
                String email=et_currency.getText().toString().trim();
                String usertype=spinner.getSelectedItem().toString();
//                String user_id = PreferenceManager.getUserid();
//                String masjid_id = PreferenceManager.getMasjid();
                if (amount.equals("") || cardnumber.equals("") || month.equals("") || cvvcode.equals("") || email.equals("")) {

                    Toast.makeText(VistorCharity.this,"Please enter the correct values.",Toast.LENGTH_SHORT).show();
                } else {
                    doDonate(amount, cardnumber, month,year, cvvcode, email,usertype);
                }
            }
        });
    }
    public void initxml() {
        spinner = findViewById(R.id.masjid_name);
        et_amount = (EditText) findViewById(R.id.amount);
        et_moth=(EditText)findViewById(R.id.month);
        et_year=(EditText)findViewById(R.id.year);
        et_card = (EditText) findViewById(R.id.card_number);
        et_cvv = (EditText) findViewById(R.id.cvvcode);
        et_currency = (EditText) findViewById(R.id.currener);
        btn_donate=findViewById(R.id.charityDonate);
        btn_cancel=findViewById(R.id.charityCancel);
    }

    public void doDonate(final String amount, final String cardnum, final String month, final String year, final String cvvcod, final String email, final String type)
    {
        String url= Constants.transaction_url;
        String tag="Setting";
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
                        Toast.makeText(VistorCharity.this, responseobject.getString("result"), Toast.LENGTH_SHORT).show();
                        finish();


                    }
                    else {
                        Toast.makeText(VistorCharity.this, responseobject.getString("result"), Toast.LENGTH_SHORT).show();
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
                params.put("card_num",cardnum);
                params.put("cvc",cvvcod);
                params.put("exp_year",year);
                params.put("exp_month",month);
                params.put("amount",amount);
                params.put("currency","usd");
                params.put("user_id",email);
                params.put("type",type);
                params.put("masjid",masjidid);
                return params;
            }

        };
        // add the request object to the queue to be executed
        PreferenceManager.getInstance().addToRequestQueue(req,tag);
        PreferenceManager.getInstance().addToRequestQueue(req,tag);
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


}
