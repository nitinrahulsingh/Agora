package com.intelegain.agora.model.model_login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestLogin
{
    @SerializedName("OsType")
    @Expose
    private String osType;
    @SerializedName("DeviceId")
    @Expose
    private String deviceId;
    @SerializedName("EmpId")
    @Expose
    private String empId;
    @SerializedName("fcmToken")
    @Expose
    private String fcmToken;
    @SerializedName("Password")
    @Expose
    private String password;

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
