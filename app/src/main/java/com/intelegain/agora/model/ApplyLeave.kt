package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by suraj.m on 17/8/17.
 */
class ApplyLeave {
    @SerializedName("Status")
    @Expose
    var status: String? = null
    @SerializedName("Message")
    @Expose
    var message: String? = null

}