package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by suraj.m on 17/8/17.
 */
class HolidayMaster {
    @SerializedName("HolidayList")
    @Expose
    var holidayList: ArrayList<HolidayData>? = null

}