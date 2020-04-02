package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by suraj.m on 29/7/17.
 */
class AttendanceData {
    //    public int getViewType() {
//        return ViewType;
//    }
//
//    private int ViewType;
//
//    public AttendanceData(int iViewType) {
//        this.ViewType = iViewType;
//    }
//{"EmpId":"2562","Startdate":"05/11/2017","EndDate":"08/09/2017"}
    @SerializedName("AttInTime")
    @Expose
    var attInTime: String? = null
    @SerializedName("AttOutTime")
    @Expose
    var attOutTime: String? = null
    @SerializedName("AttStatus")
    @Expose
    var attStatus: String? = null
    @SerializedName("AttendanceDate")
    @Expose
    var attendanceDate: String? = null
    @SerializedName("Date")
    @Expose
    var date: String? = null
    @SerializedName("Day")
    @Expose
    var day: String? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null
    @SerializedName("HolidayImageUrl")
    @Expose
    var holidayImageUrl: String? = null
    @SerializedName("ImageId")
    @Expose
    var imageId: Int? = null
    @SerializedName("RowBgColor")
    @Expose
    var rowBgColor: String? = null
    @SerializedName("RowTextColor")
    @Expose
    var rowTextColor: String? = null
    @SerializedName("Timesheethours")
    @Expose
    var timesheethours: String? = null
    @SerializedName("ViewType")
    @Expose
    var viewType: Int? = null
    @SerializedName("Workinghours")
    @Expose
    var workinghours: String? = null

    companion object {
        // View types
        const val NORMAL = 0
        const val LEAVE = 1
        const val HOLIDAY = 2
    }
}