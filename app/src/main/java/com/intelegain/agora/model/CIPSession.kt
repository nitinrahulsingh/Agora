package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by suraj.m on 6/9/17.
 */
class CIPSession {
    @SerializedName("CIPSessionsInfo")
    @Expose
    var cIPSessionsInfo: String? = null

}