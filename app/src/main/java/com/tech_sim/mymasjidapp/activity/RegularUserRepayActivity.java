package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegularUserRepayActivity  extends AppCompatActivity {
    RadioButton prmtRadioOne, prmtRadioSix, prmtRadioOneYear;
    RadioGroup prmtRadioGroup;
    TextView prmtPrice;
    String email="";
    private EditText et_amount,et_card,et_cvc,et_month,et_year;
    private Button btn_cancel,btn_pay;
    private Constants constants;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reapy_regular_layout);
        constants=new Constants(this);
        Intent intent=getIntent();
        email=intent.getStringExtra("email");
        initXml();

        View.OnClickListener optionClickListiner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prmtRadioOne.isChecked()) {
                    prmtPrice.setText("£ 1");
                } else if (prmtRadioSix.isChecked()) {
                    prmtPrice.setText("£ 6");
                } else if (prmtRadioOneYear.isChecked()) {
                    prmtPrice.setText("£ 12");
                } else {
                    prmtPrice.setVisibility(View.GONE);
                }
            }
        };
        prmtRadioOne.setOnClickListener(optionClickListiner);
        prmtRadioSix.setOnClickListener(optionClickListiner);
        prmtRadioOneYear.setOnClickListener(optionClickListiner);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.check_pay=1;
                finish();
            }
        });
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount=prmtPrice.getText().toString();
                String[] array=amount.split(" ");
                String amount1=array[1];
                String card=et_card.getText().toString().trim();
                String cvccode=et_cvc.getText().toString().trim();
                String month=et_month.getText().toString().trim();
                String year=et_year.getText().toString().trim();
                if(amount.equals("")||card.equals("")||cvccode.equals("")||month.equals("")||year.equals(""))
                {
                    Toast.makeText(RegularUserRepayActivity.this,"Please enter the correct values.",Toast.LENGTH_SHORT).show();
                }
                else {
                    payFunction(email,amount1,card,cvccode,month,year);
                }
            }
        });


    }
    public void initXml()
    {
        prmtRadioGroup = findViewById(R.id.PmRadioButtonGroup);
        prmtRadioOne = findViewById(R.id.PmRadioOneM);
        prmtRadioSix = findViewById(R.id.PmRadioSixM);
        prmtRadioOneYear = findViewById(R.id.PmRadioOneY);
        prmtPrice = findViewById(R.id.Pmprice);
        et_card=(EditText)findViewById(R.id.cardnmuber);
        et_cvc=(EditText)findViewById(R.id.cvvcode);
        et_month=(EditText)findViewById(R.id.month);
        et_year=(EditText)findViewById(R.id.year);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        btn_pay=(Button)findViewById(R.id.btn_pay);
    }
    public void payFunction(final String email, final String amount, final String card, final String cvcard, final String month,final String year)
    {
        String url=Constants.User_pay_url;
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
                        Toast.makeText(RegularUserRepayActivity.this, responseobject.getString("result"), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegularUserRepayActivity.this,UserDashboard.class);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        Toast.makeText(RegularUserRepayActivity.this, responseobject.getString("result"), Toast.LENGTH_SHORT).show();
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
                params.put("email",email);
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
