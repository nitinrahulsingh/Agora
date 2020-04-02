package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by suraj.m on 1/11/17.
 */
class AvailableLeave {
    @SerializedName("ID")
    @Expose
    var iD: Int? = null
    @SerializedName("LeaveType")
    @Expose
    var leaveType: Any? = null
    @SerializedName("LeaveFrom")
    @Expose
    var leaveFrom: Any? = null
    @SerializedName("LeaveTo")
    @Expose
    var leaveTo: Any? = null
    @SerializedName("LeaveDesc")
    @Expose
    var leaveDesc: Any? = null
    @SerializedName("LeaveStatus")
    @Expose
    var leaveStatus: Any? = null
    @SerializedName("AdminComments")
    @Expose
    var adminComments: Any? = null
    @SerializedName("TotalCL")
    @Expose
    var totalCL: Int? = null
    @SerializedName("TotalSL")
    @Expose
    var totalSL: Int? = null
    @SerializedName("TotalPL")
    @Expose
    var totalPL: Int? = null
    @SerializedName("TotalCO")
    @Expose
    var totalCO: Int? = null
    @SerializedName("ConsumedCL")
    @Expose
    var consumedCL: Int? = null
    @SerializedName("ConsumedSL")
    @Expose
    var consumedSL: Int? = null
    @SerializedName("ConsumedPL")
    @Expose
    var consumedPL: Int? = null
    @SerializedName("ConsumedCO")
    @Expose
    var consumedCO: Int? = null
    @SerializedName("BalanceCL")
    @Expose
    var balanceCL: Int? = null
    @SerializedName("BalanceSL")
    @Expose
    var balanceSL: Int? = null
    @SerializedName("BalancePL")
    @Expose
    var balancePL: Int? = null
    @SerializedName("BalanceCO")
    @Expose
    var balanceCO: Int? = null
    @SerializedName("TotalCLTillDate")
    @Expose
    var totalCLTillDate: Int? = null
    @SerializedName("TotalSLTillDate")
    @Expose
    var totalSLTillDate: Int? = null
    @SerializedName("TotalPLTillDate")
    @Expose
    var totalPLTillDate: Int? = null
    @SerializedName("TotalCOTillDate")
    @Expose
    var totalCOTillDate: Int? = null

}