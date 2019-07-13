package com.tech_sim.mymasjidapp.model;

public class PagerModel {
    String id,descriptionOne,descriptionTwo,descriptionThree;

    public PagerModel(String id, String descriptionOne, String descriptionTwo, String descriptionThree) {
        this.id = id;
        this.descriptionOne = descriptionOne;
        this.descriptionTwo = descriptionTwo;
        this.descriptionThree = descriptionThree;
    }

    public String getId() {
        return id;
    }

    public String getDescriptionOne() {
        return descriptionOne;
    }
    public String getDescriptionTwo() {
        return descriptionTwo;
    }

    public String getDescriptionThree() {
        return descriptionThree;
    }

}
