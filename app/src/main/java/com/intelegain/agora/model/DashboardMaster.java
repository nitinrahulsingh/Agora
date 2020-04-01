package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by suraj.m on 6/9/17.
 */

public class DashboardMaster {
    @SerializedName("DashboardData")
    @Expose
    private ArrayList<AttendanceData> dashboardAtttendanceData = null;
    @SerializedName("OccassionsData")
    @Expose
    private ArrayList<OccassionsData> occassionsData = null;
    @SerializedName("News")
    @Expose
    private ArrayList<News> news = null;
    @SerializedName("CIPSessions")
    @Expose
    private ArrayList<CIPSession> CIPSessions = null;


    public ArrayList<AttendanceData> getDashboardData() {
        return dashboardAtttendanceData;
    }

    public void setDashboardData(ArrayList<AttendanceData> dashboardData) {
        this.dashboardAtttendanceData = dashboardData;
    }

    public ArrayList<OccassionsData> getOccassionsData() {
        return occassionsData;
    }

    public void setOccassionsData(ArrayList<OccassionsData> occassionsData) {
        this.occassionsData = occassionsData;
    }

    public ArrayList<News> getNews() {
        return news;
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }

    public ArrayList<CIPSession> getCIPSessions() {
        return CIPSessions;
    }

    public void setCIPSessions(ArrayList<CIPSession> cIPSessions) {
        this.CIPSessions = cIPSessions;
    }

}
