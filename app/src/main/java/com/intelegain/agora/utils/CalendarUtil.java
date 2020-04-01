package com.intelegain.agora.utils;

import java.util.Calendar;

/**
 * Created by suraj.m on 3/8/17.
 */

public class CalendarUtil {

    public static Calendar objCal;
    private CalendarUtil() {

    }

    /** Get the current MONTH in the form of Integer
     * @return Current MONTH in integer format
     */
    public static int getCurrentMonthInInteger() {
        if(objCal == null)
            objCal = Calendar.getInstance();

        return objCal.get(Calendar.MONTH);
    }

    /** Get the current DAY in the form of Integer
     * @return Current DAY in integer format
     */
    public static int getCurrentDayInInteger() {
        if(objCal == null)
            objCal = Calendar.getInstance();

        return objCal.get(Calendar.DAY_OF_MONTH);
    }

    /** Get the current YEAR in the form of Integer
     * @return Current YEAR in integer format
     */
    public static int getCurrentYearInInteger() {
        if(objCal == null)
            objCal = Calendar.getInstance();

        return objCal.get(Calendar.YEAR);
    }
}
