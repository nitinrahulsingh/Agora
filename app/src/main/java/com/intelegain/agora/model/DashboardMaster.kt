package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by suraj.m on 6/9/17.
 */
class DashboardMaster {
    @SerializedName("DashboardData")
    @Expose
    var dashboardData: ArrayList<AttendanceData>? = null
    @SerializedName("OccassionsData")
    @Expose
    var occassionsData: ArrayList<OccassionsData>? = null
    @SerializedName("News")
    @Expose
    var news: ArrayList<News>? = null
    @SerializedName("CIPSessions")
    @Expose
    var cIPSessions: ArrayList<CIPSession>? = null

}