package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by maninder on 21/11/17.
 */
class KnowledgebaseProjectName {
    @SerializedName("ProjectData")
    @Expose
    var projectData: List<ProjectDatum>? = null

    inner class ProjectDatum {
        @SerializedName("projId")
        @Expose
        var projId: Int? = null
        @SerializedName("projName")
        @Expose
        var projName: String? = null
        @SerializedName("custId")
        @Expose
        var custId: Int? = null
        @SerializedName("projDesc")
        @Expose
        var projDesc: Any? = null
    }
}