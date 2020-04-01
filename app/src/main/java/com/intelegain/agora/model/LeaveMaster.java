package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by suraj.m on 16/8/17.
 */

public class LeaveMaster {

    @SerializedName("FiscalYear")
    @Expose
    public Integer fiscalYear;

    @SerializedName("LeaveType")
    @Expose
    private Leave leaveType;
    @SerializedName("LeaveData")
    @Expose
    private ArrayList<LeaveData> leaveData = null;

    public Leave getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(Leave leaveType) {
        this.leaveType = leaveType;
    }

    public ArrayList<LeaveData> getLeaveData() {
        return leaveData;
    }

    public void setLeaveData(ArrayList<LeaveData> leaveData) {
        this.leaveData = leaveData;
    }
}
