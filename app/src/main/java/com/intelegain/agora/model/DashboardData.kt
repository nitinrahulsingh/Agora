package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by suraj.m on 6/9/17.
 */
class DashboardData {
    @SerializedName("EventType")
    @Expose
    var eventType: String? = null
    @SerializedName("EventDate")
    @Expose
    var eventDate: String? = null
    @SerializedName("EventName")
    @Expose
    var eventName: String? = null
    @SerializedName("InTime")
    @Expose
    var inTime: String? = null
    @SerializedName("OutTime")
    @Expose
    var outTime: String? = null
    // new dashboard
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
        // ENENT Type meaning
        const val PRESENT = 0 // Indicate by orange color with circle
        const val ABSENT = 1 // Indicate by gray circle with dot
        const val HALF_DAY = 2 // Indicate by red color with circle
        const val LEAVE = 3 // does not mentioned yet
        const val HOLIDAY = 4 // Indicate by gray color with circle
    }
}