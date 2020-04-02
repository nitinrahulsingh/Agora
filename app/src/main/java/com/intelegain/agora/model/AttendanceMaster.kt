package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by suraj.m on 18/8/17.
 */
class AttendanceMaster {
    @SerializedName("AttendanceData")
    @Expose
    var attendanceData: ArrayList<AttendanceData>? = null

}