package com.intelegain.agora.model.model_login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseLogin {
    @SerializedName("EmployeeID")
    @Expose
    var employeeID: Int? = null
    @SerializedName("IsAdmin")
    @Expose
    var isAdmin: Boolean? = null
    @SerializedName("IsModuleAdmin")
    @Expose
    var isModuleAdmin: Boolean? = null
    @SerializedName("SkillID")
    @Expose
    var skillID: Int? = null
    @SerializedName("Name")
    @Expose
    var name: String? = null
    @SerializedName("EmailID")
    @Expose
    var emailID: String? = null
    @SerializedName("Address")
    @Expose
    var address: String? = null
    @SerializedName("Contact")
    @Expose
    var contact: String? = null
    @SerializedName("JoiningDate")
    @Expose
    var joiningDate: String? = null
    @SerializedName("LeavingDate")
    @Expose
    var leavingDate: String? = null
    @SerializedName("ProbationPeriod")
    @Expose
    var probationPeriod: String? = null
    @SerializedName("Notes")
    @Expose
    var notes: String? = null
    @SerializedName("AccountNo")
    @Expose
    var accountNo: Any? = null
    @SerializedName("BDate")
    @Expose
    var bDate: String? = null
    @SerializedName("ADate")
    @Expose
    var aDate: String? = null
    @SerializedName("PreviousEmployer")
    @Expose
    var previousEmployer: String? = null
    @SerializedName("Experince")
    @Expose
    var experince: Int? = null
    @SerializedName("LocationID")
    @Expose
    var locationID: String? = null
    @SerializedName("ProfileID")
    @Expose
    var profileID: String? = null
    @SerializedName("ProfileLocationID")
    @Expose
    var profileLocationID: Int? = null
    @SerializedName("IsActive")
    @Expose
    var isActive: Boolean? = null
    @SerializedName("Message")
    @Expose
    var message: String? = null
    @SerializedName("UserType")
    @Expose
    var userType: String? = null
    @SerializedName("CurrentAddress")
    @Expose
    var currentAddress: String? = null
    @SerializedName("empConfDate")
    @Expose
    var empConfDate: String? = null
    @SerializedName("Designation")
    @Expose
    var designation: String? = null
    @SerializedName("status")
    @Expose
    var status: Boolean? = null
    @SerializedName("empForgotpwdLinkDate")
    @Expose
    var empForgotpwdLinkDate: Any? = null
    @SerializedName("empPassword")
    @Expose
    var empPassword: Any? = null
    @SerializedName("Token")
    @Expose
    var token: String? = null
    @SerializedName("UploadedImage")
    @Expose
    var uploadedImage: String? = null

}