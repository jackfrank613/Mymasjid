package com.tech_sim.mymasjidapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Masjid extends AppCompatActivity implements View.OnClickListener{
    CardView crdManageSalah, crdMAnageAnnouncement, crdAnswerQuestion, crdManageChild, crdManageComunity;
    CardView buttonlogout, crdsetting, crdAttendanceHistory, crdExpense, crdHelp, crdgolive;

    AdView adView;
    boolean doubleBackToExitPressedOnce = false;
    TextView MasjidNameDashBoard;
    ImageView advertise_image;
    String ad_link,ad_image;
    Constants constants;
    private LinearLayout layout_question;
    private TextView txt_question;
    private String type;
    private int not_count=0;
    private int result_code=100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (data.getBooleanExtra("verification", false)) {
                    if (PreferenceManager.getCont3() <= 0) {
                        layout_question.setVisibility(View.INVISIBLE);
                        PreferenceManager.count_question=0;
                        PreferenceManager.setCount3(0);
                        layout_question.setVisibility(View.INVISIBLE);
                    } else {
                        layout_question.setVisibility(View.VISIBLE);
                        txt_question.setText(String.valueOf(PreferenceManager.getCont3()));

                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masjid);
        constants=new Constants(this);
        IntentFilter intentFilter = new IntentFilter("com.teachnologybeach.masjjidapp.message");
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, intentFilter);
        SharedPreferences prefs;
        prefs = getSharedPreferences("MyPref", MODE_PRIVATE);

        final String Masjid;
        Masjid = prefs.getString("MasjidID", null);
        getAdvertise();
        final String MasjidName=prefs.getString("MasjidName",null);
        advertise_image=(ImageView)findViewById(R.id.img);
        crdManageSalah = findViewById(R.id.ManageSalah);
        crdMAnageAnnouncement = findViewById(R.id.ManageAnnouncement);
        crdAnswerQuestion = findViewById(R.id.AnswerQuestion);
        crdManageChild = findViewById(R.id.ManageChild);
        crdManageComunity = findViewById(R.id.ManageCammetti);
        buttonlogout = findViewById(R.id.btnMasjidlogout);
        crdExpense = findViewById(R.id.Expense);
        layout_question=findViewById(R.id.question_layout);
        txt_question=findViewById(R.id.question_noti);
        crdgolive = findViewById(R.id.GoLive);
        crdHelp = findViewById(R.id.Help);
        crdsetting = findViewById(R.id.seeting);
        crdAttendanceHistory = findViewById(R.id.Attendancehistory);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        adView = findViewById(R.id.testAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        crdManageSalah.setOnClickListener(this);
        crdMAnageAnnouncement.setOnClickListener(this);
        crdAnswerQuestion.setOnClickListener(this);
        crdManageChild.setOnClickListener(this);
        crdManageComunity.setOnClickListener(this);
        crdExpense.setOnClickListener(this);
        crdgolive.setOnClickListener(this);
        crdHelp.setOnClickListener(this);
        crdsetting.setOnClickListener(this);
        crdAttendanceHistory.setOnClickListener(this);
        buttonlogout.setOnClickListener(this);
        if(PreferenceManager.getCont3()<=0) {
            layout_question.setVisibility(View.INVISIBLE);
            PreferenceManager.count_question=0;
            PreferenceManager.setCount3(0);
        }
        else {
            layout_question.setVisibility(View.VISIBLE);
            txt_question.setText(String.valueOf(PreferenceManager.getCont3()));
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.ManageSalah:

                Intent intent = new Intent(Masjid.this, SalahMasjidPanel.class);
                startActivity(intent);
                break;

            case R.id.ManageAnnouncement:
                Intent intent1 = new Intent(Masjid.this, MasjidAnnouncementActivity.class);
                startActivity(intent1);

                break;

            case R.id.AnswerQuestion:
                Intent intent2 = new Intent(Masjid.this, ManageAnsQuestionPanel.class);
                intent2.putExtra("face_verification",false);
                startActivityForResult(intent2,result_code);
                break;

            case R.id.ManageChild:

                Intent intent3 = new Intent(Masjid.this, ManageChildren.class);
                startActivity(intent3);

                break;

            case R.id.ManageCammetti:

                Intent intent4 = new Intent(Masjid.this, MajidManageActivity.class);
                startActivity(intent4);
                break;
            case R.id.GoLive:
                Intent intent8 = new Intent(Masjid.this,ActivityGolive.class);
                startActivity(intent8);
                break;
            case R.id.Expense:
                Intent intent9= new Intent(Masjid.this, ManageExpenseActivity.class);
                startActivity(intent9);

                break;
            case R.id.Help:
                Intent intent10 = new Intent(Masjid.this, Help.class);
                startActivity(intent10);

                break;
            case R.id.Attendancehistory:
                Intent intent11 = new Intent(Masjid.this, AttendanceHistory.class);
                startActivity(intent11);

                break;
            case R.id.seeting:
                Intent intent12 = new Intent(Masjid.this, MasjidSettin.class);
                startActivity(intent12);

                break;


            case R.id.btnMasjidlogout:
                PreferenceManager.setCount3(0);
                getLogout();

                break;


            default:
                break;
        }

    }
    public void getLogout(){
        String url="http://mymasjid.space/mobileApp/admin/masjid_logout";
        final String user_id=PreferenceManager.getMasjid();
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("error").equals("false")) {
                                PreferenceManager.setCheck(-1);
                                Intent intent7=new Intent(Masjid.this,RegistrationMainScreen.class);
                                startActivity(intent7);
                                finish();


                            }
                            else {
                                Toast.makeText(Masjid.this,"Not refund",Toast.LENGTH_SHORT).show();
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
                params.put("masjid",user_id);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);


    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
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
                                Glide.with(Masjid.this).load(ad_image).into(advertise_image);
                                moveTowebView();
                            }
                            else {
                                Toast.makeText(Masjid.this,response.getString("result"),Toast.LENGTH_SHORT).show();
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
        advertise_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Registration.this,"move to webview",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(ad_link));
                startActivity(intent);
            }
        });
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Update your RecyclerView here using notifyItemInserted(position);
            Bundle bundle = intent.getExtras();
            type=bundle.getString("type");
            if(type.equals("question"))
            {
                not_count=bundle.getInt("count");
                layout_question.setVisibility(View.VISIBLE);
                txt_question.setText(String.valueOf(not_count));
            }
        }


    };

}
