package com.tech_sim.mymasjidapp.model;

public class ChildPresentModel {
    private String date;
    private String present;
    public ChildPresentModel(){

    }
    public ChildPresentModel(String date, String present)
    {
        this.date=date;
        this.present=present;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }
}
