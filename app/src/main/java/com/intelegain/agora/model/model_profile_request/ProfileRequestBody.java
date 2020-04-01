package com.intelegain.agora.model.model_profile_request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class ProfileRequestBody
{
    @SerializedName("EmpId")
    @Expose
    private String empId;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }


}
