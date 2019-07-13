package com.tech_sim.mymasjidapp.model;

public class ViewComitteeModel {
    private String type;
    private String name;
    private String number;

    public ViewComitteeModel(){}
    public ViewComitteeModel(String type, String name, String number){
        this.name=name;
        this.type=type;
        this.number=number;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


}
