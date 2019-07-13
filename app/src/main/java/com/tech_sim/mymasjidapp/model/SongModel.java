package com.tech_sim.mymasjidapp.model;

public class SongModel {

    private String s_song;
    private String m_song;
    private String l_song;

    public SongModel(){}
    public SongModel(String s_song, String m_song, String l_song)
    {
        this.s_song=s_song;
        this.m_song=m_song;
        this.l_song=l_song;
    }

    public String getS_song() {
        return s_song;
    }

    public void setS_song(String s_song) {
        this.s_song = s_song;
    }

    public String getM_song() {
        return m_song;
    }

    public void setM_song(String m_song) {
        this.m_song = m_song;
    }

    public String getL_song() {
        return l_song;
    }

    public void setL_song(String l_song) {
        this.l_song = l_song;
    }


}
