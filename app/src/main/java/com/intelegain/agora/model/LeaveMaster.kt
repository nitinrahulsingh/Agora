package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by suraj.m on 16/8/17.
 */
class LeaveMaster {
    @kotlin.jvm.JvmField
    @SerializedName("FiscalYear")
    @Expose
    var fiscalYear: Int? = null
    @SerializedName("LeaveType")
    @Expose
    var leaveType: Leave? = null
    @SerializedName("LeaveData")
    @Expose
    var leaveData: ArrayList<LeaveData>? = null

}