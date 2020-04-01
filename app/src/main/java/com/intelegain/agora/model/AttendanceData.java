package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by suraj.m on 29/7/17.
 */

public class AttendanceData {

    // View types
    public final static int NORMAL = 0;
    public final static int LEAVE = 1;
    public final static int HOLIDAY = 2;

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
    public String attInTime;
    @SerializedName("AttOutTime")
    @Expose
    public String attOutTime;
    @SerializedName("AttStatus")
    @Expose
    private String attStatus;
    @SerializedName("AttendanceDate")
    @Expose
    private String attendanceDate;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("Day")
    @Expose
    private String day;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("HolidayImageUrl")
    @Expose
    private String holidayImageUrl;
    @SerializedName("ImageId")
    @Expose
    private Integer imageId;
    @SerializedName("RowBgColor")
    @Expose
    private String rowBgColor;
    @SerializedName("RowTextColor")
    @Expose
    private String rowTextColor;
    @SerializedName("Timesheethours")
    @Expose
    private String timesheethours;
    @SerializedName("ViewType")
    @Expose
    private Integer viewType;
    @SerializedName("Workinghours")
    @Expose
    private String workinghours;

    public String getAttInTime() {
        return attInTime;
    }

    public void setAttInTime(String attInTime) {
        this.attInTime = attInTime;
    }

    public String getAttOutTime() {
        return attOutTime;
    }

    public void setAttOutTime(String attOutTime) {
        this.attOutTime = attOutTime;
    }

    public String getAttStatus() {
        return attStatus;
    }

    public void setAttStatus(String attStatus) {
        this.attStatus = attStatus;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHolidayImageUrl() {
        return holidayImageUrl;
    }

    public void setHolidayImageUrl(String holidayImageUrl) {
        this.holidayImageUrl = holidayImageUrl;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getRowBgColor() {
        return rowBgColor;
    }

    public void setRowBgColor(String rowBgColor) {
        this.rowBgColor = rowBgColor;
    }

    public String getRowTextColor() {
        return rowTextColor;
    }

    public void setRowTextColor(String rowTextColor) {
        this.rowTextColor = rowTextColor;
    }

    public String getTimesheethours() {
        return timesheethours;
    }

    public void setTimesheethours(String timesheethours) {
        this.timesheethours = timesheethours;
    }

    public Integer getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    public String getWorkinghours() {
        return workinghours;
    }

    public void setWorkinghours(String workinghours) {
        this.workinghours = workinghours;
    }
}
