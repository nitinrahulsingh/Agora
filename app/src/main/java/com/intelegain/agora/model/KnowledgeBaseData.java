package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by suraj.m on 26/7/17.
 */

public class KnowledgeBaseData {




    @SerializedName("ID")
    @Expose
    private String KnowledgeID;

    @SerializedName("KnowledgeTitle")
    @Expose
    private String KnowledgeTitle;

    @SerializedName("KnowledgeProjectTitle")
    @Expose
    private String KnowledgeProjectTitle;
    @SerializedName("KnowledgeTechnology")
    @Expose
    private String KnowledgeTechnology;
    @SerializedName("KnowledgePostedBy")
    @Expose
    private String KnowledgePostedBy;
    @SerializedName("KnowledgePostedDate")
    @Expose
    private String KnowledgePostedDate;

    @SerializedName("KnowledgeDescription")
    @Expose
    private String KnowledgeDescription;

    @SerializedName("KnowledegeFileName")
    @Expose
    private String KnowledegeFileName;

    @SerializedName("KnowledgeUrl")
    @Expose
    private String KnowledgeUrl;

    @SerializedName("KnowledgeTagList")
    @Expose
    private ArrayList<KnowledgeTagList> mlstKnowledgeTagList;

    public KnowledgeBaseData(String id, String knowledgeTitle, String knowledgeProjectTitle,
                             String knowledgeTechnology, String knowledgePostedBy,
                             String knowledgePostedDate, String knowledgeDescription, String knowledegeFileName, String knowledgeUrl, ArrayList<KnowledgeTagList> lstKnowledgeTagList) {
        KnowledgeID=id;
        KnowledgeTitle = knowledgeTitle;
        KnowledgeProjectTitle = knowledgeProjectTitle;
        KnowledgeTechnology = knowledgeTechnology;
        KnowledgePostedBy = knowledgePostedBy;
        KnowledgePostedDate = knowledgePostedDate;
        mlstKnowledgeTagList = lstKnowledgeTagList;
        KnowledgeDescription = knowledgeDescription;
        KnowledegeFileName =knowledegeFileName;
        KnowledgeUrl=knowledgeUrl;
    }


    public String getKnowledgeId() {
        return KnowledgeID;
    }

    public String getKnowledgeTitle() {
        return KnowledgeTitle;
    }

    public String getKnowledgeProjectTitle() {
        return KnowledgeProjectTitle;
    }

    public String getKnowledgeTechnology() {
        return KnowledgeTechnology;
    }

    public String getKnowledgePostedBy() {
        return KnowledgePostedBy;
    }

    public String getKnowledgePostedDate() {
        return KnowledgePostedDate;
    }

    public String getKnowledgeDescription() {
        return KnowledgeDescription;
    }
    public String getKnowledegeFileName() {
        return KnowledegeFileName;
    }public String getKnowledgeUrl() {
        return KnowledgeUrl;
    }
    public ArrayList<KnowledgeTagList> getKnowledgeTagList() {
        return mlstKnowledgeTagList;
    }
}
