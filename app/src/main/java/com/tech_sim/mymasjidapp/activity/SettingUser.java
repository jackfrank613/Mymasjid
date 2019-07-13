package com.tech_sim.mymasjidapp.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rilixtech.CountryCodePicker;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.SongModel;
import com.tech_sim.mymasjidapp.model.UserinfoModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingUser extends AppCompatActivity implements View.OnClickListener{
    private EditText et_firstname,et_lastname, et_email,et_house,et_phone,et_password,et_newpass,et_conpass;
    private CheckBox check_athan,check_athan2, check_athan3;
    private Button btn_cancel,btn_save;
    private Constants constants;
    private MediaPlayer mediaPlayer;
    CountryCodePicker ccp;
    RelativeLayout phoneverify;
    private ImageView image1,image2,image3;
    private boolean is_check=true;
    private RelativeLayout lay_athan,lay_athan1,lay_athan2;

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
        setContentView(R.layout.activity_account_setting);
        constants=new Constants(this);
        initXml();
        Gson gson=new Gson();
        String list= PreferenceManager.get();
        Type type = new TypeToken<ArrayList<UserinfoModel>>() {}.getType();
        ArrayList<UserinfoModel> models=gson.fromJson(list,type);
        if(models!=null) {
            for (int i = 0; i < models.size(); i++) {
                et_firstname.setText(models.get(i).getF_name());
                et_lastname.setText(models.get(i).getL_name());
                et_phone.setText(models.get(i).getPhone());
                et_email.setText(models.get(i).getEmail());
                et_house.setText(models.get(i).getHouse());
            }
        }
        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus

                }
            }
        });
        if(PreferenceManager.getSongcheck()==1)
        {
            check_athan.setChecked(true);
        }
        else if(PreferenceManager.getSongcheck()==2) {
            check_athan2.setChecked(true);
        }
        else {
            check_athan3.setChecked(true);
        }
        if(Constants.check_veryfiy)
        {
            et_phone.setFocusable(false);
        }
        phoneverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_phone.getText().toString().equals(""))
                {
                    String phone=et_phone.getText().toString().trim();
                    Intent intent=new Intent(SettingUser.this,PhoneVerify.class);
                    intent.putExtra("phone_number","+"+ccp.getSelectedCountryCode()+phone);
                    intent.putExtra("phone",phone);
                    intent.putExtra("code","+"+ccp.getSelectedCountryCode());
                    startActivity(intent);
                }

            }
        });
