package com.intelegain.agora.model;

/**
 * Created by suraj.m on 1/11/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AppliedLeaveDetails {

    @SerializedName("FiscalYear")
    @Expose
    public int fiscalYear;
    @SerializedName("LeaveData")
    @Expose
    public ArrayList<LeaveData> leaveData = null;


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
        @SerializedName("TotalCL")
        @Expose
        public Integer totalCL;
        @SerializedName("TotalSL")
        @Expose
        public Integer totalSL;
        @SerializedName("TotalPL")
        @Expose
        public Integer totalPL;
        @SerializedName("TotalCO")
        @Expose
        public Integer totalCO;
        @SerializedName("ConsumedCL")
        @Expose
        public Integer consumedCL;
        @SerializedName("ConsumedSL")
        @Expose
        public Integer consumedSL;
        @SerializedName("ConsumedPL")
        @Expose
        public Integer consumedPL;
        @SerializedName("ConsumedCO")
        @Expose
        public Integer consumedCO;
        @SerializedName("BalanceCL")
        @Expose
        public Integer balanceCL;
        @SerializedName("BalanceSL")
        @Expose
        public Integer balanceSL;
        @SerializedName("BalancePL")
        @Expose
        public Integer balancePL;
        @SerializedName("BalanceCO")
        @Expose
        public Integer balanceCO;
        @SerializedName("TotalCLTillDate")
        @Expose
        public Integer totalCLTillDate;
        @SerializedName("TotalSLTillDate")
        @Expose
        public Integer totalSLTillDate;
        @SerializedName("TotalPLTillDate")
        @Expose
        public Integer totalPLTillDate;
        @SerializedName("TotalCOTillDate")
        @Expose
        public Integer totalCOTillDate;

        public int getLeaveId() {
            return this.iD;
        }

        public String getLeaveFrom() {
            return leaveFrom;
        }

        public void setLeaveFrom(String leaveFrom) {
            this.leaveFrom = leaveFrom;
        }

        public String getLeaveTo() {
            return leaveTo;
        }

        public void setLeaveTo(String leaveTo) {
            this.leaveTo = leaveTo;
        }

        public String getLeaveStatus() {
            return leaveStatus;
        }

        public String getLeaveType() {
            return leaveType;
        }

        public String getLeaveDesc() {
            return leaveDesc;
        }


    }
}