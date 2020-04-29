package com.intelegain.agora.interfeces;

import com.intelegain.agora.model.AddKnowledge;
import com.intelegain.agora.model.AppliedLeaveDetails;
import com.intelegain.agora.model.ApplyLeave;
import com.intelegain.agora.model.AttendanceMaster;
import com.intelegain.agora.model.AvailableLeave;
import com.intelegain.agora.model.ChangePassword;
import com.intelegain.agora.model.CommonStatusMessage;
import com.intelegain.agora.model.DashboardMaster;
import com.intelegain.agora.model.DocumentData;
import com.intelegain.agora.model.EmpLogin;
import com.intelegain.agora.model.EmployeeProfile;
import com.intelegain.agora.model.Employee_contacts;
import com.intelegain.agora.model.ForgotPasswordDetails;
import com.intelegain.agora.model.HolidayMaster;
import com.intelegain.agora.model.KbAttchmentResponce;
import com.intelegain.agora.model.KnowdlegeAttachmentResponce;
import com.intelegain.agora.model.KnowledgeMaster;
import com.intelegain.agora.model.KnowledgebaseProjectName;
import com.intelegain.agora.model.Leave;
import com.intelegain.agora.model.LeaveMaster;
import com.intelegain.agora.model.SaveSkillMatrixEntity;
import com.intelegain.agora.model.SkillMatrixData;
import com.intelegain.agora.model.TechnologyData;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by suraj.m on 9/8/17.
 */

public interface WebApiInterface {

    /***************************** SURAJ SECTION *****************************************/

    /**
     * To get the holiday list from server
     *
     * @param empId Employee id
     * @param token Token number
     * @return JSon Array object containing holiday list data
     */
    @GET("getholiday")
    Call<HolidayMaster> getHolidayList(@Header("empid") String empId, @Header("token") String token);


    /* To get the Attendance data list from server
     */
    @POST("empattendance")
    Call<AttendanceMaster> getAttendanceData(@Header("empid") String empId,
                                             @Header("token") String token,
                                             @Body Map<String, String> params);

    /**
     * To get the leave data from server which includes available leaves data and applied leave data status
     *
     * @param empId  Employee id
     * @param token  Token number
     * @param params Body parameter
     * @return JSon Array object containing leave list data
     */

    @POST("getLeaveType")
    Call<ArrayList<Leave>> getLeaveType(@Header("empid") String empId,
                                        @Header("token") String token,
                                        @Body Map<String, String> params);
    /**
     * To apply leave to server
     * @param empId  Employee id
     * @param token  Token number
     * @param params Body parameter
     * @return Json object which include status and message for applied leave
     */
    @POST("applyLeave")
    Call<ApplyLeave> applyLeave(@Header("empid") String empId,
                                @Header("token") String token,
                                @Body Map<String, String> params);

    @POST("removeLeave")
    Call<CommonStatusMessage> deleteLeave(@Header("empid") String empId,
                                          @Header("token") String token,
                                          @Body Map<String, String> params);


    /**
     * To get the knowledge base data from server
     *
     * @param empId Employee id
     * @param token Token number
     */


    @POST("getknowledgebase")
    Call<KnowledgeMaster> getKnowledgeBaseData(@Header("empid") String empId,
                                               @Header("token") String token);


    /**
     * To add the knowledge base data to server
     *
     * @param empId  Employee id
     * @param token  Token number
     * @param params Body parameter
     */
    @POST("addKnowledge")
    Call<AddKnowledge> addKnowledge(@Header("empid") String empId,
                                    @Header("token") String token,
                                    @Body Map<String, String> params);


    /* To get the dashboard data from server
     */

    @POST("empdashboard")
    Call<DashboardMaster> getDashboardData(@Header("empid") String empId,
                                           @Header("token") String token,
                                           @Body Map<String, String> params);


    /**
     * To get available leaves
     *
     * @param empId  Employee id
     * @param token  Token number
     * @param params Body parameter
     * @return JSon Array object containing available leave data
     */

