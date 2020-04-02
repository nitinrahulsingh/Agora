package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by suraj.m on 10/8/17.
 */
class Leave {
    @SerializedName("ID")
    @Expose
    var iD = 0
    @SerializedName("LeaveType")
    @Expose
    var leaveType: String? = null
    @SerializedName("LeaveDesc")
    @Expose
    var leaveDesc: String? = null
    @SerializedName("TotalCL")
    @Expose
    var totalCL = 0
    @SerializedName("TotalSL")
    @Expose
    var totalSL = 0
    @SerializedName("TotalPL")
    @Expose
    var totalPL = 0
    @SerializedName("TotalCO")
    @Expose
    var totalCO = 0
    @SerializedName("ConsumedCL")
    @Expose
    var consumedCL = 0
    @SerializedName("ConsumedSL")
    @Expose
    var consumedSL = 0
    @SerializedName("ConsumedPL")
    @Expose
    var consumedPL = 0
    @SerializedName("ConsumedCO")
    @Expose
    var consumedCO = 0
    @SerializedName("BalanceCL")
    @Expose
    var balanceCL = 0
    @SerializedName("BalanceSL")
    @Expose
    var balanceSL = 0
    @SerializedName("BalancePL")
    @Expose
    var balancePL = 0
    @SerializedName("BalanceCO")
    @Expose
    var balanceCO = 0
    @SerializedName("TotalCLTillDate")
    @Expose
    var totalCLTillDate = 0
    @SerializedName("TotalSLTillDate")
    @Expose
    var totalSLTillDate = 0
    @SerializedName("TotalPLTillDate")
    @Expose
    var totalPLTillDate = 0
    //    @SerializedName("ID")
    @SerializedName("TotalCOTillDate")
    @Expose
    var totalCOTillDate = 0

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