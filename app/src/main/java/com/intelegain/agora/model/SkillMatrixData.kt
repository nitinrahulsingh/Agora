package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by maninder on 22/11/17.
 */
class SkillMatrixData {
    @SerializedName("lstEmpSkill")
    @Expose
    var lstEmpSkill: List<EmpSkill>? = null

    inner class EmpSkill {
        @SerializedName("SkillID")
        @Expose
        var skillID: String? = null
        @SerializedName("CategoryId")
        @Expose
        var categoryId: Int? = null
        @SerializedName("EmployeeSkillID")
        @Expose
        var employeeSkillID: Int? = null
        @SerializedName("EmpID")
        @Expose
        var empID: Int? = null
        @SerializedName("UserID")
        @Expose
        var userID: Int? = null
        @SerializedName("Experience")
        @Expose
        var experience: String? = null
        @SerializedName("Years")
        @Expose
        var years: String? = null
        @SerializedName("Months")
        @Expose
        var months: String? = null
        @SerializedName("EmpCount")
        @Expose
        var empCount: Int? = null
        @SerializedName("Mode")
        @Expose
        var mode: Any? = null
        @SerializedName("Category")
        @Expose
        var category: String? = null
        @SerializedName("SkillName")
        @Expose
        var skillName: String? = null
        @SerializedName("Level")
        @Expose
        var level: String? = null
        @SerializedName("ActiveSkill")
        @Expose
        var activeSkill: Boolean? = null
        @SerializedName("Status")
        @Expose
        var status: Any? = null
        @SerializedName("EmpName")
        @Expose
        var empName: Any? = null
        @SerializedName("MaxExperience")
        @Expose
        var maxExperience: Any? = null
        @SerializedName("InsertedDate")
        @Expose
        var insertedDate: Any? = null
        var valueHasChanged = false
    }
}