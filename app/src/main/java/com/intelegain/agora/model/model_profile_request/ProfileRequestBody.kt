package com.intelegain.agora.model.model_profile_request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfileRequestBody {
    @SerializedName("EmpId")
    @Expose
    var empId: String? = null

}