    @POST("availableLeavs")
    Call<ArrayList<AvailableLeave>> getAvailableLeave(@Header("empid") String empId,
                                                      @Header("token") String token,
                                                      @Body Map<String, String> params);


    /**
     * To get available leaves
     *
     * @param empId  Employee id
     * @param token  Token number
     * @param params Body parameter
     * @return JSon Array object containing available leave data
     */

    @POST("getLeaveDetails")
    Call<AppliedLeaveDetails> getAppliedLeaveDetails(@Header("empid") String empId,
                                                     @Header("token") String token,
                                                     @Body Map<String, String> params);

    /***************************** SURAJ SECTION END *****************************************/

    /************************************* MANI **********************************/
    /**
     *
     * */

//    @POST("login")
//    Call<Login> empLogin(@Body Map<String, String> params);

    @POST("getempprofile ")
    Call<EmployeeProfile> getEmployeeProfile(@Header("empid") String empId,
                                             @Header("token") String token,
                                             @Body Map<String, String> params);

    @POST("editprofile ")
    Call<CommonStatusMessage> editProfile(@Header("empid") String empId,
                                          @Header("token") String token,
                                          @Body Map<String, String> params);

    @POST("getcontacts")
    Call<List<Employee_contacts>> getAllEmployeeContacts(@Header("empid") String empId,
                                                         @Header("token") String token,
                                                         @Body Map<String, String> params);

    @POST("changePassword")
    Call<ChangePassword> changePassword(@Header("empid") String empId,
                                        @Header("token") String token,
                                        @Body Map<String, String> params);

    @Multipart
    @POST("imageupload")
    Call<ResponseBody> uploadProfileImage(@Header("empid") String empId,
                                          @Header("token") String token,
                                          @Body Map<String, String> params
            /*@Part MultipartBody.Part file*/);

    @POST("getproject")
    Call<KnowledgebaseProjectName> getProjectNameList(@Header("empid") String empId,
                                                      @Header("token") String token,
                                                      @Body Map<String, String> params);

    @POST("gettechnology")
    Call<TechnologyData> getTechnologyNameList(@Header("empid") String empId,
                                               @Header("token") String token,
                                               @Body Map<String, String> params);


    @POST("getskillmatrix")
    Call<SkillMatrixData> getSkillMatrix(@Header("empid") String empId,
                                         @Header("token") String token,
                                         @Body Map<String, String> params);


    @POST("saveskillmatrix")
    Call<CommonStatusMessage> saveSkillMatrix(@Header("empid") String empId,
                                              @Header("token") String token,
                                              @Body /*JSONObject params*/ SaveSkillMatrixEntity saveSkillMatrixEntity);

    @POST("login")
    Call<EmpLogin> empLogin(@Header("empid") String empId,
                            @Body Map<String, String> params);

    @POST("getdocument")
    Call<DocumentData> getDocument(@Header("empid") String empId,
                                   @Header("token") String token,
                                   @Body Map<String, String> params);


    @POST("empleavedetails")
    Call<LeaveMaster> getAllLeaveDetails(@Header("empid") String empId,
                                         @Header("token") String token,
                                         @Body Map<String, String> params);

    /************************************* MANI end**********************************/

    /*************************************Kalpesh ***********************************/

    @POST("getknowledgeattachment")
    Call<KnowdlegeAttachmentResponce> getKnowledgeAttachment(@Header("empid") String empId,
                                                             @Header("token") String token,
                                                             @Body Map<String, String> params);

    @Multipart
    @POST("addKnowledge")
    Call<KbAttchmentResponce> uploadFile(@Part MultipartBody.Part file, @Part("UploadedImage") RequestBody name,
                                         @Header("empid") String empId,
                                         @Header("token") String token);

    @POST("forgotPassword")
    Call<ForgotPasswordDetails> getForgotPassword(/*@Header("empid") String empId,
                                                  @Header("token") String token,*/
                                                  @Body Map<String, String> params);

}
