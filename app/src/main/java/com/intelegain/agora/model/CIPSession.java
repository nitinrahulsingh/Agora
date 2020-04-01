package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by suraj.m on 6/9/17.
 */

public class CIPSession {
    @SerializedName("CIPSessionsInfo")
    @Expose
    private String cIPSessionsInfo;

    public String getCIPSessionsInfo() {
        return cIPSessionsInfo;
    }

    public void setCIPSessionsInfo(String cIPSessionsInfo) {
        this.cIPSessionsInfo = cIPSessionsInfo;
    }

}
