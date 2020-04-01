package com.intelegain.agora.model.model_login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseLogin {
    @SerializedName("EmployeeID")
    @Expose
    private Integer employeeID;
    @SerializedName("IsAdmin")
    @Expose
    private Boolean isAdmin;
    @SerializedName("IsModuleAdmin")
    @Expose
    private Boolean isModuleAdmin;
    @SerializedName("SkillID")
    @Expose
    private Integer skillID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("EmailID")
    @Expose
    private String emailID;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Contact")
    @Expose
    private String contact;
    @SerializedName("JoiningDate")
    @Expose
    private String joiningDate;
    @SerializedName("LeavingDate")
    @Expose
    private String leavingDate;
    @SerializedName("ProbationPeriod")
    @Expose
    private String probationPeriod;
    @SerializedName("Notes")
    @Expose
    private String notes;
    @SerializedName("AccountNo")
    @Expose
    private Object accountNo;
    @SerializedName("BDate")
    @Expose
    private String bDate;
    @SerializedName("ADate")
    @Expose
    private String aDate;
    @SerializedName("PreviousEmployer")
    @Expose
    private String previousEmployer;
    @SerializedName("Experince")
    @Expose
    private Integer experince;
    @SerializedName("LocationID")
    @Expose
    private String locationID;
    @SerializedName("ProfileID")
    @Expose
    private String profileID;
    @SerializedName("ProfileLocationID")
    @Expose
    private Integer profileLocationID;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("UserType")
    @Expose
    private String userType;
    @SerializedName("CurrentAddress")
    @Expose
    private String currentAddress;
    @SerializedName("empConfDate")
    @Expose
    private String empConfDate;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("empForgotpwdLinkDate")
    @Expose
    private Object empForgotpwdLinkDate;
    @SerializedName("empPassword")
    @Expose
    private Object empPassword;
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("UploadedImage")
    @Expose
    private String uploadedImage;

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsModuleAdmin() {
        return isModuleAdmin;
    }

    public void setIsModuleAdmin(Boolean isModuleAdmin) {
        this.isModuleAdmin = isModuleAdmin;
    }

    public Integer getSkillID() {
        return skillID;
    }

    public void setSkillID(Integer skillID) {
        this.skillID = skillID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(String leavingDate) {
        this.leavingDate = leavingDate;
    }

    public String getProbationPeriod() {
        return probationPeriod;
    }

    public void setProbationPeriod(String probationPeriod) {
        this.probationPeriod = probationPeriod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Object getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Object accountNo) {
        this.accountNo = accountNo;
    }

    public String getBDate() {
        return bDate;
    }

    public void setBDate(String bDate) {
        this.bDate = bDate;
    }

    public String getADate() {
        return aDate;
    }

    public void setADate(String aDate) {
        this.aDate = aDate;
    }

    public String getPreviousEmployer() {
        return previousEmployer;
    }

    public void setPreviousEmployer(String previousEmployer) {
        this.previousEmployer = previousEmployer;
    }

    public Integer getExperince() {
        return experince;
    }

    public void setExperince(Integer experince) {
        this.experince = experince;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getProfileID() {
        return profileID;
    }

    public void setProfileID(String profileID) {
        this.profileID = profileID;
    }

    public Integer getProfileLocationID() {
        return profileLocationID;
    }

    public void setProfileLocationID(Integer profileLocationID) {
        this.profileLocationID = profileLocationID;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getEmpConfDate() {
        return empConfDate;
    }

    public void setEmpConfDate(String empConfDate) {
        this.empConfDate = empConfDate;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getEmpForgotpwdLinkDate() {
        return empForgotpwdLinkDate;
    }

    public void setEmpForgotpwdLinkDate(Object empForgotpwdLinkDate) {
        this.empForgotpwdLinkDate = empForgotpwdLinkDate;
    }

    public Object getEmpPassword() {
        return empPassword;
    }

    public void setEmpPassword(Object empPassword) {
        this.empPassword = empPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUploadedImage() {
        return uploadedImage;
    }

    public void setUploadedImage(String uploadedImage) {
        this.uploadedImage = uploadedImage;
    }

}

