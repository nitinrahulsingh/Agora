package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by suraj.m on 17/8/17.
 */

public class HolidayMaster {
    @SerializedName("HolidayList")
    @Expose
    private ArrayList<HolidayData> holidayList = null;

    public ArrayList<HolidayData> getHolidayList() {
        return holidayList;
    }

    public void setHolidayList(ArrayList<HolidayData> holidayList) {
        this.holidayList = holidayList;
    }
}
