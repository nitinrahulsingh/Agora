package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by maninder on 27/7/17.
 */
class Employee_contacts {
    var strUrl: String? = null
    var strEmpName: String? = null
    var strEmpDesingation: String? = null
    var strMail: String? = null
    var strPhoneNumber: String? = null
    var isSwiped = false
    @SerializedName("empid")
    @Expose
    var empid: Int? = null
    @SerializedName("ProfileImageURL")
    @Expose
    var ProfileImageURL: String? = null
    @SerializedName("empName")
    @Expose
    var empName: String? = null
    @SerializedName("empAddress")
    @Expose
    var empAddress: String? = null
    @SerializedName("empGender")
    @Expose
    var empGender: String? = null
    @SerializedName("empContact")
    @Expose
    var empContact: String? = null
    @SerializedName("empJoiningDate")
    @Expose
    var empJoiningDate: String? = null
    @SerializedName("empLeavingDate")
    @Expose
    var empLeavingDate: String? = null
    @SerializedName("empProbationPeriod")
    @Expose
    var empProbationPeriod: Int? = null
    @SerializedName("empNotes")
    @Expose
    var empNotes: String? = null
    @SerializedName("empEmail")
    @Expose
    var empEmail: String? = null
    @SerializedName("empTester")
    @Expose
    var empTester: Boolean? = null
    @SerializedName("empAccountNo")
    @Expose
    var empAccountNo: String? = null
    @SerializedName("EmpPAN")
    @Expose
    var empPAN: String? = null
    @SerializedName("EmpUAN")
    @Expose
    var empUAN: String? = null
    @SerializedName("EmpEPF")
    @Expose
    var empEPF: String? = null
    @SerializedName("empBDate")
    @Expose
    var empBDate: String? = null
    @SerializedName("empADate")
    @Expose
    var empADate: String? = null
    @SerializedName("empPrevEmployer")
    @Expose
    var empPrevEmployer: String? = null
    @SerializedName("empExperince")
    @Expose
    var empExperince: Int? = null
    @SerializedName("IsSuperAdmin")
    @Expose
    var isSuperAdmin: Boolean? = null
    @SerializedName("IsAccountAdmin")
    @Expose
    var isAccountAdmin: Boolean? = null
    @SerializedName("IsPayrollAdmin")
    @Expose
    var isPayrollAdmin: Boolean? = null
    @SerializedName("IsPM")
    @Expose
    var isPM: Boolean? = null
    @SerializedName("IsProjectReport")
    @Expose
    var isProjectReport: Boolean? = null
    @SerializedName("IsProjectStatus")
    @Expose
    var isProjectStatus: Boolean? = null
    @SerializedName("IsLeaveAdmin")
    @Expose
    var isLeaveAdmin: Boolean? = null
    @SerializedName("IsActive")
    @Expose
    var isActive: Boolean? = null
    @SerializedName("LocationFKID")
    @Expose
    var locationFKID: Int? = null
    @SerializedName("skillid")
    @Expose
    var skillid: Int? = null
    @SerializedName("InsertedOn")
    @Expose
    var insertedOn: String? = null
    @SerializedName("InsertedBy")
    @Expose
    var insertedBy: Int? = null
    @SerializedName("InsertedIP")
    @Expose
    var insertedIP: String? = null
    @SerializedName("ModifiedOn")
    @Expose
    var modifiedOn: String? = null
    @SerializedName("ModifiedBy")
    @Expose
    var modifiedBy: Int? = null
    @SerializedName("ModifiedIP")
    @Expose
    var modifiedIP: String? = null
    @SerializedName("IsTester")
    @Expose
    var isTester: Boolean? = null
    @SerializedName("empPassword")
    @Expose
    var empPassword: String? = null
    @SerializedName("AnnualCTC")
    @Expose
    var annualCTC: Int? = null
    @SerializedName("CTC")
    @Expose
    var cTC: Int? = null
    @SerializedName("Gross")
    @Expose
    var gross: Int? = null
    @SerializedName("Net")
    @Expose
    var net: Int? = null
    @SerializedName("Resume")
    @Expose
    var resume: String? = null
    @SerializedName("Qualification")
    @Expose
    var qualification: String? = null
    @SerializedName("QualificationId")
    @Expose
    var qualificationId: String? = null
    @SerializedName("SecSkills")
    @Expose
    var secSkills: String? = null
    @SerializedName("SecSkillsId")
    @Expose
    var secSkillsId: String? = null
    @SerializedName("PrimarySkill")
    @Expose
    var primarySkill: Int? = null
    @SerializedName("Designation")
    @Expose
    var designation: String? = null
    @SerializedName("Skill")
    @Expose
    var skill: String? = null
    @SerializedName("Type")
    @Expose
    var type: String? = null
    @SerializedName("Photo")
    @Expose
    var photo: String? = null
    @SerializedName("Mode")
    @Expose
    var mode: String? = null
    @SerializedName("EmpStatus")
    @Expose
    var empStatus: String? = null
    @SerializedName("LeavingStatus")
    @Expose
    var leavingStatus: String? = null
    @SerializedName("ProjectID")
    @Expose
    var projectID: Int? = null
    @SerializedName("SecurityLevel")
    @Expose
    var securityLevel: Int? = null
    @SerializedName("PrimarySkillDesc")
    @Expose
    var primarySkillDesc: String? = null
    @SerializedName("Event")
    @Expose
    var event: String? = null
    @SerializedName("ProfileID")
    @Expose
    var profileID: Int? = null
    @SerializedName("CAddress")
    @Expose
    var cAddress: String? = null
    @SerializedName("HistoryID")
    @Expose
    var historyID: Int? = null
    @SerializedName("ADUserName")
    @Expose
    var aDUserName: String? = null
    @SerializedName("Flag")
    @Expose
    var flag: String? = null
    @SerializedName("strBirthday")
    @Expose
    var strBirthday: String? = null
}