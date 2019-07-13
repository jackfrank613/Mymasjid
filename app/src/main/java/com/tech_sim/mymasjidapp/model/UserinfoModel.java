package com.tech_sim.mymasjidapp.model;

public class UserinfoModel {
    private String id;
    private String f_name;
    private String l_name;
    private String email;
    private String house;
    private String phone;
    public UserinfoModel(){}
    public UserinfoModel(String id,String f_name,String l_name,String email,String house,String phone)
    {
        this.id=id;
        this.f_name=f_name;
        this.l_name=l_name;
        this.email=email;
        this.house=house;
        this.phone=phone;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
