package com.tech_sim.mymasjidapp.model;

public class MarkChildModel {
    private String child_id;
    private String child_name;
    public MarkChildModel(){}
    public MarkChildModel(String child_id, String child_name)
    {
        this.child_id=child_id;
        this.child_name=child_name;
    }
    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

}
