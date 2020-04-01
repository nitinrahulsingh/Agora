package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by suraj.m on 17/8/17.
 */

public class KnowledgeTagList {
    @SerializedName("TagName")
    @Expose
    private String tagName;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
