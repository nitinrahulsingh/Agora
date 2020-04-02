package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by maninder on 21/11/17.
 */
class TechnologyData {
    @SerializedName("TechnologyData")
    @Expose
    var technologyData: List<TechnologyDatum>? = null

    inner class TechnologyDatum {
        @SerializedName("techId")
        @Expose
        var techId: Int? = null
        @SerializedName("techName")
        @Expose
        var techName: String? = null
        @SerializedName("subtechName")
        @Expose
        var subtechName: String? = null
    }
}