package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.intelegain.agora.model.SkillMatrixData.EmpSkill

/**
 * Created by maninder on 2/1/18.
 */
class SaveSkillMatrixEntity {
    @SerializedName("UserID")
    @Expose
    var userID: String? = null
    @SerializedName("EmpId")
    @Expose
    var empId: String? = null
    @SerializedName("lstEmpSkill")
    @Expose
    var lstEmpSkill: List<EmpSkill>? = null
}