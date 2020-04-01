package com.intelegain.agora.model;



import com.intelegain.agora.utils.CalenderMonth;

import java.util.Date;

/**
 * Created by suraj.m on 10/7/17.
 */

public class CalanderDate {

    private String dateStr;
    private boolean isSunday;
    private Date date;
    private CalenderMonth calenderMonth;

    public CalanderDate(String dateStr, boolean isSunday, Date date, CalenderMonth calenderMonth) {
        this.dateStr = dateStr;
        this.isSunday = isSunday;
        this.date = date;
        this.calenderMonth = calenderMonth;
    }

    public String getDateStr() {
        return dateStr;
    }

    public boolean isSunday() {
        return isSunday;
    }

    public Date getDate() {
        return date;
    }

    public CalenderMonth getCalenderMonth() {
        return calenderMonth;
    }




//    private String date;
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public boolean isSunday() {
//        return isSunday;
//    }
//
//    public void setSunday(boolean sunday) {
//        isSunday = sunday;
//    }
//
//    public boolean isEnablel() {
//        return isEnablel;
//    }
//
//    public void setEnablel(boolean enablel) {
//        isEnablel = enablel;
//    }
//
//    private boolean isSunday;
//    private boolean isEnablel;

}
