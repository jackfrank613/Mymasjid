package com.tech_sim.mymasjidapp.utils;

import android.app.Activity;
import android.provider.DocumentsContract;

import com.kaopiz.kprogresshud.KProgressHUD;


public class Constants {
    //Root Url of Masjid Panel
    private static final String ROOT_URL_Masjid ="http://mymasjid.space/mobileApp/admin/";
    public static final  String URL_Phone_Verify=ROOT_URL_Masjid+"phone_verify";
    public static final String URL_Forgot_password=ROOT_URL_Masjid+"forgot_password";
    //Register Mosque Api URL
    public static final String URL_REGISTER = ROOT_URL_Masjid + "register_mosque";
    //Register User Api URL
    public static final String URL_REGISTER_USER = ROOT_URL_Masjid + "register_user";
    public static final  String URL_SEARCH_MASJID=ROOT_URL_Masjid+"masjid_search";
    //Login Mosque Api URL
    public static final String URL_LOGIN_MASJID = ROOT_URL_Masjid + "login_masjid";
    //Login User Api URL
    public static final String URL_LOGIN_USER = ROOT_URL_Masjid + "login_user";
    //Add Salah Time Mosque Api URL
    public static final String URL_ADD_SALAH_TIME_MASJID = ROOT_URL_Masjid + "add_salah_masjid/";
    //Add Announcement Mosque Api URL
    public static final String URL_ADD_ANNOUNCEMENT_MASJID = ROOT_URL_Masjid + "make_announcement_masjid";
    //Add Child Mosque Api URL
    public static final String URL_ADD_Child_MASJID = ROOT_URL_Masjid + "add_child_masjid/";
    //Give Answer Mosque Api URL
    public static final String URL_GIVE_ANSWER_MASJID = ROOT_URL_Masjid + "give_answer_masjid";
    //Add Committee Mosque Api URL
    public static final String URL_ADD_COMMITTEE_MASJID = ROOT_URL_Masjid + "add_masjid_committee";
    //Add Expense Mosque Api URL
    public static final String URL_ADD_EXPENSE_MASJID = ROOT_URL_Masjid + "expense_masjid";
    //Get Masjid Salah Time URL
    public static final String URL_VIEW_SALAH_TIME_MASJID = ROOT_URL_Masjid + "getSalahMasjid";
    //GET MASJID ANNOUNCEMENT URL
    public static final String URL_VIEW_ANOUNCEMNT_MASJID = ROOT_URL_Masjid + "getAnnouncementsMasjid";
    //GET MASJID CHILD URL
    public static final String URL_VIEW_CHILD_MASJID = ROOT_URL_Masjid + "getChildrenMasjid";
    public static final String cash_url=ROOT_URL_Masjid+"cash_for_children";
    //SETTINGS MASJID URL
    public static final String URL_UPDATE_SETTINGS = ROOT_URL_Masjid + "updateMasjidSetting";
    //Add Child Report Masjid URL
    public static final String URL_CHIL_REPORT_MASJID = ROOT_URL_Masjid + "childReportMasjid";

    /////Get Question Masjid URL
    public static final String URL_GET_QUESTION_MASJID = ROOT_URL_Masjid + "getMasjidQuestions";

    //Delete Announcement URL
    public static final String URL_DELETE_ANNONCEMENT = ROOT_URL_Masjid + "deleteAnnouncement";

    public static final String URL_INFO_UPADTE=ROOT_URL_Masjid+"user_info_update";
    //GET SINGLE SALAH URL
    public static final String URL_GET_SALAH_SINGLE = ROOT_URL_Masjid + "getSingleSalah";
    //VIEW MASJID COMMITTE URL
    public static final String URL_VIEW_MASJID_COMMITTEE = ROOT_URL_Masjid + "getMasjidCommittee";

    //daily attendance api URL
    public static final String URL_CHILD_DAILY_ATTENDANCE=ROOT_URL_Masjid+"dailyChildAttendance";

    //Get Single Salah URL
    public static final String URL_GET_SINGLE_CHILD=ROOT_URL_Masjid+"getSingleChild";


    public static final String URL_GET_ABOUT_US=ROOT_URL_Masjid+"about_us";
    public static final String check_url=ROOT_URL_Masjid+"question_see_masjid";


    public static final String URL_SUBMIT_ATTENDANCE=ROOT_URL_Masjid+"addChildrenAttendanceMasjid";


    public static final String URL_GET_CHILD_ATTENDANCE_HISTORY=ROOT_URL_Masjid+"getChildAttendanceHistory";


    public static final String URL_GET_Masjid_SETTING=ROOT_URL_Masjid+"getMasjidSettingData";

    public static final String URL_Salah_time=ROOT_URL_Masjid+"getSalahMasjid";
    public static final String URL_Salah_audio=ROOT_URL_Masjid+"getAnnouncementsMasjid";
    public static final String URL_USER_Quetion=ROOT_URL_Masjid+"getMasjidQuestions";
    public static final String URL_ANSWER=ROOT_URL_Masjid+"give_answer_masjid";
    public static final String customer_url= ROOT_URL_Masjid+"customer_create";
    public static final String transaction_url=ROOT_URL_Masjid+"transaction";
    public static final String User_pay_url=ROOT_URL_Masjid+"user_regiser_pay";
    public static final String User_get_children= ROOT_URL_Masjid+"get_user_children";
    public static final String User_get_payfee_url=ROOT_URL_Masjid+"getchildren_user";
    public static final String Transaction_Children=ROOT_URL_Masjid+"transaction_for_children";
    public static int check_pay=1;
    public static String saveSong="http://mymasjid.space/mobileApp/uploads/athanaudio/azan1.mp3";

    public static String masid_name="";
//    public static String vistor_masid_name="";
    public static String user_masjid_name;
    public static  String masjid_id="";
    public static  String visitor_masjidname="";
    public static  String res_masjid_id="";
    public static String child_id="";
    public static String search="";
    public static String city="";
    public static String song="http://mymasjid.space/mobileApp/uploads/athanaudio/azan1.mp3";
    public KProgressHUD kpHUD;
    public static boolean pay_check=true;

    //
    public static String firstname="";
    public static String surname="";
    public static String postcode_list="";
    public static String house="";
    public static String email_code="";
    public static String pssword_code="";
    public static String confirmpass="";
    public static String pay="";
    public static String phone="";
    public static boolean check_veryfiy=false;
    public static boolean check_noti=false;
    public static String delete_url=ROOT_URL_Masjid+"delete_expense_incoming";
public Activity activity;

    public Constants(Activity activity)
    {
        this.activity=activity;
        kpHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
//                .setLabel("Login")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
    }


//


}
