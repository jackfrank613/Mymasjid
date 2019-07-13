package com.tech_sim.mymasjidapp.model;

public class UserQuestionModel {
    private String name;
    private String question;
    private String audio;
    private String date;
    private String user_id;
    private String staus;
    private String id;
    public UserQuestionModel(){

    }
    public UserQuestionModel(String name, String question, String audio, String date, String user_id,String staus,String id)
    {
        this.question=question;
        this.audio=audio;
        this.date=date;
        this.name=name;
        this.user_id=user_id;
        this.staus=staus;
        this.id=id;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
