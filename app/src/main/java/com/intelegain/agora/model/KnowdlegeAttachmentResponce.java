package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kalpesh.c on 1/3/18.
 */

public class KnowdlegeAttachmentResponce {


    @Expose
    @SerializedName("Attachments")
    public List<Attachments> Attachments;

    public static class Attachments {
        @Expose
        @SerializedName("FileData")
        public String FileData;
        @Expose
        @SerializedName("FileName")
        public String FileName;
    }
}
