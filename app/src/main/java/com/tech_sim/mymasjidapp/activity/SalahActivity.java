package com.tech_sim.mymasjidapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.model.SalahTimeModel;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.T;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SalahActivity extends AppCompatActivity {
    Button salahCancel, salahUpdate;
    TextView faja, zhor, asr, maghrib, isha, juma,tahajiud;
    TextView StartTimeText, EndTimeText, JammahTimeText;
    String SalahName = "";
    String StartTime, EndTime, JammahTime;
    ProgressDialog progressDialog;
    ImageView alarm;
    LinearLayout hiden;
    private boolean isDisplay=true;
    private ArrayList<SalahTimeModel> salahTimeModels=new ArrayList<>();
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
        setContentView(R.layout.activity_salah_screen);
        faja = findViewById(R.id.faja);
        zhor = findViewById(R.id.zhor);
        asr = findViewById(R.id.asr);
        maghrib = findViewById(R.id.maghrib);
        isha = findViewById(R.id.isha);
        juma = findViewById(R.id.juma);
        tahajiud = findViewById(R.id.tahajiud);

        hiden=findViewById(R.id.Hidden);
        hiden.setVisibility(View.GONE);
        salahCancel = findViewById(R.id.salahcancel);
        //   salahUpdate = findViewById(R.id.salahupdate);
        StartTimeText = findViewById(R.id.startTimetext);
        EndTimeText = findViewById(R.id.endTimetext);
        JammahTimeText = findViewById(R.id.jammahTimetext);
        salahCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setClickViewLisener();
    }
    public void setClickViewLisener(){
        final int count=PreferenceManager.salahTimeModels.size();
        faja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   if(PreferenceManager.salahTimeModels.size()==0)
                   {
                       T.showError(SalahActivity.this,"Masjid does not insert salah time yet.");
                   }
                   else {
                       for (int i = 0; i < PreferenceManager.salahTimeModels.size(); i++) {
                           if (PreferenceManager.salahTimeModels.get(i).getS_name().equals("Fajar")) {
                               hiden.setVisibility(View.VISIBLE);
                               StartTimeText.setText(PreferenceManager.salahTimeModels.get(i).getS_time());
                               JammahTimeText.setText(PreferenceManager.salahTimeModels.get(i).getJ_time());
                               EndTimeText.setText(PreferenceManager.salahTimeModels.get(i).getE_time());

                           } else {
                               //   Toast.makeText(SalahActivity.this,"Please,waiting...",Toast.LENGTH_SHORT).show();
                               Log.i("text", "please,waiting");
                           }
                       }
                   }

            }
        });
        zhor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(PreferenceManager.salahTimeModels.size()==0)
                    {
                        T.showError(SalahActivity.this,"Masjid does not insert salah time yet.");
                    }
                    else {
                        for (int i = 0; i < PreferenceManager.salahTimeModels.size(); i++) {
                            if (PreferenceManager.salahTimeModels.get(i).getS_name().equals("Zohr")) {
                                hiden.setVisibility(View.VISIBLE);
                                StartTimeText.setText(PreferenceManager.salahTimeModels.get(i).getS_time());
                                JammahTimeText.setText(PreferenceManager.salahTimeModels.get(i).getJ_time());
                                EndTimeText.setText(PreferenceManager.salahTimeModels.get(i).getE_time());

                            } else {
                                Log.i("text", "please,waiting");
                                //  Toast.makeText(SalahActivity.this,"Please,waiting...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

        });
        asr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(count==0)
                    {
                        T.showError(SalahActivity.this,"Masjid does not insert salah time yet.");

                    }
                    else {
                        for (int i = 0; i < PreferenceManager.salahTimeModels.size(); i++) {
                            if (PreferenceManager.salahTimeModels.get(i).getS_name().equals("Asr")) {
                                hiden.setVisibility(View.VISIBLE);
                                StartTimeText.setText(PreferenceManager.salahTimeModels.get(i).getS_time());
                                JammahTimeText.setText(PreferenceManager.salahTimeModels.get(i).getJ_time());
                                EndTimeText.setText(PreferenceManager.salahTimeModels.get(i).getE_time());

                            } else {
                                //  Toast.makeText(SalahActivity.this,"Please,waiting...",Toast.LENGTH_SHORT).show();
                                Log.i("text", "please,waiting");
                            }
                        }
                    }
                    isDisplay=false;
                }

        });
        maghrib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(count==0)
                    {
                        T.showError(SalahActivity.this,"Masjid does not insert salah time yet.");

                    }
                    else {
                        for (int i = 0; i < PreferenceManager.salahTimeModels.size(); i++) {
                            if (PreferenceManager.salahTimeModels.get(i).getS_name().equals("Maghrib")) {
                                hiden.setVisibility(View.VISIBLE);
                                StartTimeText.setText(PreferenceManager.salahTimeModels.get(i).getS_time());
                                JammahTimeText.setText(PreferenceManager.salahTimeModels.get(i).getJ_time());
                                EndTimeText.setText(PreferenceManager.salahTimeModels.get(i).getE_time());

                            } else {
                                // Toast.makeText(SalahActivity.this,"Please,waiting...",Toast.LENGTH_SHORT).show();
                                Log.i("text", "please,waiting");
                            }
                        }
                    }
                    isDisplay=false;
                }

        });
        isha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(count==0)
                    {
                        T.showError(SalahActivity.this,"Masjid does not insert salah time yet.");

                    }
                    else {
                        for (int i = 0; i < PreferenceManager.salahTimeModels.size(); i++) {
                            if (PreferenceManager.salahTimeModels.get(i).getS_name().equals("Isha")) {
                                hiden.setVisibility(View.VISIBLE);
                                StartTimeText.setText(PreferenceManager.salahTimeModels.get(i).getS_time());
                                JammahTimeText.setText(PreferenceManager.salahTimeModels.get(i).getJ_time());
                                EndTimeText.setText(PreferenceManager.salahTimeModels.get(i).getE_time());

                            } else {
                                // Toast.makeText(SalahActivity.this,"Please,waiting...",Toast.LENGTH_SHORT).show();
                                Log.i("text", "please,waiting");
                            }
                        }
                    }
                    isDisplay=false;
                }

        });
        juma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(count==0)
                    {
                        T.showError(SalahActivity.this,"Masjid does not insert salah time yet.");

                    }
                    else {
                        for (int i = 0; i < PreferenceManager.salahTimeModels.size(); i++) {
                            if (PreferenceManager.salahTimeModels.get(i).getS_name().equals("Juma")) {
                                hiden.setVisibility(View.VISIBLE);
                                StartTimeText.setText(PreferenceManager.salahTimeModels.get(i).getS_time());
                                JammahTimeText.setText(PreferenceManager.salahTimeModels.get(i).getJ_time());
                                EndTimeText.setText(PreferenceManager.salahTimeModels.get(i).getE_time());

                            } else {
                                //  Toast.makeText(SalahActivity.this,"Please,waiting...",Toast.LENGTH_SHORT).show();
                                Log.i("text", "please,waiting");
                            }
                        }
                    }
                    isDisplay=false;
                }

        });
        tahajiud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(count==0)
                    {
                        T.showError(SalahActivity.this,"Masjid does not insert salah time yet.");

                    }
                    else {
                        for (int i = 0; i < PreferenceManager.salahTimeModels.size(); i++) {
                            if (PreferenceManager.salahTimeModels.get(i).getS_name().equals("Tahajud")) {
                                hiden.setVisibility(View.VISIBLE);
                                StartTimeText.setText(PreferenceManager.salahTimeModels.get(i).getS_time());
                                JammahTimeText.setText(PreferenceManager.salahTimeModels.get(i).getJ_time());
                                EndTimeText.setText(PreferenceManager.salahTimeModels.get(i).getE_time());

                            } else {
                                //   Toast.makeText(SalahActivity.this,"Please,waiting...",Toast.LENGTH_SHORT).show();
                                Log.i("text", "please,waiting");
                            }
                        }
                    }
                    isDisplay=false;
                }
        });

    }
}
