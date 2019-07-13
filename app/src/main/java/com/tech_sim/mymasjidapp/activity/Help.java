package com.tech_sim.mymasjidapp.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.R;

import org.json.JSONObject;

public class Help extends AppCompatActivity {
    Button btnCancelAboutUS;
    ImageView facebook, twiter, linkedin, googleplus;

    TextView contect, emailHelp, description;

    String FaceBook, Twitter, Linkedin, GooglePlus, Contect, EmailHelp, Description;
    Uri uriFacbook, uriTwitter, uriLinkedin, uriGooglePlus;
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getData();
        btnCancelAboutUS = findViewById(R.id.btncancelaboutUs);
        facebook = findViewById(R.id.facbook);
        twiter = findViewById(R.id.twiter);
        linkedin = findViewById(R.id.linkedin);
        googleplus = findViewById(R.id.gogleplus);
        contect = findViewById(R.id.contect);
        emailHelp = findViewById(R.id.emailhelp);
        description = findViewById(R.id.descriptionHelp);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!URLUtil.isValidUrl(FaceBook)) {
                        Toast.makeText(Help.this, " This is not a valid link", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(FaceBook));
                        startActivity(intent);
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Help.this, " You don't have any browser to open web page", Toast.LENGTH_LONG).show();
                }
            }
        });
        twiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!URLUtil.isValidUrl(Twitter)) {
                        Toast.makeText(Help.this, " This is not a valid link", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(Twitter));
                        startActivity(intent);
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Help.this, " You don't have any browser to open web page", Toast.LENGTH_LONG).show();
                }
            }
        });
        googleplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!URLUtil.isValidUrl(emailHelp.getText().toString())) {
                        Toast.makeText(Help.this, " This is not a valid link", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(emailHelp.getText().toString()));
                        startActivity(intent);
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Help.this, " You don't have any browser to open web page", Toast.LENGTH_LONG).show();
                }
            }
        });
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!URLUtil.isValidUrl(Linkedin)) {
                        Toast.makeText(Help.this, " This is not a valid link", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(Linkedin));
                        startActivity(intent);
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(Help.this, " You don't have any browser to open web page", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancelAboutUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                startActivity(new Intent(Help.this,Masjid.class));
            }
        });
    }

    void getData() {
        String url = "http://mymasjid.space/mobileApp/admin/about_us";
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject reader = new JSONObject(response);

                            JSONObject obj = reader.getJSONObject("result");

                            FaceBook = obj.getString("facebook");
                            Linkedin = obj.getString("instagram");
                            Twitter = obj.getString("twitter");
//                            GooglePlus = obj.getString("google_plus");
                            EmailHelp = obj.getString("email");
                            Contect = obj.getString("contact");
                            Description = obj.getString("about_us");
                            description.setText(Description);
                            emailHelp.setText(EmailHelp);
                            contect.setText(Contect);

                        } catch (Exception e) {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        r.add(stringRequest);
    }
}