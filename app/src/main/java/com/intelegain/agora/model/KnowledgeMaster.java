package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by suraj.m on 17/8/17.
 */

public class KnowledgeMaster {
    @SerializedName("KnowledgeData")
    @Expose
    private ArrayList<KnowledgeBaseData> knowledgeData = null;

    public ArrayList<KnowledgeBaseData> getKnowledgeData() {
        return knowledgeData;
    }

    public void setKnowledgeData(ArrayList<KnowledgeBaseData> knowledgeData) {
        this.knowledgeData = knowledgeData;
    }
}
