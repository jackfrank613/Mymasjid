package com.tech_sim.mymasjidapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddSalahActivity extends AppCompatActivity implements View.OnClickListener {
    EditText AddStartTime, AddJamahTime, AddEndTime;
    Button AddSalahTimeCancel, AddSalahTimeUpdate;
    Spinner NamazName;
    ProgressDialog progressDialogAddSalah;
    ArrayList<String> SalahArray = new ArrayList<String>();
    String SalahStatus,SalahStartTime,SalahEndTime,SalahJammahTime,SalahNamazName,SalahID;
    Constants constants;
    static Dialog d ;
    String format;


    final Calendar c = Calendar.getInstance();

    final int hour = c.get(Calendar.HOUR_OF_DAY);

    final int minute = c.get(Calendar.MINUTE);

    int year = Calendar.getInstance().get(Calendar.YEAR);
    private Calendar calendar;
    private int CalendarHour;
    private int CalendarMinute;
    TimePickerDialog timePickerDialog;
    private TimePicker timePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_salashtime);
        AddStartTime = findViewById(R.id.ADDNamazStartTime);
        AddEndTime = findViewById(R.id.ADDNamazEndTime);
        AddJamahTime = findViewById(R.id.ADDJammahTime);

        NamazName = findViewById(R.id.SelectNamazName);
        AddSalahTimeCancel = findViewById(R.id.ADDSalahTimeCancel);
        AddSalahTimeUpdate = findViewById(R.id.ADDSalahTimeUpdate);
        constants=new Constants(this);
        AddStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showYearDialog(1);
                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            if (hourOfDay == 0) {
                                hourOfDay += 12;
                                format = "AM";
                            } else if (hourOfDay == 12) {
                                format = "PM";
                            } else if (hourOfDay > 12) {
                                hourOfDay -= 12;
                                format = "PM";
                            } else {
                                format = "AM";
                            }
                            showDisplay(hourOfDay,minute,format,1);
                        }
                    }
                };
            //    TimePickerDialog timePickerDialog = new TimePickerDialog(AddSalahActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddSalahActivity.this, android.R.style.Theme_Holo_Light_Dialog, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Salah Time");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();

            }
        });

        AddJamahTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            if (hourOfDay == 0) {
                                hourOfDay += 12;
                                format = "AM";
                            } else if (hourOfDay == 12) {
                                format = "PM";
                            } else if (hourOfDay > 12) {
                                hourOfDay -= 12;
                                format = "PM";
                            } else {
                                format = "AM";
                            }
                            showDisplay(hourOfDay,minute,format,2);
                        }
                    }
                };
                //    TimePickerDialog timePickerDialog = new TimePickerDialog(AddSalahActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddSalahActivity.this, android.R.style.Theme_Holo_Light_Dialog, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Salah Time");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();

            }
        });
        AddEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (view.isShown()) {
                            if (hourOfDay == 0) {
                                hourOfDay += 12;
                                format = "AM";
                            } else if (hourOfDay == 12) {
                                format = "PM";
                            } else if (hourOfDay > 12) {
                                hourOfDay -= 12;
                                format = "PM";
                            } else {
                                format = "AM";
                            }
//                            AddEndTime.setText(hourOfDay + ":" + minute + format);
                            showDisplay(hourOfDay,minute,format,3);
                        }
                    }
                };
                //    TimePickerDialog timePickerDialog = new TimePickerDialog(AddSalahActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddSalahActivity.this, android.R.style.Theme_Holo_Light_Dialog, myTimeListener, hour, minute, true);
                timePickerDialog.setTitle("Salah Time");
                timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                timePickerDialog.show();

            }
        });


        AddSalahTimeUpdate.setOnClickListener(this);
        AddSalahTimeCancel.setOnClickListener(this);
        progressDialogAddSalah = new ProgressDialog(this);

