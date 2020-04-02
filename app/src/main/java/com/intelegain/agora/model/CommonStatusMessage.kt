package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by maninder on 17/11/17.
 */
class CommonStatusMessage {
    @kotlin.jvm.JvmField
    @SerializedName("Status")
    @Expose
    var status: String? = null
    @kotlin.jvm.JvmField
    @SerializedName("Message")
    @Expose
    var message: String? = null
}