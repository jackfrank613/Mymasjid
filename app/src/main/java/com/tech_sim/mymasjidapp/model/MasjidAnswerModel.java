package com.tech_sim.mymasjidapp.model;

public class MasjidAnswerModel {
    private String masjid_name;
    private String date;
    private String audio;
    private String answer;
    private String an_answer;
    private String an_date;



    private String state_id;


    private String status;
    public MasjidAnswerModel(){}
    public MasjidAnswerModel(String masjid_name, String date, String audio, String answer, String an_answer, String an_date,String status,String state_id)
    {
        this.masjid_name=masjid_name;
        this.date=date;
        this.audio=audio;
        this.answer=answer;
        this.an_answer=an_answer;
        this.an_date=an_date;
        this.status=status;
        this.state_id=state_id;
    }

    public String getMasjid_name() {
        return masjid_name;
    }

    public void setMasjid_name(String masjid_name) {
        this.masjid_name = masjid_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAn_answer() {
        return an_answer;
    }

    public void setAn_answer(String an_answer) {
        this.an_answer = an_answer;
    }

    public String getAn_date() {
        return an_date;
    }

    public void setAn_date(String an_date) {
        this.an_date = an_date;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }
}
