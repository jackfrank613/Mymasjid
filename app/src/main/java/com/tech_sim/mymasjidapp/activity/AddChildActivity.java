package com.tech_sim.mymasjidapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdView;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddChildActivity extends AppCompatActivity {
    Button btnchildCancel, btnchildUpdate;
    AdView adView;
    EditText pastepin,pastchild;
    ProgressDialog progressDialogAddC;
    Constants constants;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        constants=new Constants(this);
        btnchildCancel = findViewById(R.id.btnChildcancel);
        btnchildUpdate = findViewById(R.id.btnChildUpdate);
        pastepin=findViewById(R.id.pastePin);
        pastchild=findViewById(R.id.pastechild);
        btnchildCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                startActivity(new Intent(AddChild.this, UserDashboard.class));
            }
        });
        btnchildUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin=pastepin.getText().toString().trim();
                String child=pastchild.getText().toString().trim();
                if (pastepin.getText().toString().trim().equals("")||pastchild.getText().toString().equals("")) {
                    pastepin.setError("Please Enter Question Description");
                } else {

                    AddChildUSer(pin,child);
                }

            }
        });

    }
    private void AddChildUSer(final String pin, final String child) {
        String url="http://mymasjid.space/mobileApp/admin/addchildren_user";
        final String masjid_id= PreferenceManager.getMasjid();
        final String user_id=PreferenceManager.getUserid();
        constants.kpHUD.show();
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        constants.kpHUD.dismiss();
                        try {
//                            Log.e("Response AD: ", response);
                            JSONObject obj = new JSONObject(response);
                            if(obj.getString("error").equals("false"))
                            {
                                Toast.makeText(AddChildActivity.this,obj.getString("result"),Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(AddChildActivity.this, ManageChildActivit.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(AddChildActivity.this,obj.getString("result"),Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),"No Answer Given",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pin_number", pin);
                params.put("masjid", masjid_id);
                params.put("child_name", child);
                params.put("user_id",user_id);
                return params;
            }
        };
        r.add(stringRequest);
    }
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }
}
