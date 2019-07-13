package com.tech_sim.mymasjidapp.model;

public class ViewChildItem {
    public String id;
    public  String ch_name;
    public ViewChildItem(){}
    public ViewChildItem(String id, String ch_name)
    {
        this.id=id;
        this.ch_name=ch_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCh_name() {
        return ch_name;
    }

    public void setCh_name(String ch_name) {
        this.ch_name = ch_name;
    }

}
