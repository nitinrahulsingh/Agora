package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by maninder on 2/1/18.
 */

public class SaveSkillMatrixEntity {
    @SerializedName("UserID")
    @Expose
    public String userID;
    @SerializedName("EmpId")
    @Expose
    public String empId;
    @SerializedName("lstEmpSkill")
    @Expose
    public List<SkillMatrixData.EmpSkill> lstEmpSkill = null;
}
