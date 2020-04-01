package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by suraj.m on 18/7/17.
 */

public class LeaveData {


    @SerializedName("ID")
    @Expose
    public Integer iD;
    @SerializedName("LeaveType")
    @Expose
    public String leaveType;
    @SerializedName("LeaveFrom")
    @Expose
    public String leaveFrom;
    @SerializedName("LeaveTo")
    @Expose
    public String leaveTo;
    @SerializedName("LeaveDesc")
    @Expose
    public String leaveDesc;
    @SerializedName("LeaveStatus")
    @Expose
    public String leaveStatus;
    @SerializedName("AdminComments")
    @Expose
    public String adminComments;
    @SerializedName("LeaveReason")
    @Expose
    public Object leaveReason;

    /*public LeaveData(LeaveStatus leaveStatus, LeaveType leaveType, String leaveStartDate,
                     String leaveEndDate, String leaveTitleText, String leaveDetailText) {
        LeaveStatus = leaveStatus;
        LeaveType = leaveType;
        LeaveFromDate = leaveStartDate;
        LeaveToDate = leaveEndDate;
        LeaveTitleText = leaveTitleText;
        LeaveDetailText = leaveDetailText;
    }*/

/*    public com.intelgain.constant.LeaveStatus getLeaveStatus() {
        return LeaveStatus;
    }

    public com.intelgain.constant.LeaveType getLeaveType() {
        return LeaveType;
    }*/


    public int getLeaveId() {
        return this.iD;
    }

    public String getLeaveFrom() {
        return this.leaveFrom;
    }

    public void setLeaveFrom(String leaveFromDate) {
        this.leaveFrom = leaveFromDate;
    }

    public void setLeaveTo(String leaveToDate) {
        this.leaveTo = leaveToDate;
    }

    public String getLeaveTo() {
        return this.leaveTo;
    }

    public String getLeaveStatus() {
        return this.leaveStatus;
    }

    public String getLeaveType() {
        return this.leaveType;
    }

    public String getLeaveDesc() {
        return leaveDesc;
    }


}
