package com.tech_sim.mymasjidapp.model;

public class SalahTimeModel {
    private int number;
    private String s_name;
    private String s_time;
    private String j_time;
    private String e_time;

    public SalahTimeModel(){}
    public SalahTimeModel(int number, String s_name, String s_time, String j_time, String e_time){
     this.number=number;
     this.s_name=s_name;
     this.s_time=s_time;
     this.j_time=j_time;
     this.e_time=e_time;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getS_time() {
        return s_time;
    }

    public void setS_time(String s_time) {
        this.s_time = s_time;
    }

    public String getJ_time() {
        return j_time;
    }

    public void setJ_time(String j_time) {
        this.j_time = j_time;
    }

    public String getE_time() {
        return e_time;
    }

    public void setE_time(String e_time) {
        this.e_time = e_time;
    }



}
