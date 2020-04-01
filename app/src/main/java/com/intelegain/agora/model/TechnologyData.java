package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by maninder on 21/11/17.
 */

public class TechnologyData {
    @SerializedName("TechnologyData")
    @Expose
    public List<TechnologyDatum> technologyData = null;


    public class TechnologyDatum {

        @SerializedName("techId")
        @Expose
        public Integer techId;
        @SerializedName("techName")
        @Expose
        public String techName;
        @SerializedName("subtechName")
        @Expose
        public String subtechName;

    }
}
