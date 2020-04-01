package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by suraj.m on 10/8/17.
 */

public class Leave {

    @SerializedName("ID")
    @Expose
    private int iD;
    @SerializedName("LeaveType")
    @Expose
    private String leaveType;
    @SerializedName("LeaveDesc")
    @Expose
    private String leaveDesc;
    @SerializedName("TotalCL")
    @Expose
    private int totalCL;
    @SerializedName("TotalSL")
    @Expose
    private int totalSL;
    @SerializedName("TotalPL")
    @Expose
    private int totalPL;
    @SerializedName("TotalCO")
    @Expose
    private int totalCO;
    @SerializedName("ConsumedCL")
    @Expose
    private int consumedCL;
    @SerializedName("ConsumedSL")
    @Expose
    private int consumedSL;
    @SerializedName("ConsumedPL")
    @Expose
    private int consumedPL;
    @SerializedName("ConsumedCO")
    @Expose
    private int consumedCO;
    @SerializedName("BalanceCL")
    @Expose
    private int balanceCL;
    @SerializedName("BalanceSL")
    @Expose
    private int balanceSL;
    @SerializedName("BalancePL")
    @Expose
    private int balancePL;
    @SerializedName("BalanceCO")
    @Expose
    private int balanceCO;
    @SerializedName("TotalCLTillDate")
    @Expose
    private int totalCLTillDate;
    @SerializedName("TotalSLTillDate")
    @Expose
    private int totalSLTillDate;
    @SerializedName("TotalPLTillDate")
    @Expose
    private int totalPLTillDate;
    @SerializedName("TotalCOTillDate")
    @Expose
    private int totalCOTillDate;

    public int getID() {
        return iD;
    }

    public void setID(int iD) {
        this.iD = iD;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getLeaveDesc() {
        return leaveDesc;
    }

    public void setLeaveDesc(String leaveDesc) {
        this.leaveDesc = leaveDesc;
    }

    public int getTotalCL() {
        return totalCL;
    }

    public void setTotalCL(int totalCL) {
        this.totalCL = totalCL;
    }

    public int getTotalSL() {
        return totalSL;
    }

    public void setTotalSL(int totalSL) {
        this.totalSL = totalSL;
    }

    public int getTotalPL() {
        return totalPL;
    }

    public void setTotalPL(int totalPL) {
        this.totalPL = totalPL;
    }

    public int getTotalCO() {
        return totalCO;
    }

    public void setTotalCO(int totalCO) {
        this.totalCO = totalCO;
    }

    public int getConsumedCL() {
        return consumedCL;
    }

    public void setConsumedCL(int consumedCL) {
        this.consumedCL = consumedCL;
    }

    public int getConsumedSL() {
        return consumedSL;
    }

    public void setConsumedSL(int consumedSL) {
        this.consumedSL = consumedSL;
    }

    public int getConsumedPL() {
        return consumedPL;
    }

    public void setConsumedPL(int consumedPL) {
        this.consumedPL = consumedPL;
    }

    public int getConsumedCO() {
        return consumedCO;
    }

    public void setConsumedCO(int consumedCO) {
        this.consumedCO = consumedCO;
    }

    public int getBalanceCL() {
        return balanceCL;
    }

    public void setBalanceCL(int balanceCL) {
        this.balanceCL = balanceCL;
    }

    public int getBalanceSL() {
        return balanceSL;
    }

    public void setBalanceSL(int balanceSL) {
        this.balanceSL = balanceSL;
    }

    public int getBalancePL() {
        return balancePL;
    }

    public void setBalancePL(int balancePL) {
        this.balancePL = balancePL;
    }

    public int getBalanceCO() {
        return balanceCO;
    }

    public void setBalanceCO(int balanceCO) {
        this.balanceCO = balanceCO;
    }

    public int getTotalCLTillDate() {
        return totalCLTillDate;
    }

    public void setTotalCLTillDate(int totalCLTillDate) {
        this.totalCLTillDate = totalCLTillDate;
    }

    public int getTotalSLTillDate() {
        return totalSLTillDate;
    }

    public void setTotalSLTillDate(int totalSLTillDate) {
        this.totalSLTillDate = totalSLTillDate;
    }

    public int getTotalPLTillDate() {
        return totalPLTillDate;
    }

    public void setTotalPLTillDate(int totalPLTillDate) {
        this.totalPLTillDate = totalPLTillDate;
    }

    public int getTotalCOTillDate() {
        return totalCOTillDate;
    }

    public void setTotalCOTillDate(int totalCOTillDate) {
        this.totalCOTillDate = totalCOTillDate;
    }





//    @SerializedName("ID")
//    @Expose
//    public Integer iD;
//    @SerializedName("Leave")
//    @Expose
//    public String leaveType;
//    @SerializedName("LeaveFrom")
//    @Expose
//    public Object leaveFrom;
//    @SerializedName("LeaveTo")
//    @Expose
//    public Object leaveTo;
//    @SerializedName("LeaveDesc")
//    @Expose
//    public String leaveDesc;
//    @SerializedName("LeaveStatus")
//    @Expose
//    public Object leaveStatus;
//    @SerializedName("AdminComments")
//    @Expose
//    public Object adminComments;
//    @SerializedName("TotalCL")
//    @Expose
//    public Integer totalCL;
//    @SerializedName("TotalSL")
//    @Expose
//    public Integer totalSL;
//    @SerializedName("TotalPL")
//    @Expose
//    public Integer totalPL;
//    @SerializedName("TotalCO")
//    @Expose
//    public Integer totalCO;
//    @SerializedName("ConsumedCL")
//    @Expose
//    public Integer consumedCL;
//    @SerializedName("ConsumedSL")
//    @Expose
//    public Integer consumedSL;
//    @SerializedName("ConsumedPL")
//    @Expose
//    public Integer consumedPL;
//    @SerializedName("ConsumedCO")
//    @Expose
//    public Integer consumedCO;
//    @SerializedName("BalanceCL")
//    @Expose
//    public Integer balanceCL;
//    @SerializedName("BalanceSL")
//    @Expose
//    public Integer balanceSL;
//    @SerializedName("BalancePL")
//    @Expose
//    public Integer balancePL;
//    @SerializedName("BalanceCO")
//    @Expose
//    public Integer balanceCO;
//    @SerializedName("TotalCLTillDate")
//    @Expose
//    public Integer totalCLTillDate;
//    @SerializedName("TotalSLTillDate")
//    @Expose
//    public Integer totalSLTillDate;
//    @SerializedName("TotalPLTillDate")
//    @Expose
//    public Integer totalPLTillDate;
//    @SerializedName("TotalCOTillDate")
//    @Expose
//    public Integer totalCOTillDate;
}
