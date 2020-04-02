package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by suraj.m on 26/7/17.
 */
class KnowledgeBaseData(@field:Expose @field:SerializedName("ID") val knowledgeId: String, @field:Expose @field:SerializedName("KnowledgeTitle") val knowledgeTitle: String, @field:Expose @field:SerializedName("KnowledgeProjectTitle") val knowledgeProjectTitle: String,
                        @field:Expose @field:SerializedName("KnowledgeTechnology") val knowledgeTechnology: String, @field:Expose @field:SerializedName("KnowledgePostedBy") val knowledgePostedBy: String,
                        @field:Expose @field:SerializedName("KnowledgePostedDate") val knowledgePostedDate: String, @field:Expose @field:SerializedName("KnowledgeDescription") val knowledgeDescription: String, @field:Expose @field:SerializedName("KnowledegeFileName") val knowledegeFileName: String, @field:Expose @field:SerializedName("KnowledgeUrl") val knowledgeUrl: String, @field:Expose @field:SerializedName("KnowledgeTagList") val knowledgeTagList: ArrayList<KnowledgeTagList>)