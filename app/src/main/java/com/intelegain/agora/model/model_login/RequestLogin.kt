package com.intelegain.agora.model.model_login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RequestLogin {
    @SerializedName("OsType")
    @Expose
    var osType: String? = null
    @SerializedName("DeviceId")
    @Expose
    var deviceId: String? = null
    @SerializedName("EmpId")
    @Expose
    var empId: String? = null
    @SerializedName("fcmToken")
    @Expose
    var fcmToken: String? = null
    @SerializedName("Password")
    @Expose
    var password: String? = null

}