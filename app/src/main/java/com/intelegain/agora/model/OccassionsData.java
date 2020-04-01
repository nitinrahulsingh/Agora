package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by suraj.m on 6/9/17.
 */

public class OccassionsData {

    @SerializedName("OccassionFor")
    @Expose
    private String occassionFor;
    @SerializedName("OccassionDate")
    @Expose
    private String occassionDate;
    @SerializedName("OccassionName")
    @Expose
    private String occassionName;
    @SerializedName("OccasionImageUrl")
    @Expose
    private String occasionImageUrl;

    public String getOccassionFor() {
        return occassionFor;
    }

    public void setOccassionFor(String occassionFor) {
        this.occassionFor = occassionFor;
    }

    public String getOccassionDate() {
        return occassionDate;
    }

    public void setOccassionDate(String occassionDate) {
        this.occassionDate = occassionDate;
    }

    public String getOccassionName() {
        return occassionName;
    }

    public void setOccassionName(String occassionName) {
        this.occassionName = occassionName;
    }

    public String getOccasionImageUrl() {
        return occasionImageUrl;
    }

    public void setOccasionImageUrl(String occasionImageUrl) {
        this.occasionImageUrl = occasionImageUrl;
    }
}
