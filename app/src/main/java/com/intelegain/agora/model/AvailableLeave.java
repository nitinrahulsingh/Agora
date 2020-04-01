package com.intelegain.agora.model;

/**
 * Created by suraj.m on 1/11/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AvailableLeave {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("LeaveType")
    @Expose
    private Object leaveType;
    @SerializedName("LeaveFrom")
    @Expose
    private Object leaveFrom;
    @SerializedName("LeaveTo")
    @Expose
    private Object leaveTo;
    @SerializedName("LeaveDesc")
    @Expose
    private Object leaveDesc;
    @SerializedName("LeaveStatus")
    @Expose
    private Object leaveStatus;
    @SerializedName("AdminComments")
    @Expose
    private Object adminComments;
    @SerializedName("TotalCL")
    @Expose
    private Integer totalCL;
    @SerializedName("TotalSL")
    @Expose
    private Integer totalSL;
    @SerializedName("TotalPL")
    @Expose
    private Integer totalPL;
    @SerializedName("TotalCO")
    @Expose
    private Integer totalCO;
    @SerializedName("ConsumedCL")
    @Expose
    private Integer consumedCL;
    @SerializedName("ConsumedSL")
    @Expose
    private Integer consumedSL;
    @SerializedName("ConsumedPL")
    @Expose
    private Integer consumedPL;
    @SerializedName("ConsumedCO")
    @Expose
    private Integer consumedCO;
    @SerializedName("BalanceCL")
    @Expose
    private Integer balanceCL;
    @SerializedName("BalanceSL")
    @Expose
    private Integer balanceSL;
    @SerializedName("BalancePL")
    @Expose
    private Integer balancePL;
    @SerializedName("BalanceCO")
    @Expose
    private Integer balanceCO;
    @SerializedName("TotalCLTillDate")
    @Expose
    private Integer totalCLTillDate;
    @SerializedName("TotalSLTillDate")
    @Expose
    private Integer totalSLTillDate;
    @SerializedName("TotalPLTillDate")
    @Expose
    private Integer totalPLTillDate;
    @SerializedName("TotalCOTillDate")
    @Expose
    private Integer totalCOTillDate;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Object getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(Object leaveType) {
        this.leaveType = leaveType;
    }

    public Object getLeaveFrom() {
        return leaveFrom;
    }

    public void setLeaveFrom(Object leaveFrom) {
        this.leaveFrom = leaveFrom;
    }

    public Object getLeaveTo() {
        return leaveTo;
    }

    public void setLeaveTo(Object leaveTo) {
        this.leaveTo = leaveTo;
    }

    public Object getLeaveDesc() {
        return leaveDesc;
    }

    public void setLeaveDesc(Object leaveDesc) {
        this.leaveDesc = leaveDesc;
    }

    public Object getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(Object leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public Object getAdminComments() {
        return adminComments;
    }

    public void setAdminComments(Object adminComments) {
        this.adminComments = adminComments;
    }

    public Integer getTotalCL() {
        return totalCL;
    }

    public void setTotalCL(Integer totalCL) {
        this.totalCL = totalCL;
    }

    public Integer getTotalSL() {
        return totalSL;
    }

    public void setTotalSL(Integer totalSL) {
        this.totalSL = totalSL;
    }

    public Integer getTotalPL() {
        return totalPL;
    }

    public void setTotalPL(Integer totalPL) {
        this.totalPL = totalPL;
    }

    public Integer getTotalCO() {
        return totalCO;
    }

    public void setTotalCO(Integer totalCO) {
        this.totalCO = totalCO;
    }

    public Integer getConsumedCL() {
        return consumedCL;
    }

    public void setConsumedCL(Integer consumedCL) {
        this.consumedCL = consumedCL;
    }

    public Integer getConsumedSL() {
        return consumedSL;
    }

    public void setConsumedSL(Integer consumedSL) {
        this.consumedSL = consumedSL;
    }

    public Integer getConsumedPL() {
        return consumedPL;
    }

    public void setConsumedPL(Integer consumedPL) {
        this.consumedPL = consumedPL;
    }

    public Integer getConsumedCO() {
        return consumedCO;
    }

    public void setConsumedCO(Integer consumedCO) {
        this.consumedCO = consumedCO;
    }

    public Integer getBalanceCL() {
        return balanceCL;
    }

    public void setBalanceCL(Integer balanceCL) {
        this.balanceCL = balanceCL;
    }

    public Integer getBalanceSL() {
        return balanceSL;
    }

    public void setBalanceSL(Integer balanceSL) {
        this.balanceSL = balanceSL;
    }

    public Integer getBalancePL() {
        return balancePL;
    }

    public void setBalancePL(Integer balancePL) {
        this.balancePL = balancePL;
    }

    public Integer getBalanceCO() {
        return balanceCO;
    }

    public void setBalanceCO(Integer balanceCO) {
        this.balanceCO = balanceCO;
    }

    public Integer getTotalCLTillDate() {
        return totalCLTillDate;
    }

    public void setTotalCLTillDate(Integer totalCLTillDate) {
        this.totalCLTillDate = totalCLTillDate;
    }

    public Integer getTotalSLTillDate() {
        return totalSLTillDate;
    }

    public void setTotalSLTillDate(Integer totalSLTillDate) {
        this.totalSLTillDate = totalSLTillDate;
    }

    public Integer getTotalPLTillDate() {
        return totalPLTillDate;
    }

    public void setTotalPLTillDate(Integer totalPLTillDate) {
        this.totalPLTillDate = totalPLTillDate;
    }

    public Integer getTotalCOTillDate() {
        return totalCOTillDate;
    }

    public void setTotalCOTillDate(Integer totalCOTillDate) {
        this.totalCOTillDate = totalCOTillDate;
    }

}

