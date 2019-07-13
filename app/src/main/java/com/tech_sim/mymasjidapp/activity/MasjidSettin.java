package com.tech_sim.mymasjidapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.JsonObject;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.model.AccountParams;
import com.stripe.android.model.BankAccount;
import com.stripe.android.model.Token;
import com.tech_sim.mymasjidapp.R;
import com.tech_sim.mymasjidapp.utils.AppHelper;
import com.tech_sim.mymasjidapp.utils.Constants;
import com.tech_sim.mymasjidapp.utils.PreferenceManager;
import com.tech_sim.mymasjidapp.utils.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class MasjidSettin extends AppCompatActivity {

    Button btn_cancel, btn_save;
    private EditText et_name, et_account_number, et_holder_name, et_routing_number, et_firstname, et_lastname, et_email, et_phone, et_day, et_month, et_year, et_city, et_postcode, et_state, et_country,et_website;
    private EditText et_street;
    private  TextView et_a_country;
    private CountryCodePicker picker, picker1,picker2;
    private ImageView image_camera;
    private static final int REQ_CODE = 9001;
    private static final int SELECT_PICTURE = 1;
    private Constants constants;
    private Stripe stripe;
    TextView txt_country;
    private EditText et_currency;
    String file_id;
    String bank_token;
    String stripe_token;
    String accountID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        constants = new Constants(this);
        initXml();
//        getData();

//        test1Token();
//        testToken();
        et_name.setText(PreferenceManager.getNamer());
        et_account_number.setText(PreferenceManager.getAccountNum());
        et_holder_name.setText(PreferenceManager.getholderName());
        //    et_country.setText(PreferenceManager.getBankerCountry());
        et_currency.setText(PreferenceManager.getCurrency());
        et_firstname.setText(PreferenceManager.getFirst());
        et_lastname.setText(PreferenceManager.getLast());
        et_email.setText(PreferenceManager.getEmail());
        et_phone.setText(PreferenceManager.getphoner());
        et_day.setText(PreferenceManager.getDay());
        et_month.setText(PreferenceManager.getMonther());
        et_year.setText(PreferenceManager.getYearer());
        et_city.setText(PreferenceManager.getaccountCity());
        et_state.setText(PreferenceManager.getaccountCity());
        et_street.setText(PreferenceManager.getState());
        et_a_country.setText(PreferenceManager.getUserCountry());
        accountID=PreferenceManager.getAccount();





        image_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        picker1.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country country) {
                txt_country.setText(picker1.getSelectedCountryName());
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = et_name.getText().toString().trim();
                PreferenceManager.setNamer(name);
                String holder = et_holder_name.getText().toString().trim();
                String account_number = et_account_number.getText().toString().trim();
                String route_number = et_routing_number.getText().toString().trim();
                String first_name = et_firstname.getText().toString().trim();
                String last_name = et_lastname.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();
                String day = et_day.getText().toString().trim();
                String month = et_month.getText().toString().trim();
                String year = et_year.getText().toString().trim();
                String city = et_city.getText().toString().trim();
                String postcode = et_postcode.getText().toString().trim();
                String state = et_state.getText().toString().trim();
                String country = picker1.getSelectedCountryNameCode();
                String currency = et_currency.getText().toString().trim();
                String street=et_street.getText().toString().trim();
                String a_country=picker2.getSelectedCountryNameCode();
                String link=et_website.getText().toString().trim();
                final String phone_code = picker.getSelectedCountryCode();


                if (name.equals("") || holder.equals("") || account_number.equals("") || route_number.equals("") || first_name.equals("") || last_name.equals("") || email.equals("") || phone.equals("") || day.equals("")
                        || city.equals("") || postcode.equals("") || state.equals("") || country.equals("") || currency.equals("") || month.equals("") || year.equals("")) {
                    Toast.makeText(MasjidSettin.this, "Please,enter the correct values.", Toast.LENGTH_SHORT).show();
                } else {
                    if(accountID.equals(""))
                    {
                        String b_token= bankToken(country,currency,holder,route_number,account_number);
                        String stripe_account=accountToken(first_name,last_name,email,city,postcode,state,a_country,street,day,month,year,phone,phone_code);
                        sendPaymentDetail(name, holder, account_number, route_number, first_name, last_name, email, phone, day, city, postcode, state, country, currency, month, year,a_country,street,link,b_token,stripe_account);
                    }
                    else {
                        String bb_token=bankToken(country,currency,holder,route_number,account_number);
                        sendPaymentDetail(name, holder, account_number, route_number, first_name, last_name, email, phone, day, city, postcode, state, country, currency, month, year,a_country,street,link,bb_token,"");
                    }

                }
            }
        });
    }

    public void initXml() {
        btn_cancel = (Button) findViewById(R.id.btncancelsetings);
        btn_save = (Button) findViewById(R.id.btnsavesettings);
        et_name = (EditText) findViewById(R.id.MasjidName);
        et_account_number = (EditText) findViewById(R.id.cardName);
        et_holder_name = (EditText) findViewById(R.id.accountTile);
        et_routing_number = (EditText) findViewById(R.id.accountNo);
        et_firstname = (EditText) findViewById(R.id.MasjidContect);
        et_lastname = (EditText) findViewById(R.id.last_name);
        et_email = (EditText) findViewById(R.id.email);
        et_phone = (EditText) findViewById(R.id.phone);
        et_day = (EditText) findViewById(R.id.date);
        et_month = (EditText) findViewById(R.id.month);
        et_year = (EditText) findViewById(R.id.year);
        et_city = (EditText) findViewById(R.id.city);
        et_postcode = (EditText) findViewById(R.id.postcode);
        et_state = (EditText) findViewById(R.id.state);
        et_street=(EditText)findViewById(R.id.street);
        et_a_country=(TextView)findViewById(R.id.a_country);
//        et_country=(EditText)findViewById(R.id.country);
        et_currency = (EditText) findViewById(R.id.currency);
        picker = (CountryCodePicker) findViewById(R.id.ccp);
        picker1 = (CountryCodePicker) findViewById(R.id.ccp1);
        picker2=(CountryCodePicker)findViewById(R.id.ccp2);
        image_camera = (ImageView) findViewById(R.id.p_take);
        txt_country = (TextView) findViewById(R.id.country_name);
        et_website=(EditText)findViewById(R.id.link);
//        et_currency.setText("GBP");
//        txt_country.setText("United Kingdom");

    }

    public void sendPaymentDetail(final String name, final String holder, final String account, final String route, final String firstname, final String lastname, final String email, String phone, final String day, final String city, final String postcode, final String state, final String country, final String currency, final String month, final String year,final String a_country,final  String street,final String link,final String b_token,final String stripe_account) {


        final String phone_code = picker.getSelectedCountryCode();
        final String phone_number = phone;
        final String masjid_id = PreferenceManager.getMasjid();

//        getAccountToken();
        String url = Constants.customer_url;
        String tag = "Setting";
        constants.kpHUD.show();
        VolleyMultipartRequest req = new VolleyMultipartRequest(POST,
                url, new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                constants.kpHUD.dismiss();
                String responseStr = new String(response.data);
                try {
                    JSONObject responseobject = new JSONObject(responseStr);
                    if (responseobject.getString("error").equals("false")) {
                        Toast.makeText(MasjidSettin.this, responseobject.getString("result"), Toast.LENGTH_SHORT).show();
                        btn_save.setEnabled(true);
                        JSONObject array=responseobject.getJSONObject("result");
                        String accountid=responseobject.getString("account_id");
                        PreferenceManager.setAccount(accountid);
                        String first=array.getString("first_name");
                        PreferenceManager.setFirst(first);
                        String last=array.getString("last_name");
                        PreferenceManager.setLast(last);
                        String account_country=array.getString("account_city");
                        PreferenceManager.setaccountCity(account_country);
                        String post=array.getString("postal_code");
                        PreferenceManager.setPostalcode(post);
                        String email=array.getString("email");
                        PreferenceManager.setEmail(email);
                        String phone=array.getString("phone_number");
                        PreferenceManager.setPhoner(phone);
                        String day=array.getString("day");
                        PreferenceManager.setDay(day);
                        String month=array.getString("month");
                        PreferenceManager.setMonther(month);
                        String year=array.getString("year");
                        PreferenceManager.setYearer(year);
                        String b_country=array.getString("bank_country");
                        PreferenceManager.setBankerCountry(b_country);
                        String currency=array.getString("currency");
                        PreferenceManager.setCurrency(currency);
                        String user_country=array.getString("user_country");
                        PreferenceManager.setUserCountry(user_country);
                        String street=array.getString("street");
                        PreferenceManager.setStreet(street);
                        String holder_name=array.getString("holder_name");
                        PreferenceManager.setholderName(holder_name);
                        String banklast=array.getString("bank_last4");
                        PreferenceManager.setAccountNum(banklast);

                        Intent intent = new Intent(MasjidSettin.this, Masjid.class);
                        startActivity(intent);
                        finish();
                    } else {

                        Toast.makeText(MasjidSettin.this, responseobject.getString("result"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException ignored) {
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Log.d("Error.Response", "Network is error.");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("account_number", account);
                params.put("routing_number", route);
                params.put("account_holder_name", holder);
                params.put("first_name", firstname);
                params.put("last_name", lastname);
                params.put("city", city);
                params.put("postal_code", postcode);
                params.put("state", state);
                params.put("email", email);
                params.put("phone_code", phone_code);
                params.put("phone_number", phone_number);
                params.put("day", day);
                params.put("month", month);
                params.put("year", year);
                params.put("masjid", masjid_id);
                params.put("currency", currency);
                params.put("masjid_name", name);
                params.put("bank_country", country);
                params.put("user_country",a_country);
                params.put("street",street);
                params.put("token",stripe_account);
                params.put("bank_token",b_token);
                params.put("website_url",link);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("front", new DataPart("customer.jpg", AppHelper.getFileDataFromDrawable(MasjidSettin.this, image_camera.getDrawable()), "image/jpeg"));
                return params;
            }

        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // add the request object to the queue to be executed
        PreferenceManager.getInstance().addToRequestQueue(req, tag);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            image_camera.setImageURI(selectedImageUri);
            uploadFileTostripe();

        }
    }


    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }


    public void getData() {
        String url = "http://mymasjid.space/mobileApp/admin/getMasjidSettingData";
        final String masjid = PreferenceManager.getMasjid();
        String tag = "GetData";
        VolleyMultipartRequest req = new VolleyMultipartRequest(POST,
                url, new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                constants.kpHUD.dismiss();
                String responseStr = new String(response.data);
                try {
                    JSONObject responseobject = new JSONObject(responseStr);
                    if (responseobject.getString("error").equals("false")) {
                        JSONObject object = responseobject.getJSONObject("data");
                        String name = object.getString("name");
                        String email = object.getString("email");
                        et_name.setText(name);
                        et_email.setText(email);


                    } else {
                        Toast.makeText(MasjidSettin.this, responseobject.getString("result"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException ignored) {
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Log.d("Error.Response", "that is not");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("masjid", masjid);

                return params;
            }


        };
        // add the request object to the queue to be executed
        PreferenceManager.getInstance().addToRequestQueue(req, tag);


    }

    public void uploadFileTostripe() {

        String url = " https://files.stripe.com/v1/files";
        String tag = "Setting";
        constants.kpHUD.show();
        VolleyMultipartRequest req = new VolleyMultipartRequest(POST,
                url, new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                constants.kpHUD.dismiss();
                String responseStr = new String(response.data);
                Log.d("data",responseStr);
                JSONObject object=null;
                try {
                    object=new JSONObject(responseStr);
                    file_id=object.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                constants.kpHUD.dismiss();
                Log.d("Error.Response", error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //  params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer pk_live_8WXBAJlDdCVYNnhikj2u3Pgp");
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("purpose", "identity_document");

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("file", new DataPart("customer.jpg", AppHelper.getFileDataFromDrawable(MasjidSettin.this, image_camera.getDrawable()), "image/jpeg"));
                return params;
            }

        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//         add the request object to the queue to be executed
        PreferenceManager.getInstance().addToRequestQueue(req, tag);

    }

    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    public String getCountryCode(String countryname) {
        String[] isoCountryCodes = Locale.getISOCountries();
        Map<String, String> countryMap = new HashMap<>();
        Locale locale;
        String name;
        for (String code : isoCountryCodes) {
            locale = new Locale("", code);
            name = locale.getDisplayName();
            countryMap.put(name, code);
        }
        return countryMap.get(countryname);
    }



    public String bankToken(final String country, final String currency, final String h_name, final String r_name, final String a_name) {

        String url = "https://api.stripe.com/v1/tokens";
        StringRequest jsonObjRequest = new StringRequest(
                POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String banker=response;
                        VolleyLog.d("banktoken", response);
                        Log.d("banker",banker);
                        try {
                            JSONObject object=new JSONObject(banker);
                            bank_token=object.getString("id");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer pk_live_8WXBAJlDdCVYNnhikj2u3Pgp");
                return params;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("bank_account[country]", country);
                params.put("bank_account[currency]", currency);
                params.put("bank_account[account_holder_name]", h_name);
                params.put("bank_account[account_holder_type]", "individual");
                params.put("bank_account[routing_number]", r_name);
                params.put("bank_account[account_number]", a_name);
                return params;
            }

        };

        PreferenceManager.getInstance().addToRequestQueue(jsonObjRequest);

        return bank_token;

    }

    @SuppressLint("Range")
    public void Token() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Stripe stripe = new Stripe(MasjidSettin.this);
                stripe.setDefaultPublishableKey("pk_test_OonBfw5p1DWCyFxjNkZrVZp8");
                BankAccount bankAccount = new BankAccount("000123456789", "US", "usd", "110000000");
                stripe.createBankAccountToken(bankAccount, new TokenCallback() {
                    @Override
                    public void onError(Exception error) {
                        Log.e("Stripe Error", error.getMessage());
                    }

                    @Override
                    public void onSuccess(com.stripe.android.model.Token token) {
                        Log.e("Bank Token", token.getId());
                        String id = token.getId();
                    }
                });
            }
        }).start();


    }

    public void test1Token() {

        new Thread(new Runnable() {
            public void run() {
                String url = "https://api.stripe.com/v1/tokens";
                // Map<String,Object> params=new HashMap<String, Object>();
                Map<String, String> tokenParams = new HashMap<String, String>();
                Map<String, String> accountParams = new HashMap<String, String>();
                Map<String, String> individualParams = new HashMap<String, String>();
                Map<String, String> addressParams = new HashMap<String, String>();
                Map<String, String> dobparams = new HashMap<String, String>();
                Map<String, String> verifyParams = new HashMap<String, String>();
                Map<String, String> documentParams = new HashMap<String, String>();
                Map<String, String> docParams = new HashMap<String, String>();
                docParams.put("front", "file_1Ef6ACK9WyPWJUDku6WIOOc7");
                docParams.put("back", null);
                documentParams.put("document", String.valueOf(docParams));
                dobparams.put("day", "2");
                dobparams.put("month", "2");
                dobparams.put("year", "1994");
                addressParams.put("city", "London");
                addressParams.put("postal_code", "st3");
                addressParams.put("state", "London");
                addressParams.put("country", "GB");
                individualParams.put("first_name", "Jane");
                individualParams.put("last_name", "Doe");
                individualParams.put("email", "jack@gmail.com");
                individualParams.put("phone", "+44" + "7585320049");
                individualParams.put("address", String.valueOf(addressParams));
                individualParams.put("dob", String.valueOf(dobparams));
                individualParams.put("verification", String.valueOf(documentParams));
                accountParams.put("individual", String.valueOf(individualParams));
                accountParams.put("business_type", "individual");
                // accountParams.put("tos_shown_and_accepted", true);
                tokenParams.put("account", String.valueOf(accountParams));
                JSONObject jsonObject=new JSONObject(tokenParams);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        POST,url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("TAG", response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", error.getMessage(), error);
                    }
                }) { //no semicolon or coma
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> tokenParams = new HashMap<String, String>();
                        tokenParams.put("Content-Type", "application/x-www-form-urlencoded");
                        tokenParams.put("Authorization", "Bearer sk_test_b5jSQiDwF1yGqE3ZdAJLyVJ1");
                        return tokenParams;
                    }

                };
                PreferenceManager.getInstance().addToRequestQueue(jsonObjectRequest);
            }
        }).start();


    }

    public String accountToken(final String first, final String last, final String email, final String city, final String postal, final String state, final String country, final String street, final String day, final String month, final String year,final String phone,final String phone_code) {
        String url = "https://api.stripe.com/v1/tokens";

        StringRequest jsonObjRequest = new StringRequest(

                POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String striperesponse=response;
                        VolleyLog.d("striperesponse", response);
                        try {
                            JSONObject object=new JSONObject(striperesponse);
                            stripe_token=object.getString("id");
                            Log.d("stripe_token",stripe_token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer pk_live_8WXBAJlDdCVYNnhikj2u3Pgp");
                return params;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("account[individual][first_name]", first);
                params.put("account[individual][last_name]", last);
                params.put("account[tos_shown_and_accepted]", String.valueOf(true));
                params.put("account[business_type]","individual");
                params.put("account[individual][verification][document][front]",file_id);
                params.put("account[individual][email]",email);
                //   params.put("account[individual][phone]","+"+phone_code+phone);
                params.put("account[individual][address][city]",city);
                params.put("account[individual][address][postal_code]",postal);
                params.put("account[individual][address][state]",state);
                params.put("account[individual][address][country]",country);
                params.put("account[individual][address][line1]",street);
                params.put("account[individual][dob][day]",day);
                params.put("account[individual][dob][month]",month);
                params.put("account[individual][dob][year]",year);
                return params;
            }

        };

        PreferenceManager.getInstance().addToRequestQueue(jsonObjRequest);
        return stripe_token;
    }

    public  void makeRequest() throws IOException {
        URL url = null;
        try {
            url = new URL("https://api.stripe.com/v1/tokens");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Map<String, Object> tokenParams = new HashMap<String, Object>();
        Map<String, Object> accountParams = new HashMap<String, Object>();
        Map<String, Object> individualParams = new HashMap<String, Object>();
        Map<String, Object> addressParams = new HashMap<String, Object>();
        Map<String, Object> dobparams = new HashMap<String, Object>();
        Map<String, Object> verifyParams = new HashMap<String, Object>();
        Map<String, Object> documentParams = new HashMap<String, Object>();
        Map<String, Object> docParams = new HashMap<String, Object>();
        docParams.put("front", "file_1Ef6ACK9WyPWJUDku6WIOOc7");
        docParams.put("back", null);
        documentParams.put("document", docParams);
        dobparams.put("day", "2");
        dobparams.put("month", "2");
        dobparams.put("year", "1994");
        addressParams.put("city", "London");
        addressParams.put("postal_code", "st3");
        addressParams.put("state", "London");
        addressParams.put("country", "GB");
        individualParams.put("first_name", "Jane");
        individualParams.put("last_name", "Doe");
        individualParams.put("email", "jack@gmail.com");
        individualParams.put("phone", "+44" + "7585320049");
        individualParams.put("address", addressParams);
        individualParams.put("dob", dobparams);
        individualParams.put("verification", documentParams);
        accountParams.put("individual", individualParams);
        accountParams.put("business_type", "individual");
        accountParams.put("tos_shown_and_accepted", true);
        tokenParams.put("account", accountParams);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : tokenParams.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            try {
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            postData.append('=');
            try {
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        byte[] postDataBytes = new byte[0];
        try {
            postDataBytes = postData.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Authorization","Bearer sk_test_b5jSQiDwF1yGqE3ZdAJLyVJ1");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        try {
            conn.getOutputStream().write(postDataBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Reader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
            sb.append((char)c);
        String response = sb.toString();
        Log.d("bearToken",response);

    }

}
