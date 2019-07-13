package com.tech_sim.mymasjidapp.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.tech_sim.mymasjidapp.ScheduleService;
import com.tech_sim.mymasjidapp.model.Masjiidmodel;
import com.tech_sim.mymasjidapp.model.SalahTimeModel;
import com.tech_sim.mymasjidapp.model.SongModel;
import com.tech_sim.mymasjidapp.model.TimeModel;
import com.tech_sim.mymasjidapp.model.UserinfoModel;
import com.tech_sim.mymasjidapp.model.ViewAudioModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PreferenceManager extends Application {
    public static final String TAG = PreferenceManager.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static PreferenceManager mInstance;
    static SharedPreferences preferences;
    static SharedPreferences.Editor prefEditor;
    public static ArrayList<Masjiidmodel> masjiidmodels;
    public static ArrayList<Masjiidmodel> userMasjidmodels;
    public static ArrayList<Masjiidmodel> visitorMasjidmodels;
    public static ArrayList<SalahTimeModel> salahTimeModels;
    public static ArrayList<ViewAudioModel> viewAudioModels;
    public static ArrayList<TimeModel> timeModels;
    public static ArrayList<SongModel> songModels;
    public static ArrayList<SalahTimeModel> models;
    public static int count_announce=0;
    public static int count_answer=0;
    public static int count_question=0;
    public static String imei;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        preferences = getSharedPreferences("MyMasjid", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        prefEditor.commit();
        masjiidmodels=new ArrayList<>();
        userMasjidmodels=new ArrayList<>();
        visitorMasjidmodels=new ArrayList<>();
        salahTimeModels=new ArrayList<>();
        viewAudioModels=new ArrayList<>();
        timeModels=new ArrayList<>();
        songModels=new ArrayList<>();
        models=new ArrayList<>();
        //  startService(new Intent(PreferenceManager.this, ScheduleService.class));
    }
    public static synchronized PreferenceManager getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    public static void setPassword(String password)
    {
        prefEditor.putString("pass", password);
        prefEditor.commit();
    }
    public static String getPassword()
    {
        return preferences.getString("pass", "");
    }
    public static void setCity(String city)
    {
        prefEditor.putString("city", city);
        prefEditor.commit();
    }
    public static String getCity()
    {
        return preferences.getString("city", "");
    }
    public static void setEmail(String email)
    {
        prefEditor.putString("email", email);
        prefEditor.commit();
    }
    public static String getEmail()
    {
        return preferences.getString("email", "");
    }
    public static void setCheck(int login){
        prefEditor.putInt("check", login);
        prefEditor.commit();
    }

    public static int getCheck()
    {
        return preferences.getInt("check",-1);
    }
    public static void setMasjid(String masjid)
    {
        prefEditor.putString("masjid", masjid);
        prefEditor.commit();
    }
    public static String getMasjid(){

        return preferences.getString("masjid","");
    }
    public static String getUserid(){

        return preferences.getString("userid","");
    }
    public static void setUserid(String userid){
        prefEditor.putString("userid", userid);
        prefEditor.commit();
    }


    public static void set(String key, String value) {
        prefEditor.putString(key, value);
        prefEditor.commit();
    }
    public static String get()
    {
        return preferences.getString("info","");
    }

    public static void setLoginMethod(String loginMethod) {
        prefEditor.remove("login");
        prefEditor.apply();
        prefEditor.putString("login", loginMethod);
        prefEditor.apply();
    }


    public static String getLoginMethod() {
        return preferences.getString("login", "");
    }
    public static void setMasjidCity(String masjidCity){
        prefEditor.putString("masjidcity", masjidCity);
        prefEditor.commit();
    }
    public static String getMasjidCity()
    {
        return preferences.getString("masjidcity","");
    }

    public static void setMasjidName(String name)
    {
        prefEditor.putString("name",name);
        prefEditor.commit();
    }
    public static String getMasjidName()
    {

        return preferences.getString("name","");

    }
    public static void setCount1(int c_anounce)
    {
        prefEditor.putInt("count1",c_anounce);
        prefEditor.commit();
    }
    public static int getCont1()
    {
        return preferences.getInt("count1",-1);

    }
    public static void setCount2(int c_answer)
    {
        prefEditor.putInt("count2",c_answer);
        prefEditor.commit();
    }
    public static int getCont2()
    {
        return preferences.getInt("count2",-1);

    }
    public static void setCount3(int c_qestion)
    {
        prefEditor.putInt("count3",c_qestion);
        prefEditor.commit();
    }
    public static int getCont3()
    {
        return preferences.getInt("count3",-1);

    }
    public static void setSong(String sonurl)
    {
        prefEditor.putString("song",sonurl);
        prefEditor.commit();
    }
    public static String getSong()
    {
        return preferences.getString("song","");
    }
    public static void setSongcheck(int ss)
    {
        prefEditor.putInt("songer",ss);
        prefEditor.commit();
    }
    public static int getSongcheck()
    {
        return preferences.getInt("songer",-1);
    }
    public static void setIMEI(String imei)
    {
        prefEditor.putString("imei",imei);
        prefEditor.commit();
    }
    public static String getImei()
    {
        return preferences.getString("imei","");
    }

    public  static void setFirst(String first){

        prefEditor.putString("first",first);
        prefEditor.commit();

    }
    public static String getFirst(){
        return preferences.getString("first","");
    }
    public  static void setLast(String lasttname){

        prefEditor.putString("last",lasttname);
        prefEditor.commit();

    }
    public static String getLast(){
        return preferences.getString("last","");
    }
    public  static void setPostalcode(String postal){

        prefEditor.putString("postal",postal);
        prefEditor.commit();

    }
    public static String getPostalcode(){
        return preferences.getString("postal","");
    }
    public  static void setaccountCity(String postal){

        prefEditor.putString("a_city",postal);
        prefEditor.commit();

    }
    public static String getaccountCity(){
        return preferences.getString("a_city","");
    }
    public  static void setState(String postal){

        prefEditor.putString("state",postal);
        prefEditor.commit();

    }
    public static String getState(){
        return preferences.getString("state","");
    }
    public  static void setmail(String mail){

        prefEditor.putString("mail",mail);
        prefEditor.commit();

    }
    public static String getmail(){
        return preferences.getString("mail","");
    }
    public  static void setPhoner(String mail){

        prefEditor.putString("phone",mail);
        prefEditor.commit();

    }
    public static String getphoner(){
        return preferences.getString("phone","");
    }
    public  static void setDay(String mail){

        prefEditor.putString("day",mail);
        prefEditor.commit();

    }
    public static String getDay(){
        return preferences.getString("day","");
    }
    public  static void setMonther(String mail){

        prefEditor.putString("month",mail);
        prefEditor.commit();

    }
    public static String getMonther(){
        return preferences.getString("month","");
    }
    public  static void setYearer(String mail){

        prefEditor.putString("year",mail);
        prefEditor.commit();

    }
    public static String getYearer(){
        return preferences.getString("year","");
    }
    public  static void setBankerCountry(String mail){

        prefEditor.putString("bankcountry",mail);
        prefEditor.commit();

    }
    public static String getBankerCountry(){
        return preferences.getString("bankcountry","");
    }
    public  static void setCurrency(String mail){

        prefEditor.putString("currency",mail);
        prefEditor.commit();

    }
    public static String getCurrency(){
        return preferences.getString("currency","");
    }
    public  static void setUserCountry(String mail){

        prefEditor.putString("user",mail);
        prefEditor.commit();

    }
    public static String getUserCountry(){
        return preferences.getString("user","");
    }
    public  static void setholderName(String mail){

        prefEditor.putString("holder",mail);
        prefEditor.commit();

    }
    public static String getholderName(){
        return preferences.getString("holder","");
    }
    public  static void setAccountNum(String mail){

        prefEditor.putString("num",mail);
        prefEditor.commit();

    }
    public static String getAccountNum(){
        return preferences.getString("num","");
    }
    public  static void setStreet(String mail){

        prefEditor.putString("street",mail);
        prefEditor.commit();

    }
    public static String getStreet(){
        return preferences.getString("street","");
    }
    public static void setNamer(String name)
    {
        prefEditor.putString("name","********"+name);
        prefEditor.commit();
    }
    public static String getNamer(){
        return preferences.getString("name","");
    }
    public static void setAccount(String account)
    {
        prefEditor.putString("account",account);
        prefEditor.commit();
    }
    public static String getAccount(){
        return preferences.getString("account","");
    }
}
