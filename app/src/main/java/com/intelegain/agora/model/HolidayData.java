package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by suraj.m on 24/7/17.
 */

public class HolidayData {

    @SerializedName("ImageId")
    @Expose
    private int ImageId;
    @SerializedName("HolidayDay")
    @Expose
    private String HolidayDay;
    @SerializedName("HolidayMonth")
    @Expose
    private String HolidayMonth;
    @SerializedName("HolidayName")
    @Expose
    private String HolidayName;
    @SerializedName("HolidayImageUrl")
    @Expose
    private String HolidayImageUrl;
    @SerializedName("HolidayBgColor")
    @Expose
    private String HolidayBgColor;

    public HolidayData(String holidayDay, String holidayMonth, String holidayName,
                       String holidayImageUrl, int imageId, String holidayBgColor) {
        HolidayDay = holidayDay;
        HolidayMonth = holidayMonth;
        HolidayName = holidayName;
        HolidayImageUrl = holidayImageUrl;
        ImageId = imageId;
        HolidayBgColor = holidayBgColor;
    }

    public String getHolidayDay() {
        return HolidayDay;
    }

    public String getHolidayMonth() {
        return HolidayMonth;
    }

    public String getHolidayName() {
        return HolidayName;
    }

    public String getHolidayImageUrl() {
        return HolidayImageUrl;
    }

    public int getImageId() {
        return ImageId;
    }

    public String getHolidayBgColor() {
        return HolidayBgColor;
    }

}
