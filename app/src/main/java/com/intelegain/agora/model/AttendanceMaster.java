package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by suraj.m on 18/8/17.
 */

public class AttendanceMaster {

    @SerializedName("AttendanceData")
    @Expose
    private ArrayList<AttendanceData> attendanceData = null;

    public ArrayList<AttendanceData> getAttendanceData() {
        return attendanceData;
    }

    public void setAttendanceData(ArrayList<AttendanceData> attendanceData) {
        this.attendanceData = attendanceData;
    }
}
