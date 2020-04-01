package com.intelegain.agora.model.model_profile_request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileRequestResponse {

    @SerializedName("empid")
    @Expose
    private Integer empid;
    @SerializedName("empName")
    @Expose
    private String empName;
    @SerializedName("empAddress")
    @Expose
    private String empAddress;
    @SerializedName("empGender")
    @Expose
    private Object empGender;
    @SerializedName("empContact")
    @Expose
    private String empContact;
    @SerializedName("empJoiningDate")
    @Expose
    private Object empJoiningDate;
    @SerializedName("empLeavingDate")
    @Expose
    private Object empLeavingDate;
    @SerializedName("empProbationPeriod")
    @Expose
    private Integer empProbationPeriod;
    @SerializedName("empNotes")
    @Expose
    private Object empNotes;
    @SerializedName("empEmail")
    @Expose
    private String empEmail;
    @SerializedName("empTester")
    @Expose
    private Boolean empTester;
    @SerializedName("empAccountNo")
    @Expose
    private Object empAccountNo;
    @SerializedName("EmpPAN")
    @Expose
    private Object empPAN;
    @SerializedName("EmpUAN")
    @Expose
    private Object empUAN;
    @SerializedName("EmpEPF")
    @Expose
    private Object empEPF;
    @SerializedName("empBDate")
    @Expose
    private Object empBDate;
    @SerializedName("empADate")
    @Expose
    private Object empADate;
    @SerializedName("empPrevEmployer")
    @Expose
    private Object empPrevEmployer;
    @SerializedName("empExperince")
    @Expose
    private Integer empExperince;
    @SerializedName("IsSuperAdmin")
    @Expose
    private Boolean isSuperAdmin;
    @SerializedName("IsAccountAdmin")
    @Expose
    private Boolean isAccountAdmin;
    @SerializedName("IsPayrollAdmin")
    @Expose
    private Boolean isPayrollAdmin;
    @SerializedName("IsPM")
    @Expose
    private Boolean isPM;
    @SerializedName("IsProjectReport")
    @Expose
    private Boolean isProjectReport;
    @SerializedName("IsProjectStatus")
    @Expose
    private Boolean isProjectStatus;
    @SerializedName("IsLeaveAdmin")
    @Expose
    private Boolean isLeaveAdmin;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("LocationFKID")
    @Expose
    private Integer locationFKID;
    @SerializedName("skillid")
    @Expose
    private Integer skillid;
    @SerializedName("InsertedOn")
    @Expose
    private Object insertedOn;
    @SerializedName("InsertedBy")
    @Expose
    private Integer insertedBy;
    @SerializedName("InsertedIP")
    @Expose
    private Object insertedIP;
    @SerializedName("ModifiedOn")
    @Expose
    private Object modifiedOn;
    @SerializedName("ModifiedBy")
    @Expose
    private Integer modifiedBy;
    @SerializedName("ModifiedIP")
    @Expose
    private Object modifiedIP;
    @SerializedName("IsTester")
    @Expose
    private Boolean isTester;
    @SerializedName("empPassword")
    @Expose
    private Object empPassword;
    @SerializedName("AnnualCTC")
    @Expose
    private Integer annualCTC;
    @SerializedName("CTC")
    @Expose
    private Integer cTC;
    @SerializedName("Gross")
    @Expose
    private Integer gross;
    @SerializedName("Net")
    @Expose
    private Integer net;
    @SerializedName("Resume")
    @Expose
    private Object resume;
    @SerializedName("Qualification")
    @Expose
    private Object qualification;
    @SerializedName("QualificationId")
    @Expose
    private Object qualificationId;
    @SerializedName("SecSkills")
    @Expose
    private Object secSkills;
    @SerializedName("SecSkillsId")
    @Expose
    private Object secSkillsId;
    @SerializedName("PrimarySkill")
    @Expose
    private Integer primarySkill;
    @SerializedName("Designation")
    @Expose
    private String designation;
    @SerializedName("Skill")
    @Expose
    private Object skill;
    @SerializedName("Type")
    @Expose
    private Object type;
    @SerializedName("Photo")
    @Expose
    private Object photo;
    @SerializedName("Mode")
    @Expose
    private Object mode;
    @SerializedName("EmpStatus")
    @Expose
    private Object empStatus;
    @SerializedName("LeavingStatus")
    @Expose
    private Object leavingStatus;
    @SerializedName("ProjectID")
    @Expose
    private Integer projectID;
    @SerializedName("SecurityLevel")
    @Expose
    private Integer securityLevel;
    @SerializedName("PrimarySkillDesc")
    @Expose
    private Object primarySkillDesc;
    @SerializedName("Event")
    @Expose
    private Object event;
    @SerializedName("ProfileID")
    @Expose
    private Integer profileID;
    @SerializedName("CAddress")
    @Expose
    private Object cAddress;
    @SerializedName("HistoryID")
    @Expose
    private Integer historyID;
    @SerializedName("ADUserName")
    @Expose
    private Object aDUserName;
    @SerializedName("Flag")
    @Expose
    private String flag;
    @SerializedName("strBirthday")
    @Expose
    private String strBirthday;

    public Integer getEmpid() {
        return empid;
    }

    public void setEmpid(Integer empid) {
        this.empid = empid;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpAddress() {
        return empAddress;
    }

    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    public Object getEmpGender() {
        return empGender;
    }

    public void setEmpGender(Object empGender) {
        this.empGender = empGender;
    }

    public String getEmpContact() {
        return empContact;
    }

    public void setEmpContact(String empContact) {
        this.empContact = empContact;
    }

    public Object getEmpJoiningDate() {
        return empJoiningDate;
    }

    public void setEmpJoiningDate(Object empJoiningDate) {
        this.empJoiningDate = empJoiningDate;
    }

    public Object getEmpLeavingDate() {
        return empLeavingDate;
    }

    public void setEmpLeavingDate(Object empLeavingDate) {
        this.empLeavingDate = empLeavingDate;
    }

    public Integer getEmpProbationPeriod() {
        return empProbationPeriod;
    }

    public void setEmpProbationPeriod(Integer empProbationPeriod) {
        this.empProbationPeriod = empProbationPeriod;
    }

    public Object getEmpNotes() {
        return empNotes;
    }

    public void setEmpNotes(Object empNotes) {
        this.empNotes = empNotes;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public Boolean getEmpTester() {
        return empTester;
    }

    public void setEmpTester(Boolean empTester) {
        this.empTester = empTester;
    }

    public Object getEmpAccountNo() {
        return empAccountNo;
    }

    public void setEmpAccountNo(Object empAccountNo) {
        this.empAccountNo = empAccountNo;
    }

    public Object getEmpPAN() {
        return empPAN;
    }

    public void setEmpPAN(Object empPAN) {
        this.empPAN = empPAN;
    }

    public Object getEmpUAN() {
        return empUAN;
    }

    public void setEmpUAN(Object empUAN) {
        this.empUAN = empUAN;
    }

    public Object getEmpEPF() {
        return empEPF;
    }

    public void setEmpEPF(Object empEPF) {
        this.empEPF = empEPF;
    }

    public Object getEmpBDate() {
        return empBDate;
    }

    public void setEmpBDate(Object empBDate) {
        this.empBDate = empBDate;
    }

    public Object getEmpADate() {
        return empADate;
    }

    public void setEmpADate(Object empADate) {
        this.empADate = empADate;
    }

    public Object getEmpPrevEmployer() {
        return empPrevEmployer;
    }

    public void setEmpPrevEmployer(Object empPrevEmployer) {
        this.empPrevEmployer = empPrevEmployer;
    }

    public Integer getEmpExperince() {
        return empExperince;
    }

    public void setEmpExperince(Integer empExperince) {
        this.empExperince = empExperince;
    }

    public Boolean getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(Boolean isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
    }

    public Boolean getIsAccountAdmin() {
        return isAccountAdmin;
    }

    public void setIsAccountAdmin(Boolean isAccountAdmin) {
        this.isAccountAdmin = isAccountAdmin;
    }

    public Boolean getIsPayrollAdmin() {
        return isPayrollAdmin;
    }

    public void setIsPayrollAdmin(Boolean isPayrollAdmin) {
        this.isPayrollAdmin = isPayrollAdmin;
    }

    public Boolean getIsPM() {
        return isPM;
    }

    public void setIsPM(Boolean isPM) {
        this.isPM = isPM;
    }

    public Boolean getIsProjectReport() {
        return isProjectReport;
    }

    public void setIsProjectReport(Boolean isProjectReport) {
        this.isProjectReport = isProjectReport;
    }

    public Boolean getIsProjectStatus() {
        return isProjectStatus;
    }

    public void setIsProjectStatus(Boolean isProjectStatus) {
        this.isProjectStatus = isProjectStatus;
    }

    public Boolean getIsLeaveAdmin() {
        return isLeaveAdmin;
    }

    public void setIsLeaveAdmin(Boolean isLeaveAdmin) {
        this.isLeaveAdmin = isLeaveAdmin;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getLocationFKID() {
        return locationFKID;
    }

    public void setLocationFKID(Integer locationFKID) {
        this.locationFKID = locationFKID;
    }

    public Integer getSkillid() {
        return skillid;
    }

    public void setSkillid(Integer skillid) {
        this.skillid = skillid;
    }

    public Object getInsertedOn() {
        return insertedOn;
    }

    public void setInsertedOn(Object insertedOn) {
        this.insertedOn = insertedOn;
    }

    public Integer getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(Integer insertedBy) {
        this.insertedBy = insertedBy;
    }

    public Object getInsertedIP() {
        return insertedIP;
    }

    public void setInsertedIP(Object insertedIP) {
        this.insertedIP = insertedIP;
    }

    public Object getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Object modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Object getModifiedIP() {
        return modifiedIP;
    }

    public void setModifiedIP(Object modifiedIP) {
        this.modifiedIP = modifiedIP;
    }

    public Boolean getIsTester() {
        return isTester;
    }

    public void setIsTester(Boolean isTester) {
        this.isTester = isTester;
    }

    public Object getEmpPassword() {
        return empPassword;
    }

    public void setEmpPassword(Object empPassword) {
        this.empPassword = empPassword;
    }

    public Integer getAnnualCTC() {
        return annualCTC;
    }

    public void setAnnualCTC(Integer annualCTC) {
        this.annualCTC = annualCTC;
    }

    public Integer getCTC() {
        return cTC;
    }

    public void setCTC(Integer cTC) {
        this.cTC = cTC;
    }

    public Integer getGross() {
        return gross;
    }

    public void setGross(Integer gross) {
        this.gross = gross;
    }

    public Integer getNet() {
        return net;
    }

    public void setNet(Integer net) {
        this.net = net;
    }

    public Object getResume() {
        return resume;
    }

    public void setResume(Object resume) {
        this.resume = resume;
    }

    public Object getQualification() {
        return qualification;
    }

    public void setQualification(Object qualification) {
        this.qualification = qualification;
    }

    public Object getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(Object qualificationId) {
        this.qualificationId = qualificationId;
    }

    public Object getSecSkills() {
        return secSkills;
    }

    public void setSecSkills(Object secSkills) {
        this.secSkills = secSkills;
    }

    public Object getSecSkillsId() {
        return secSkillsId;
    }

    public void setSecSkillsId(Object secSkillsId) {
        this.secSkillsId = secSkillsId;
    }

    public Integer getPrimarySkill() {
        return primarySkill;
    }

    public void setPrimarySkill(Integer primarySkill) {
        this.primarySkill = primarySkill;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Object getSkill() {
        return skill;
    }

    public void setSkill(Object skill) {
        this.skill = skill;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getPhoto() {
        return photo;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public Object getMode() {
        return mode;
    }

    public void setMode(Object mode) {
        this.mode = mode;
    }

    public Object getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(Object empStatus) {
        this.empStatus = empStatus;
    }

    public Object getLeavingStatus() {
        return leavingStatus;
    }

    public void setLeavingStatus(Object leavingStatus) {
        this.leavingStatus = leavingStatus;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(Integer securityLevel) {
        this.securityLevel = securityLevel;
    }

    public Object getPrimarySkillDesc() {
        return primarySkillDesc;
    }

    public void setPrimarySkillDesc(Object primarySkillDesc) {
        this.primarySkillDesc = primarySkillDesc;
    }

    public Object getEvent() {
        return event;
    }

    public void setEvent(Object event) {
        this.event = event;
    }

    public Integer getProfileID() {
        return profileID;
    }

    public void setProfileID(Integer profileID) {
        this.profileID = profileID;
    }

    public Object getCAddress() {
        return cAddress;
    }

    public void setCAddress(Object cAddress) {
        this.cAddress = cAddress;
    }

    public Integer getHistoryID() {
        return historyID;
    }

    public void setHistoryID(Integer historyID) {
        this.historyID = historyID;
    }

    public Object getADUserName() {
        return aDUserName;
    }

    public void setADUserName(Object aDUserName) {
        this.aDUserName = aDUserName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getStrBirthday() {
        return strBirthday;
    }

    public void setStrBirthday(String strBirthday) {
        this.strBirthday = strBirthday;
    }

}
