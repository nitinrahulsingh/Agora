package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by maninder on 22/11/17.
 */

public class SkillMatrixData {

    @SerializedName("lstEmpSkill")
    @Expose
    public List<EmpSkill> lstEmpSkill = null;


    public class EmpSkill {

        @SerializedName("SkillID")
        @Expose
        public String skillID;
        @SerializedName("CategoryId")
        @Expose
        public Integer categoryId;
        @SerializedName("EmployeeSkillID")
        @Expose
        public Integer employeeSkillID;
        @SerializedName("EmpID")
        @Expose
        public Integer empID;
        @SerializedName("UserID")
        @Expose
        public Integer userID;
        @SerializedName("Experience")
        @Expose
        public String experience;
        @SerializedName("Years")
        @Expose
        public String years;
        @SerializedName("Months")
        @Expose
        public String months;
        @SerializedName("EmpCount")
        @Expose
        public Integer empCount;
        @SerializedName("Mode")
        @Expose
        public Object mode;
        @SerializedName("Category")
        @Expose
        public String category;
        @SerializedName("SkillName")
        @Expose
        public String skillName;
        @SerializedName("Level")
        @Expose
        public String level;
        @SerializedName("ActiveSkill")
        @Expose
        public Boolean activeSkill;
        @SerializedName("Status")
        @Expose
        public Object status;
        @SerializedName("EmpName")
        @Expose
        public Object empName;
        @SerializedName("MaxExperience")
        @Expose
        public Object maxExperience;
        @SerializedName("InsertedDate")
        @Expose
        public Object insertedDate;

        public boolean valueHasChanged = false;

    }
}
