package com.intelegain.agora.utils

import java.util.*

/**
 * Created by suraj.m on 3/8/17.
 */
object CalendarUtil {
    var objCal: Calendar? = null
    /** Get the current MONTH in the form of Integer
     * @return Current MONTH in integer format
     */
    @JvmStatic
    val currentMonthInInteger: Int
        get() {
            if (objCal == null) objCal = Calendar.getInstance()
            return objCal!![Calendar.MONTH]
        }

    /** Get the current DAY in the form of Integer
     * @return Current DAY in integer format
     */
    @JvmStatic
    val currentDayInInteger: Int
        get() {
            if (objCal == null) objCal = Calendar.getInstance()
            return objCal!![Calendar.DAY_OF_MONTH]
        }

    /** Get the current YEAR in the form of Integer
     * @return Current YEAR in integer format
     */
    @JvmStatic
    val currentYearInInteger: Int
        get() {
            if (objCal == null) objCal = Calendar.getInstance()
            return objCal!![Calendar.YEAR]
        }
}