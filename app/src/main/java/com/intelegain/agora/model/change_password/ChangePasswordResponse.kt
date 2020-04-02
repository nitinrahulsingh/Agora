package com.intelegain.agora.model.change_password

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChangePasswordResponse {
    @SerializedName("Status")
    @Expose
    var status: String? = null
    @SerializedName("Message")
    @Expose
    var message: String? = null
}