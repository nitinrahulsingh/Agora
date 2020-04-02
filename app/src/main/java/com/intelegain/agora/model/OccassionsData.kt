package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by suraj.m on 6/9/17.
 */
class OccassionsData {
    @SerializedName("OccassionFor")
    @Expose
    var occassionFor: String? = null
    @SerializedName("OccassionDate")
    @Expose
    var occassionDate: String? = null
    @SerializedName("OccassionName")
    @Expose
    var occassionName: String? = null
    @SerializedName("OccasionImageUrl")
    @Expose
    var occasionImageUrl: String? = null

}