//        SalahStatus = getIntent().getExtras().getString("status");
//
//        if(SalahStatus.equals("SingleSalah")){
//
//            SalahStartTime = getIntent().getExtras().getString("start_name");
//            SalahEndTime = getIntent().getExtras().getString("end_time");
//            SalahNamazName=getIntent().getExtras().getString("salah_name");
//            SalahJammahTime = getIntent().getExtras().getString("jamaah_time");
//            SalahID=getIntent().getExtras().getString("Salah_ID");
//
//            AddStartTime.setText(SalahStartTime);
//            AddJamahTime.setText(SalahJammahTime);
//            AddEndTime.setText(SalahEndTime);
//            final ArrayAdapter arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, SalahArray);
//            SalahArray.add(SalahNamazName);
//            NamazName.setAdapter(arrayAdapter);
//
//            arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
//            arrayAdapter.notifyDataSetChanged();
//            //NamazName.setSelected(Boolean.parseBoolean(SalahNamazName));
//            //NamazName.setAutofillHints(SalahNamazName);
//
//        }
//        else {
////            do nothing
//        }
    }
    public void showDisplay(int hour,int minute,String format,int select)
    {
        if(minute>10)
        {
            if(select==1) {
                AddStartTime.setText(String.valueOf(hour) + ":" + String.valueOf(minute)+" "+format);
            }
            else if(select==2)
            {
                AddJamahTime.setText(String.valueOf(hour) + ":" + String.valueOf(minute)+" "+format);
            }
            else {
                AddEndTime.setText(String.valueOf(hour) + ":" + String.valueOf(minute)+" "+format);

            }

        }
        else if(minute<10)
        {
            if(select==1) {
                AddStartTime.setText(String.valueOf(hour) + ":" + "0" + String.valueOf(minute)+" "+format);
            }
            else if(select==2)
            {
                AddJamahTime.setText(String.valueOf(hour) + ":" + "0" + String.valueOf(minute)+" "+format);

            }
            else {
                AddEndTime.setText(String.valueOf(hour) + ":" + "0" + String.valueOf(minute)+" "+format);

            }

        }
    }

    private void AddSalahTimeMasjid() {

//        SharedPreferences prefs;
//        prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
//        final String Masjid;
//        Masjid = prefs.getString("MasjidID", null);
//        Log.e("rst", Masjid);
        final String Masjid=PreferenceManager.getMasjid();

        final String mAddStartTimeHolder = AddStartTime.getText().toString().trim();
        final String mAddEndTimeHolder = AddEndTime.getText().toString().trim();
        final String mAddJamahTimeHolder = AddJamahTime.getText().toString().trim();
        final String mSelectNamazName = NamazName.getSelectedItem().toString();
        final int status = 1;
        constants.kpHUD.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_ADD_SALAH_TIME_MASJID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("error").equals("false")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
                                AddStartTime.setText("");
                                AddEndTime.setText("");
                                AddJamahTime.setText("");
                                // startActivity(new Intent(AddSalahActivity.this, SalahMasjidPanel.class));
                            }else {
//                                Toast.makeText(getApplicationContext(),
//                                        jsonObject.getString("result"), Toast.LENGTH_SHORT).show();
                                T.showError(AddSalahActivity.this,jsonObject.getString("result"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("start_time", mAddStartTimeHolder);
                params.put("jamaah_time", mAddJamahTimeHolder);
                assert Masjid != null;
                params.put("masjid", Masjid);
                params.put("end_time", mAddEndTimeHolder);
                params.put("name", mSelectNamazName);
                params.put("status", String.valueOf(status));
                return params;
            }
        };
        PreferenceManager.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View view) {

        if (view == AddSalahTimeUpdate) {
                if (AddStartTime.getText().toString().trim().equals("")) {
                    AddStartTime.setError("Please Enter Salah Start time");
                } else if (AddJamahTime.getText().toString().trim().equals("")) {
                    AddJamahTime.setError("Please Enter Salah Jammah Time");
                } else if (AddEndTime.getText().toString().trim().equals("")) {
                    AddEndTime.setError("Please Enter Salah End Time");
                } else {

                    AddSalahTimeMasjid();
                }

        } else if (view == AddSalahTimeCancel) {
            finish();
//            startActivity(new Intent(AddSalahaTime.this, SalahMasjidPanel.class));
        }


    }
    public void showYearDialog(final int select)

    {



        final Dialog d = new Dialog(AddSalahActivity.this);

        d.setTitle("Year Picker");

        d.setContentView(R.layout.timedialog);

        Button set = (Button) d.findViewById(R.id.button1);

        Button cancel = (Button) d.findViewById(R.id.button2);

        TextView year_text=(TextView)d.findViewById(R.id.title_text);

//        title_text.setText(hour+":"+minute);

        final NumberPicker nopicker = (NumberPicker) d.findViewById(R.id.numberPicker1);

        final NumberPicker nopicker1 = (NumberPicker) d.findViewById(R.id.numberPicker2);
       String[] values={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
       String[] values1={"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23",
               "24","25","26","27","28","29","30","31","32","33","34","35","36","37","38","39","40","41","42","43",
               "44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59"};
        nopicker.setMinValue(0);
        nopicker.setMaxValue(values.length-1);
        nopicker.setDisplayedValues(values);
        nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        nopicker1.setMaxValue(values1.length-1);
        nopicker1.setMinValue(0);
        nopicker1.setDisplayedValues(values1);
        nopicker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        set.setOnClickListener(new View.OnClickListener()

        {

            @Override            public void onClick(View v) {
                int hour=nopicker.getValue();
                int minute=nopicker1.getValue();
                if(hour<10&&minute>10)
                {
                    if(select==1) {
                        AddStartTime.setText("0" + String.valueOf(hour) + ":" + String.valueOf(minute));
                    }
                    else if(select==2)
                    {
                        AddJamahTime.setText("0" + String.valueOf(hour) + ":" + String.valueOf(minute));
                    }
                    else {
                        AddEndTime.setText("0" + String.valueOf(hour) + ":" + String.valueOf(minute));

                    }

                }
                else if(minute<10&&hour>10)
                {
                    if(select==1) {
                        AddStartTime.setText(String.valueOf(hour) + ":" + "0" + String.valueOf(minute));
                    }
                    else if(select==2)
                    {
                        AddJamahTime.setText(String.valueOf(hour) + ":" + "0" + String.valueOf(minute));

                    }
                    else {
                        AddEndTime.setText(String.valueOf(hour) + ":" + "0" + String.valueOf(minute));

                    }

                }
                else if(hour<10 && minute<10)
                {
                    if(select==1) {
                        AddStartTime.setText("0" + String.valueOf(hour) + ":" + "0" + String.valueOf(minute));
                    }
                    else if(select==2)
                    {
                        AddJamahTime.setText("0" + String.valueOf(hour) + ":" + "0" + String.valueOf(minute));
                    }
                    else {
                        AddEndTime.setText("0" + String.valueOf(hour) + ":" + "0" + String.valueOf(minute));

                    }
                }
                else {
                    if(select==1) {
                        AddStartTime.setText(String.valueOf(nopicker.getValue() + ":" + nopicker1.getValue()));
                    }
                    else if(select==2)
                    {
                        AddJamahTime.setText(String.valueOf(nopicker.getValue() + ":" + nopicker1.getValue()));

                    }
                    else {
                        AddEndTime.setText(String.valueOf(nopicker.getValue() + ":" + nopicker1.getValue()));

                    }
                }

                d.dismiss();

            }

        });

        cancel.setOnClickListener(new View.OnClickListener()

        {

            @Override            public void onClick(View v) {

                d.dismiss();

            }

        });

        d.show();

    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
