package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by maninder on 27/7/17.
 */

public class Employee_contacts {

    public String strUrl, strEmpName, strEmpDesingation, strMail, strPhoneNumber;
    public boolean isSwiped;


    @SerializedName("empid")
    @Expose
    public Integer empid;
    @SerializedName("ProfileImageURL")
    @Expose
    public String ProfileImageURL;
    @SerializedName("empName")
    @Expose
    public String empName;
    @SerializedName("empAddress")
    @Expose
    public String empAddress;
    @SerializedName("empGender")
    @Expose
    public String empGender;
    @SerializedName("empContact")
    @Expose
    public String empContact;
    @SerializedName("empJoiningDate")
    @Expose
    public String empJoiningDate;
    @SerializedName("empLeavingDate")
    @Expose
    public String empLeavingDate;
    @SerializedName("empProbationPeriod")
    @Expose
    public Integer empProbationPeriod;
    @SerializedName("empNotes")
    @Expose
    public String empNotes;
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
    public String empPAN;
    @SerializedName("EmpUAN")
    @Expose
    public String empUAN;
    @SerializedName("EmpEPF")
    @Expose
    public String empEPF;
    @SerializedName("empBDate")
    @Expose
    public String empBDate;
    @SerializedName("empADate")
    @Expose
    public String empADate;
    @SerializedName("empPrevEmployer")
    @Expose
    public String empPrevEmployer;
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
    public String insertedOn;
    @SerializedName("InsertedBy")
    @Expose
    public Integer insertedBy;
    @SerializedName("InsertedIP")
    @Expose
    public String insertedIP;
    @SerializedName("ModifiedOn")
    @Expose
    public String modifiedOn;
    @SerializedName("ModifiedBy")
    @Expose
    public Integer modifiedBy;
    @SerializedName("ModifiedIP")
    @Expose
    public String modifiedIP;
    @SerializedName("IsTester")
    @Expose
    public Boolean isTester;
    @SerializedName("empPassword")
    @Expose
    public String empPassword;
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
    public String resume;
    @SerializedName("Qualification")
    @Expose
    public String qualification;
    @SerializedName("QualificationId")
    @Expose
    public String qualificationId;
    @SerializedName("SecSkills")
    @Expose
    public String secSkills;
    @SerializedName("SecSkillsId")
    @Expose
    public String secSkillsId;
    @SerializedName("PrimarySkill")
    @Expose
    public Integer primarySkill;
    @SerializedName("Designation")
    @Expose
    public String designation;
    @SerializedName("Skill")
    @Expose
    public String skill;
    @SerializedName("Type")
    @Expose
    public String type;
    @SerializedName("Photo")
    @Expose
    public String photo;
    @SerializedName("Mode")
    @Expose
    public String mode;
    @SerializedName("EmpStatus")
    @Expose
    public String empStatus;
    @SerializedName("LeavingStatus")
    @Expose
    public String leavingStatus;
    @SerializedName("ProjectID")
    @Expose
    public Integer projectID;
    @SerializedName("SecurityLevel")
    @Expose
    public Integer securityLevel;
    @SerializedName("PrimarySkillDesc")
    @Expose
    public String primarySkillDesc;
    @SerializedName("Event")
    @Expose
    public String event;
    @SerializedName("ProfileID")
    @Expose
    public Integer profileID;
    @SerializedName("CAddress")
    @Expose
    public String cAddress;
    @SerializedName("HistoryID")
    @Expose
    public Integer historyID;
    @SerializedName("ADUserName")
    @Expose
    public String aDUserName;
    @SerializedName("Flag")
    @Expose
    public String flag;
    @SerializedName("strBirthday")
    @Expose
    public String strBirthday;
}
