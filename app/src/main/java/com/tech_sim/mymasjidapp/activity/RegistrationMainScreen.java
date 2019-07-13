package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationMainScreen extends AppCompatActivity implements View.OnClickListener{

    Button btnregMasijid, btnregUser, loginbtn, visitor;
    TextView textView;
    AdView adView;
    private String ad_link;
    private String ad_image;
    private Constants constants;
    private ImageView addimage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_main_screen);
        initXml();
        getAdvertise();

    }
    public void initXml(){
        constants=new Constants(this);
        addimage=(ImageView)findViewById(R.id.addbanner);
        btnregMasijid = findViewById(R.id.btnRegisterMasjiid);
        btnregUser = findViewById(R.id.btnRegisterUser);
        loginbtn = findViewById(R.id.btnLogin);
        visitor = findViewById(R.id.btnVisitor);
        textView = findViewById(R.id.alreadyhaveAccount);
        textView.setText(Html.fromHtml(getString(R.string.alreadyaccount)));
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        adView = findViewById(R.id.testAd);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        btnregMasijid.setOnClickListener(this);
        btnregUser.setOnClickListener(this);
        loginbtn.setOnClickListener(this);
        visitor.setOnClickListener(this);
    }
    public void getAdvertise(){
        String url="http://mymasjid.space/mobileApp/admin/banner";
        constants.kpHUD.show();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST,url,new JSONObject(),
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        constants.kpHUD.dismiss();
                        try {
                            if (response.getString("error").equals("false"))
                            {
                                JSONObject object=response.getJSONObject("result");
                                ad_link=object.getString("ad_link");
                                ad_image=object.getString("ad_image");
                                Glide.with(RegistrationMainScreen.this).load(ad_image).into(addimage);
                                moveTowebView();
                            }
                            else {
                                Toast.makeText(RegistrationMainScreen.this,response.getString("result"),Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        constants.kpHUD.dismiss();
                        Log.d("Error.Response","that is not");
                    }
                }
        );
        PreferenceManager.getInstance().addToRequestQueue(getRequest);

    }
    public void moveTowebView(){
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Registration.this,"move to webview",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(ad_link));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnVisitor:
                    Intent intent = new Intent(getApplicationContext(), Visitors.class);
                    startActivity(intent);
                break;
            case R.id.btnRegisterMasjiid:
                Intent intent1 = new Intent(RegistrationMainScreen.this, MasjidRegistration.class);
                startActivity(intent1);
                break;

            case R.id.btnRegisterUser:
                Intent intent2 = new Intent(RegistrationMainScreen.this, HepUs.class);
                startActivity(intent2);
                break;
            case R.id.btnLogin:
                Intent intent3 = new Intent(RegistrationMainScreen.this, SelectLogin.class);
                startActivity(intent3);
                break;
        }
    }
 /*   @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }*/
}
