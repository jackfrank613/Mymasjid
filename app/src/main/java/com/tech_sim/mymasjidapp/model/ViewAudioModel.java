package com.tech_sim.mymasjidapp.model;

public class ViewAudioModel {

    private String a_type;
    private String a_description;
    private String a_file;
    private String a_date;
    private boolean isCheck=true;

    public String getAnnounce() {
        return announce;
    }

    public void setAnnounce(String announce) {
        this.announce = announce;
    }

    private String announce;



    private String check;

    public ViewAudioModel(){}
    public ViewAudioModel(String announce,String a_type, String a_description, String a_file, String a_date,String check)
    {
        this.a_date=a_date;
        this.a_description=a_description;
        this.a_type=a_type;
        this.a_file=a_file;
        this.check=check;
        this.announce=announce;
    }
    public String getA_type() {
        return a_type;
    }

    public void setA_type(String a_type) {
        this.a_type = a_type;
    }

    public String getA_description() {
        return a_description;
    }

    public void setA_description(String a_description) {
        this.a_description = a_description;
    }

    public String getA_file() {
        return a_file;
    }

    public void setA_file(String a_file) {
        this.a_file = a_file;
    }

    public String getA_date() {
        return a_date;
    }

    public void setA_date(String a_date) {
        this.a_date = a_date;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
    public String getCheck() {
        return check;
    }

}
