package com.tech_sim.mymasjidapp.model;

public class TimeModel {
    private String start_time;
    private String salah_name;
    public TimeModel(){}
    public TimeModel(String start_time, String name)
    {
        this.start_time=start_time;
        this.salah_name=name;

    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getSalah_name() {
        return salah_name;
    }

    public void setSalah_name(String salah_name) {
        this.salah_name = salah_name;
    }

}
