package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by kalpesh.c on 1/3/18.
 */
class KnowdlegeAttachmentResponce {
    @kotlin.jvm.JvmField
    @Expose
    @SerializedName("Attachments")
    var Attachments: List<Attachment>? = null

    class Attachment {
        @Expose
        @SerializedName("FileData")
        var FileData: String? = null
        @kotlin.jvm.JvmField
        @Expose
        @SerializedName("FileName")
        var FileName: String? = null
    }
}