package com.tech_sim.mymasjidapp.model;

public class Masjiidmodel {

    private String id;
    private String name;
    private String address;
    private String longitude;
    private String latitude;

    private String city;

    public Masjiidmodel(){}
    public Masjiidmodel(String id, String name, String address, String longitude, String latitude, String city)
    {
        this.id=id;
        this.name=name;
        this.address=address;
        this.longitude=longitude;
        this.latitude=latitude;
        this.city=city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
