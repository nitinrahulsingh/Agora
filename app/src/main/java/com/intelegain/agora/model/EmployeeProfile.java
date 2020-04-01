package com.intelegain.agora.model;

/**
 * Created by maninder on 7/11/17.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeProfile {

    @SerializedName("empid")
    @Expose
    public String empid;
    @SerializedName("empName")
    @Expose
    public String empName;
    @SerializedName("empAddress")
    @Expose
    public String empAddress;
    @SerializedName("empGender")
    @Expose
    public Object empGender;
    @SerializedName("empContact")
    @Expose
    public String empContact;
    @SerializedName("empJoiningDate")
    @Expose
    public String empJoiningDate;
    @SerializedName("empLeavingDate")
    @Expose
    public Object empLeavingDate;
    @SerializedName("empProbationPeriod")
    @Expose
    public Integer empProbationPeriod;
    @SerializedName("empNotes")
    @Expose
    public Object empNotes;
    @SerializedName("empEmail")
    @Expose
    public String empEmail;
    @SerializedName("empTester")
    @Expose
    public Boolean empTester;
    @SerializedName("empAccountNo")
    @Expose
    public String empAccountNo;
    @SerializedName("EmpPAN")
    @Expose
    public Object empPAN;
    @SerializedName("EmpUAN")
    @Expose
    public Object empUAN;
    @SerializedName("EmpEPF")
    @Expose
    public Object empEPF;
    @SerializedName("empBDate")
    @Expose
    public Object empBDate;
    @SerializedName("empADate")
    @Expose
    public String empADate;
    @SerializedName("empPrevEmployer")
    @Expose
    public Object empPrevEmployer;
    @SerializedName("empExperince")
    @Expose
    public Integer empExperince;
    @SerializedName("IsSuperAdmin")
    @Expose
    public Boolean isSuperAdmin;
    @SerializedName("IsAccountAdmin")
    @Expose
    public Boolean isAccountAdmin;
    @SerializedName("IsPayrollAdmin")
    @Expose
    public Boolean isPayrollAdmin;
    @SerializedName("IsPM")
    @Expose
    public Boolean isPM;
    @SerializedName("IsProjectReport")
    @Expose
    public Boolean isProjectReport;
    @SerializedName("IsProjectStatus")
    @Expose
    public Boolean isProjectStatus;
    @SerializedName("IsLeaveAdmin")
    @Expose
    public Boolean isLeaveAdmin;
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("LocationFKID")
    @Expose
    public Integer locationFKID;
    @SerializedName("skillid")
    @Expose
    public Integer skillid;
    @SerializedName("InsertedOn")
    @Expose
    public Object insertedOn;
    @SerializedName("InsertedBy")
    @Expose
    public Integer insertedBy;
    @SerializedName("InsertedIP")
    @Expose
    public Object insertedIP;
    @SerializedName("ModifiedOn")
    @Expose
    public Object modifiedOn;
    @SerializedName("ModifiedBy")
    @Expose
    public Integer modifiedBy;
    @SerializedName("ModifiedIP")
    @Expose
    public Object modifiedIP;
    @SerializedName("IsTester")
    @Expose
    public Boolean isTester;
    @SerializedName("empPassword")
    @Expose
    public Object empPassword;
    @SerializedName("AnnualCTC")
    @Expose
    public Integer annualCTC;
    @SerializedName("CTC")
    @Expose
    public Integer cTC;
    @SerializedName("Gross")
    @Expose
    public Integer gross;
    @SerializedName("Net")
    @Expose
    public Integer net;
    @SerializedName("Resume")
    @Expose
    public Object resume;
    @SerializedName("Qualification")
    @Expose
    public Object qualification;
    @SerializedName("QualificationId")
    @Expose
    public Object qualificationId;
    @SerializedName("SecSkills")
    @Expose
    public Object secSkills;
    @SerializedName("SecSkillsId")
    @Expose
    public Object secSkillsId;
    @SerializedName("PrimarySkill")
    @Expose
    public Integer primarySkill;
    @SerializedName("Designation")
    @Expose
    public String designation;
    @SerializedName("Skill")
    @Expose
    public Object skill;
    @SerializedName("Type")
    @Expose
    public Object type;
    @SerializedName("Photo")
    @Expose
    public Object photo;
    @SerializedName("Mode")
    @Expose
    public Object mode;
    @SerializedName("EmpStatus")
    @Expose
    public Object empStatus;
    @SerializedName("LeavingStatus")
    @Expose
    public Object leavingStatus;
    @SerializedName("ProjectID")
    @Expose
    public Integer projectID;
    @SerializedName("SecurityLevel")
    @Expose
    public Integer securityLevel;
    @SerializedName("PrimarySkillDesc")
    @Expose
    public Object primarySkillDesc;
    @SerializedName("Event")
    @Expose
    public Object event;
    @SerializedName("ProfileID")
    @Expose
    public Integer profileID;
    @SerializedName("CAddress")
    @Expose
    public Object cAddress;
    @SerializedName("HistoryID")
    @Expose
    public Integer historyID;
    @SerializedName("ADUserName")
    @Expose
    public Object aDUserName;
    @SerializedName("Flag")
    @Expose
    public String flag;
    @SerializedName("strBirthday")
    @Expose
    public String strBirthday;

    @SerializedName("imageUrl")
    @Expose
    public String imageUrl;

}
