package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by maninder on 21/11/17.
 */

public class KnowledgebaseProjectName {
    @SerializedName("ProjectData")
    @Expose
    public List<ProjectDatum> projectData = null;


    public class ProjectDatum {

        @SerializedName("projId")
        @Expose
        public Integer projId;
        @SerializedName("projName")
        @Expose
        public String projName;
        @SerializedName("custId")
        @Expose
        public Integer custId;
        @SerializedName("projDesc")
        @Expose
        public Object projDesc;

    }
}
