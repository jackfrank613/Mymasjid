package com.tech_sim.mymasjidapp.model;

public class UserPayModel {

    private String childid;
    private String childname;
    private String age;
    private String date;
    public UserPayModel(){}
    public UserPayModel(String childid,String childname)
    {
        this.childname=childname;
        this.childid=childid;
    }
    public String getChildname() {
        return childname;
    }

    public void setChildname(String childname) {
        this.childname = childname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getChildid() {
        return childid;
    }
    public void setChildid(String childid) {
        this.childid = childid;
    }

}
