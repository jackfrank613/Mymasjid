package com.tech_sim.mymasjidapp.model;

public class ChildDateModel {
    private String date;
    private String detail;
    public ChildDateModel(){}
    public ChildDateModel(String date, String detail)
    {
        this.date=date;
        this.detail=detail;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
