package com.tech_sim.mymasjidapp.model;

public class AttendanceDetailModel {
    private String status;
    private String date;
    public AttendanceDetailModel(){}
    public AttendanceDetailModel(String date,String status)
    {
        this.date=date;
        this.status=status;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

}
