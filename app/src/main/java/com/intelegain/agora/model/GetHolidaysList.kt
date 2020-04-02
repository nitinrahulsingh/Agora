package com.intelegain.agora.model

import java.util.*

/**
 * Created by Admin on 20/08/16.
 */
class GetHolidaysList {
    var Message = ""
    @kotlin.jvm.JvmField
    var Error = ""
    @kotlin.jvm.JvmField
    var holiday_arraylist = ArrayList<HolidayListData>()
    var Status = 0
}