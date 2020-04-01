package com.intelegain.agora.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by maninder on 4/12/17.
 */


public class EmpLogin {

    @SerializedName("EmployeeID")
    @Expose
    public Integer employeeID;
    @SerializedName("IsAdmin")
    @Expose
    public Boolean isAdmin;
    @SerializedName("IsModuleAdmin")
    @Expose
    public Boolean isModuleAdmin;
    @SerializedName("SkillID")
    @Expose
    public Integer skillID;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("EmailID")
    @Expose
    public String emailID;
    @SerializedName("Address")
    @Expose
    public String address;
    @SerializedName("Contact")
    @Expose
    public String contact;
    @SerializedName("JoiningDate")
    @Expose
    public String joiningDate;
    @SerializedName("LeavingDate")
    @Expose
    public String leavingDate;
    @SerializedName("ProbationPeriod")
    @Expose
    public String probationPeriod;
    @SerializedName("Notes")
    @Expose
    public String notes;
    @SerializedName("AccountNo")
    @Expose
    public Object accountNo;
    @SerializedName("BDate")
    @Expose
    public String bDate;
    @SerializedName("ADate")
    @Expose
    public String aDate;
    @SerializedName("PreviousEmployer")
    @Expose
    public String previousEmployer;
    @SerializedName("Experince")
    @Expose
    public Integer experince;
    @SerializedName("LocationID")
    @Expose
    public String locationID;
    @SerializedName("ProfileID")
    @Expose
    public String profileID;
    @SerializedName("ProfileLocationID")
    @Expose
    public Integer profileLocationID;
    @SerializedName("IsActive")
    @Expose
    public Boolean isActive;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("UserType")
    @Expose
    public String userType;
    @SerializedName("CurrentAddress")
    @Expose
    public String currentAddress;
    @SerializedName("empConfDate")
    @Expose
    public String empConfDate;
    @SerializedName("Designation")
    @Expose
    public String designation;
    @SerializedName("status")
    @Expose
    public Boolean status;
    @SerializedName("empForgotpwdLinkDate")
    @Expose
    public Object empForgotpwdLinkDate;
    @SerializedName("empPassword")
    @Expose
    public Object empPassword;
    @SerializedName("Token")
    @Expose
    public String token;
    @SerializedName("UploadedImage")
    @Expose
    public String uploadedImage;

}