//        check_athan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(check_athan.isChecked())
//                {
//                    check_athan2.setEnabled(false);
//                    check_athan3.setEnabled(false);
//                }
//                else {
//                    check_athan3.setEnabled(true);
//                    check_athan2.setEnabled(true);
//                }
//            }
//        });
//        check_athan2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(check_athan2.isChecked())
//                {
//                    check_athan.setEnabled(false);
//                    check_athan3.setEnabled(false);
//                }
//                else {
//                    check_athan.setEnabled(true);
//                    check_athan3.setEnabled(true);
//                }
//            }
//        });
//        check_athan3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(check_athan3.isChecked())
//                {
//                    check_athan.setEnabled(false);
//                    check_athan2.setEnabled(false);
//                }
//                else {
//                    check_athan2.setEnabled(true);
//                    check_athan.setEnabled(true);
//                }
//
//            }
//        });
        lay_athan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_check)
                {
                    image1.setImageResource(R.drawable.sound);
                     startSong(1);
                     is_check=false;

                }
                else {
                    image1.setImageResource(R.drawable.mute);
                    stopSong(1);
                    is_check=true;
                }

            }
        });
        lay_athan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_check)
                {
                    image2.setImageResource(R.drawable.sound);
                    startSong(2);
                    is_check=false;
                }
                else {
                    image2.setImageResource(R.drawable.mute);
                    stopSong(2);
                    is_check=true;
                }
            }
        });
        lay_athan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(is_check)
                {
                    image3.setImageResource(R.drawable.sound);
                    startSong(3);
                    is_check=false;
                }
                else {
                    image3.setImageResource(R.drawable.mute);
                    stopSong(3);
                    is_check=true;
                }
            }
        });
        check_athan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_athan2.setChecked(false);
                check_athan3.setChecked(false);
                if(check_athan.isChecked()) {
                    PreferenceManager.setSong("");
                    PreferenceManager.setSong(PreferenceManager.songModels.get(0).getS_song());
                    PreferenceManager.setSongcheck(1);

                }
                else {
                    PreferenceManager.setSong("");
                }

            }
        });
        check_athan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_athan.setChecked(false);
                check_athan3.setChecked(false);
                if(check_athan2.isChecked()) {
                    PreferenceManager.setSong("");
                    PreferenceManager.setSong(PreferenceManager.songModels.get(0).getM_song());
                    PreferenceManager.setSongcheck(2);
                }
                else {
                    PreferenceManager.setSong("");
                }
            }
        });
        check_athan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_athan2.setChecked(false);
                check_athan.setChecked(false);
                if(check_athan3.isChecked()) {
                    PreferenceManager.setSong("");
                    PreferenceManager.setSong(PreferenceManager.songModels.get(0).getL_song());
                    PreferenceManager.setSongcheck(3);
                }
                else {
                    PreferenceManager.setSong("");
                }
            }
        });

    }
    public void initXml(){

        et_firstname=findViewById(R.id.firstNameS);
        et_lastname=findViewById(R.id.lastNameS);
        et_email=findViewById(R.id.set_email);
        et_house=findViewById(R.id.HouseNoS);
        et_phone=findViewById(R.id.phone_number_edt);
        et_password=findViewById(R.id.password);
        et_newpass=findViewById(R.id.Newpass);
        et_conpass=findViewById(R.id.conpass);
        check_athan=findViewById(R.id.athan1);
        check_athan2=findViewById(R.id.athan2);
        check_athan3=findViewById(R.id.athan3);
        btn_cancel=findViewById(R.id.btncancelsetings);
        btn_save=findViewById(R.id.btnsavesettingsUser);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        phoneverify=(RelativeLayout)findViewById(R.id.phoneverify);
        image1=(ImageView)findViewById(R.id.image_athan);
        image2=(ImageView)findViewById(R.id.image_athan2);
        image3=(ImageView)findViewById(R.id.image_athan3);
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        lay_athan=findViewById(R.id.layout_athan1);
        lay_athan1=findViewById(R.id.layout_athan2);
        lay_athan2=findViewById(R.id.layout_athan3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btncancelsetings:
                finish();
                break;
            case R.id.btnsavesettingsUser:
                 String newpass=et_newpass.getText().toString();
                 String conpass=et_conpass.getText().toString();
                if(!newpass.equals(conpass)||newpass.equals("")||conpass.equals("")) {
                    Toast.makeText(SettingUser.this,"Please,enter the correct values",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(!Constants.check_veryfiy)
                    {
                        Toast.makeText(SettingUser.this,"Please,verify your phone number.",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        saveSetting();
                    }

                }
                break;
        }
    }
    public void saveSetting(){
         final String id=PreferenceManager.getUserid();
         final String first=et_firstname.getText().toString().trim();
         final String last=et_lastname.getText().toString().trim();
         final String house=et_house.getText().toString().trim();
         final String email=et_email.getText().toString().trim();
         final String mobile_code=ccp.getSelectedCountryCode();
         final String mobile_number=et_phone.getText().toString().trim();
         final String old_pass=et_password.getText().toString().trim();
         final String new_pass=et_newpass.getText().toString().trim();
        String url="http://mymasjid.space/mobileApp/admin/user_info_update";
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject object1=new JSONObject(response);
                            if (object1.getString("error").equals("false"))
                            {
                                Intent intent=new Intent(SettingUser.this,UserDashboard.class);
                                startActivity(intent);
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
                 params.put("id",id);
                 params.put("firstname",first);
                 params.put("lastname",last);
                 params.put("house_no",house);
                 params.put("email",email);
                 params.put("mobile_code",mobile_code);
                 params.put("mobile_number",mobile_number);
                 params.put("old_password",old_pass);
                 params.put("new_password",new_pass);
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);

    }

    public void startSong(int j)
    {
        String song;
        if(j==1)
        {
        song=PreferenceManager.songModels.get(0).getS_song();

    }
        else if(j==2)
        {
            song=PreferenceManager.songModels.get(0).getM_song();

        }
        else {
            song=PreferenceManager.songModels.get(0).getL_song();

        }

        try {
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(song);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void stopSong(int k){
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(k==1)
            {
                Constants.saveSong=PreferenceManager.songModels.get(0).getS_song();
//                check_athan.setChecked(true);
            }
            else if(k==2)
            {
                Constants.saveSong=PreferenceManager.songModels.get(0).getM_song();
//                check_athan2.setChecked(true);

            }
            else {
                Constants.saveSong=PreferenceManager.songModels.get(0).getL_song();
//                check_athan3.setChecked(true);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
