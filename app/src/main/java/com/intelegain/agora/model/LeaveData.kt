package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by suraj.m on 18/7/17.
 */
class LeaveData {
    @SerializedName("ID")
    @Expose
    var iD: Int? = null
    @SerializedName("LeaveType")
    @Expose
    var leaveType: String? = null
    @SerializedName("LeaveFrom")
    @Expose
    var leaveFrom: String? = null
    @SerializedName("LeaveTo")
    @Expose
    var leaveTo: String? = null
    @SerializedName("LeaveDesc")
    @Expose
    var leaveDesc: String? = null
    @SerializedName("LeaveStatus")
    @Expose
    var leaveStatus: String? = null
    @SerializedName("AdminComments")
    @Expose
    var adminComments: String? = null
    @SerializedName("LeaveReason")
    @Expose
    var leaveReason: Any? = null

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
    val leaveId: Int
        get() = iD!!

}