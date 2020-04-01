package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by suraj.m on 6/9/17.
 */

public class DashboardData {
    // ENENT Type meaning
    public final static int PRESENT = 0;    // Indicate by orange color with circle
    public final static int ABSENT = 1;     // Indicate by gray circle with dot
    public final static int HALF_DAY = 2;   // Indicate by red color with circle
    public final static int LEAVE = 3;      // does not mentioned yet
    public final static int HOLIDAY = 4;    // Indicate by gray color with circle

    @SerializedName("EventType")
    @Expose
    private String eventType;
    @SerializedName("EventDate")
    @Expose
    private String eventDate;
    @SerializedName("EventName")
    @Expose
    private String eventName;
    @SerializedName("InTime")
    @Expose
    private String inTime;
    @SerializedName("OutTime")
    @Expose
    private String outTime;


    // new dashboard
    @SerializedName("AttInTime")
    @Expose
    public String attInTime;
    @SerializedName("AttOutTime")
    @Expose
    public String attOutTime;
    @SerializedName("AttStatus")
    @Expose
    public String attStatus;
    @SerializedName("AttendanceDate")
    @Expose
    public String attendanceDate;
    @SerializedName("Date")
    @Expose
    public String date;
    @SerializedName("Day")
    @Expose
    public String day;
    @SerializedName("Description")
    @Expose
    public String description;
    @SerializedName("HolidayImageUrl")
    @Expose
    public String holidayImageUrl;
    @SerializedName("ImageId")
    @Expose
    public Integer imageId;
    @SerializedName("RowBgColor")
    @Expose
    public String rowBgColor;
    @SerializedName("RowTextColor")
    @Expose
    public String rowTextColor;
    @SerializedName("Timesheethours")
    @Expose
    public String timesheethours;
    @SerializedName("ViewType")
    @Expose
    public Integer viewType;
    @SerializedName("Workinghours")
    @Expose
    public String workinghours;


    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

}
