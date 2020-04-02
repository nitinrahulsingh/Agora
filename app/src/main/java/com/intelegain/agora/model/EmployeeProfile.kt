package com.intelegain.agora.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by maninder on 7/11/17.
 */
class EmployeeProfile {
    @kotlin.jvm.JvmField
    @SerializedName("empid")
    @Expose
    var empid: String? = null
    @kotlin.jvm.JvmField
    @SerializedName("empName")
    @Expose
    var empName: String? = null
    @kotlin.jvm.JvmField
    @SerializedName("empAddress")
    @Expose
    var empAddress: String? = null
    @SerializedName("empGender")
    @Expose
    var empGender: Any? = null
    @kotlin.jvm.JvmField
    @SerializedName("empContact")
    @Expose
    var empContact: String? = null
    @kotlin.jvm.JvmField
    @SerializedName("empJoiningDate")
    @Expose
    var empJoiningDate: String? = null
    @SerializedName("empLeavingDate")
    @Expose
    var empLeavingDate: Any? = null
    @kotlin.jvm.JvmField
    @SerializedName("empProbationPeriod")
    @Expose
    var empProbationPeriod: Int? = null
    @SerializedName("empNotes")
    @Expose
    var empNotes: Any? = null
    @kotlin.jvm.JvmField
    @SerializedName("empEmail")
    @Expose
    var empEmail: String? = null
    @SerializedName("empTester")
    @Expose
    var empTester: Boolean? = null
    @kotlin.jvm.JvmField
    @SerializedName("empAccountNo")
    @Expose
    var empAccountNo: String? = null
    @SerializedName("EmpPAN")
    @Expose
    var empPAN: Any? = null
    @SerializedName("EmpUAN")
    @Expose
    var empUAN: Any? = null
    @SerializedName("EmpEPF")
    @Expose
    var empEPF: Any? = null
    @SerializedName("empBDate")
    @Expose
    var empBDate: Any? = null
    @kotlin.jvm.JvmField
    @SerializedName("empADate")
    @Expose
    var empADate: String? = null
    @SerializedName("empPrevEmployer")
    @Expose
    var empPrevEmployer: Any? = null
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
    var insertedOn: Any? = null
    @SerializedName("InsertedBy")
    @Expose
    var insertedBy: Int? = null
    @SerializedName("InsertedIP")
    @Expose
    var insertedIP: Any? = null
    @SerializedName("ModifiedOn")
    @Expose
    var modifiedOn: Any? = null
    @SerializedName("ModifiedBy")
    @Expose
    var modifiedBy: Int? = null
    @SerializedName("ModifiedIP")
    @Expose
    var modifiedIP: Any? = null
    @SerializedName("IsTester")
    @Expose
    var isTester: Boolean? = null
    @SerializedName("empPassword")
    @Expose
    var empPassword: Any? = null
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
    var resume: Any? = null
    @SerializedName("Qualification")
    @Expose
    var qualification: Any? = null
    @SerializedName("QualificationId")
    @Expose
    var qualificationId: Any? = null
    @SerializedName("SecSkills")
    @Expose
    var secSkills: Any? = null
    @SerializedName("SecSkillsId")
    @Expose
    var secSkillsId: Any? = null
    @SerializedName("PrimarySkill")
    @Expose
    var primarySkill: Int? = null
    @kotlin.jvm.JvmField
    @SerializedName("Designation")
    @Expose
    var designation: String? = null
    @SerializedName("Skill")
    @Expose
    var skill: Any? = null
    @SerializedName("Type")
    @Expose
    var type: Any? = null
    @SerializedName("Photo")
    @Expose
    var photo: Any? = null
    @SerializedName("Mode")
    @Expose
    var mode: Any? = null
    @SerializedName("EmpStatus")
    @Expose
    var empStatus: Any? = null
    @SerializedName("LeavingStatus")
    @Expose
    var leavingStatus: Any? = null
    @SerializedName("ProjectID")
    @Expose
    var projectID: Int? = null
    @SerializedName("SecurityLevel")
    @Expose
    var securityLevel: Int? = null
    @SerializedName("PrimarySkillDesc")
    @Expose
    var primarySkillDesc: Any? = null
    @SerializedName("Event")
    @Expose
    var event: Any? = null
    @SerializedName("ProfileID")
    @Expose
    var profileID: Int? = null
    @SerializedName("CAddress")
    @Expose
    var cAddress: Any? = null
    @SerializedName("HistoryID")
    @Expose
    var historyID: Int? = null
    @SerializedName("ADUserName")
    @Expose
    var aDUserName: Any? = null
    @SerializedName("Flag")
    @Expose
    var flag: String? = null
    @kotlin.jvm.JvmField
    @SerializedName("strBirthday")
    @Expose
    var strBirthday: String? = null
    @kotlin.jvm.JvmField
    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null
}