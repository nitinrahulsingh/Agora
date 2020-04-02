package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by kalpesh.c on 6/3/18.
 */
class KbAttchmentResponce {
    @Expose
    @SerializedName("Message")
    var Message: String? = null
    @Expose
    @SerializedName("Status")
    var Status: String? = null
}