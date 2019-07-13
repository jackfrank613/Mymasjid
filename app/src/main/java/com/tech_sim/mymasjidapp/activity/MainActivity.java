package com.tech_sim.mymasjidapp.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rd.PageIndicatorView;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.adapter.SliderPagerAdapter;
import com.tech_sim.mymasjidapp.model.PagerModel;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Dialog dialog;
    Button next;
    String deviceIMEI;
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
    public static String getUniqueIMEIId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "";
            }
            String imei = telephonyManager.getDeviceId();
            Log.e("imei", "=" + imei);
            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                return android.os.Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "not_found";
    }
    private void CheckPermissionAndStartIntent() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            //SEY SOMTHING LIKE YOU CANT ACCESS WITHOUT PERMISSION
        } else {
            doSomthing();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doSomthing();
                } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    //SEY SOMTHING LIKE YOU CANT ACCESS WITHOUT PERMISSION
                    //you can show something to user and open setting -> apps -> youApp -> permission
                    // or unComment below code to show permissionRequest Again
                    //ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                }
            }
        }
    }
    public void doSomthing() {
        deviceIMEI = getUniqueIMEIId(MainActivity.this);
        Log.d("imei_track",deviceIMEI);
       // PreferenceManager.imei=deviceIMEI;
        //andGoToYourNextStep
        PreferenceManager.setIMEI(deviceIMEI);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_layout);
        //////////////////////////////////////////
        List<PagerModel> pagerArr = new ArrayList<>();
        pagerArr.add(new PagerModel("1", getResources().getString(R.string.IntroDescriptionOne),
                getResources().getString(R.string.IntroDescriptionTwo),getResources().getString(R.string.IntroDescriptionThree)));
        pagerArr.add(new PagerModel("2", getResources().getString(R.string.IntroDescriptionOne),
                getResources().getString(R.string.IntroDescriptionTwo),getResources().getString(R.string.IntroDescriptionFour)));
        pagerArr.add(new PagerModel("3", getResources().getString(R.string.IntroDescriptionOne),
                getResources().getString(R.string.IntroDescriptionTwo),getResources().getString(R.string.IntroDescriptionFive)));
        ///////////////////////////////////////////
        SliderPagerAdapter adapter = new SliderPagerAdapter(this, pagerArr);
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        CheckPermissionAndStartIntent();
        final PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(pager);
        pageIndicatorView.setSelection(2);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });
        /////////////////////////////////////
        next = findViewById(R.id.btnNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,RegistrationMainScreen.class);
                startActivity(intent);
            }
        });
    }
}
