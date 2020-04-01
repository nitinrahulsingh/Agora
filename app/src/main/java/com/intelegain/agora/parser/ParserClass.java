package com.intelegain.agora.parser;

import android.util.Log;

import com.intelegain.agora.interfeces.Holidaylist;
import com.intelegain.agora.model.AddToFav;
import com.intelegain.agora.model.ApplyLeaveDetails;
import com.intelegain.agora.model.CalenderDataDetails;
import com.intelegain.agora.model.ChangePasswordDetails;
import com.intelegain.agora.model.CustomerData;
import com.intelegain.agora.model.ForgotPasswordDetails;
import com.intelegain.agora.model.GetAllLeaveStatusDetails;
import com.intelegain.agora.model.GetApproveLeaveDetails;
import com.intelegain.agora.model.GetAvailableLeavesDetails;
import com.intelegain.agora.model.GetDeleteAppliedLeaveDetails;
import com.intelegain.agora.model.GetFavEmpDeatils;
import com.intelegain.agora.model.GetHolidaysList;
import com.intelegain.agora.model.GetLeaveTypeDetails;
import com.intelegain.agora.model.GetLeavesForApprovalDetails;
import com.intelegain.agora.model.GetProjectTitleDetails;
import com.intelegain.agora.model.GetUnfavEmpDetails;
import com.intelegain.agora.model.HolidayListData;
import com.intelegain.agora.model.SearchContacts;
import com.intelegain.agora.model.SearchContactsDetails;
import com.intelegain.agora.model.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*********************************************************************************
 * This class is for getting response from webservice. Data is in JSON Format
 * includes Message, Status, data(this is in Array)
 ********************************************************************************/

public class ParserClass implements Holidaylist {
    String json = "";

    public ParserClass(String json) {
        this.json = json;
    }



    public UserData Signup_Parser(JSONObject jsonobject_inside) throws JSONException {
        UserData result = new UserData();
        if (json == null) {
            result.Error = "No response";
            return result;
        } else {
            try {


                    if (jsonobject_inside.length() != 0)
                    {


                            UserData userdata = new UserData();


                        String AccountNo = jsonobject_inside.getString("AccountNo");
                        String EmpId = jsonobject_inside.getString("EmployeeID");
                            userdata.JoiningDate = jsonobject_inside.getString("JoiningDate");
                            userdata.ConfirmationDate = jsonobject_inside.getString("empConfDate");
                            userdata.Name = jsonobject_inside.getString("Name");
                            userdata.EmployeeID = jsonobject_inside.getString("EmployeeID");
                            userdata.Address = jsonobject_inside.getString("Address");
                            userdata.Contact = jsonobject_inside.getString("Contact");
                            userdata.EmailID = jsonobject_inside.getString("EmailID");
                            userdata.ProbationPeriod = jsonobject_inside.getString("ProbationPeriod");
                            userdata.JoiningDate = jsonobject_inside.getString("JoiningDate");
                            userdata.AccountNo = jsonobject_inside.getString("AccountNo");
                            userdata.BDate = jsonobject_inside.getString("BDate");
                            userdata.Designation = jsonobject_inside.getString("Designation");
                          //  userdata.IsSuperAdmin = jsonobject_inside.getString("IsSuperAdmin");

                            result.user_login_data.add(userdata);

                        Log.e("TAG", " =========== " + AccountNo);
                        Log.e("TAG", " =========== " + EmpId);

                        System.out.println("Joining Date:- " + result.JoiningDate);
                        System.out.println("Address:- " + result.Address);
                        System.out.println("Contact:- " + result.Contact);
                        System.out.println("Account No:- " + result.AccountNo);
                        System.out.println("Email id:- " + result.EmailID);
                        System.out.println("Probation Period :- " + result.JoiningDate);
                        System.out.println("Confirmation Date:- " + result.ConfirmationDate);
                        System.out.println("Employee Id:- " + result.EmployeeID);
                        System.out.println("Name :- " + result.Name);
                        System.out.println("Designation :- " + result.Designation);
                        System.out.println("IsSuperAdmin :- " + result.IsSuperAdmin);
                    }




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public CustomerData customer_signup(JSONObject jsonobject_inside) throws JSONException {
        CustomerData result = new CustomerData();
        if (json == null) {
            result.Error = "No response";
            return result;
        } else {
            try {


                if (jsonobject_inside.length() != 0) {


                    CustomerData userdata = new CustomerData();



                    userdata.UserMasterID = jsonobject_inside.getString("UserMasterID");
                    userdata.FName = jsonobject_inside.getString("FName");
                    userdata.LName = jsonobject_inside.getString("LName");
                    userdata.Email = jsonobject_inside.getString("Email");


                    result.customer_login_data.add(userdata);



                    System.out.println("UserMasterIDUserMasterID:- " + result.UserMasterID);
                    System.out.println("UserMasterIDAddress:- " + result.FName);
                    System.out.println("UserMasterIDContact:- " + result.LName);
                    System.out.println("UserMasterIDAccount No:- " + result.Email);

                }




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * For getting contacts
     **/
    public SearchContacts get_search_contacts(JSONArray jsonArrayInside) {
        SearchContacts result = new SearchContacts();
        if (json == null) {
            result.Error = "No response";
            return result;
        } else {
            try {

                    if (jsonArrayInside.length() != 0) {

                        for (int i = 0; i < jsonArrayInside.length(); i++) {

                            JSONObject jsonObjectinside = jsonArrayInside.getJSONObject(i);

                            SearchContactsDetails search_contacts = new SearchContactsDetails();

                            search_contacts.emp_name = jsonObjectinside.getString("empName");
                            search_contacts.emp_id = Integer.parseInt(jsonObjectinside.getString("empid"));
                            search_contacts.phone_number = jsonObjectinside.getString("empContact");
                            search_contacts.email = jsonObjectinside.getString("empEmail");
                            search_contacts.flag = jsonObjectinside.getString("Flag");
                            Log.e("TAG", "== " + "search_contacts_details.get(position).phone_number");
                            result.search_contacts_arraylist.add(search_contacts);
                        }
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * For Adding to Favourite
     **/
    public AddToFav addToFav(JSONArray jsonInside) {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No responce";
        } else {
            try {
                //JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonInside.getJSONObject(0);

                result.Status = Integer.parseInt(jsonObject.getString("Status"));
                result.Message = jsonObject.getString("Message");

                Log.e("TAG", "Insert Status:" + Integer.parseInt(jsonObject.getString("Status")));
                Log.e("TAG", "Insert Message:" + jsonObject.getString("Message"));
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * For getting Favourite
     **/

    public AddToFav getFavEmp(JSONArray jsonArrayInside) {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No responce";
        } else {
            try {


                if (jsonArrayInside.length() != 0) {

                    for (int i = 0; i < jsonArrayInside.length(); i++) {

                        JSONObject jsonObjectfavcontact = jsonArrayInside.getJSONObject(i);

                        GetFavEmpDeatils getfavempdetails = new GetFavEmpDeatils();

                        getfavempdetails.FavouriteEmpId = Integer.parseInt(jsonObjectfavcontact.getString("empid"));
                        getfavempdetails.FavouriteEmpName = jsonObjectfavcontact.getString("empName");
                        getfavempdetails.FavouriteEmpContact = jsonObjectfavcontact.getString("empContact");
                        getfavempdetails.FavouriteEmpEmail = jsonObjectfavcontact.getString("empEmail");
                        getfavempdetails.FavouriteFlag = jsonObjectfavcontact.getString("Flag");
                        result.getfavemp_arraylist.add(getfavempdetails);
                    }
                }

            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * For getting Timesheet Dates
     **/

    public AddToFav calenderDate(JSONArray jsonArrayInside) {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No responce";
        } else {
            try {

                if (jsonArrayInside.length() != 0) {
                        for (int i = 0; i < jsonArrayInside.length(); i++) {

                            JSONObject jsonobject_data = jsonArrayInside.getJSONObject(i);

                            CalenderDataDetails calenderdataDetails = new CalenderDataDetails();


                            calenderdataDetails.Discription = jsonobject_data.getString("description");
                            calenderdataDetails.AttendanceStatus = jsonobject_data.getString("attStatus");
                            calenderdataDetails.AttInTime = jsonobject_data.getString("attInTime");
                            calenderdataDetails.AttOutTime = jsonobject_data.getString("attOutTime");
                            calenderdataDetails.WorkingHours = jsonobject_data.getString("workinghours");
                            calenderdataDetails.MissedDate = jsonobject_data.getString("attendanceDate");
                            calenderdataDetails.TimesheetHours = jsonobject_data.getString("timesheethours");
                            calenderdataDetails.Flag = jsonobject_data.getString("description");
                            result.gettimsheetdates_arraylist.add(calenderdataDetails);
                            System.out.println("CallenderMissedDate " + calenderdataDetails.MissedDate);
                        }

                    }
                } catch (JSONException e) {
                e.printStackTrace();
            }


    }
        return result;}

    public AddToFav getAvailableLeaves(JSONArray jsonArrayInside) {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No responce";
        } else {
            try {

                if (jsonArrayInside.length() != 0) {
                    for (int i = 0; i < jsonArrayInside.length(); i++) {

                        JSONObject jsonObjectInside = jsonArrayInside.getJSONObject(i);

                        GetAvailableLeavesDetails availableLeavesDetails = new GetAvailableLeavesDetails();


                        availableLeavesDetails.BalanceCL = jsonObjectInside.getString("BalanceCL");
                        availableLeavesDetails.BalancePL = jsonObjectInside.getString("BalancePL");
                        availableLeavesDetails.BalanceSL = jsonObjectInside.getString("BalanceSL");
                        availableLeavesDetails.BalanceCO = jsonObjectInside.getString("BalanceCO");
                        availableLeavesDetails.ConsumedCL = jsonObjectInside.getString("ConsumedCL");
                        availableLeavesDetails.ConsumedCO = jsonObjectInside.getString("ConsumedCO");
                        availableLeavesDetails.ConsumedPL = jsonObjectInside.getString("ConsumedPL");
                        availableLeavesDetails.ConsumedSL = jsonObjectInside.getString("ConsumedSL");
                        availableLeavesDetails.TotalCL = jsonObjectInside.getString("TotalCL");
                        availableLeavesDetails.TotalCO = jsonObjectInside.getString("TotalCO");
                        availableLeavesDetails.TotalPL = jsonObjectInside.getString("TotalPL");
                        availableLeavesDetails.TotalSL = jsonObjectInside.getString("TotalSL");


                        result.getAvailableLeaves_arraylist.add(availableLeavesDetails);
                        System.out.println("RESULT:- " + result.getAvailableLeaves_arraylist);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public AddToFav getLeaveType(JSONArray jsonArrayInside) {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No Responce";
        } else {
            try {


                for (int i = 0; i < jsonArrayInside.length(); i++) {
                    JSONObject jsonObjectInside = jsonArrayInside.getJSONObject(i);
                    GetLeaveTypeDetails getLeaveTypeDetails = new GetLeaveTypeDetails();
                    getLeaveTypeDetails.statusID = jsonObjectInside.getString("Leave").toString();
                    getLeaveTypeDetails.statusDescription = jsonObjectInside.getString("LeaveDesc");
                    result.getLeaveType_arraylist.add(getLeaveTypeDetails);
                    System.out.println("RESULT:- " + result.getLeaveType_arraylist);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public AddToFav insertleaves() {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No Responce";
        } else {
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                result.Status = jsonObject.getInt("Status");
                result.Message = jsonObject.getString("Message");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public AddToFav getLeaveStatus(JSONArray jsonArrayInside) {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No Responce";
        } else {
            try {
              /*  JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                result.Status = jsonObject.getInt("Status");
                result.Message = jsonObject.getString("Message");

                JSONArray jsonArrayInside = jsonObject.getJSONArray("lstLeaveStatus");*/

                for (int i = 0; i < jsonArrayInside.length(); i++) {
                    JSONObject jsonObjectInside = jsonArrayInside.getJSONObject(i);

                    GetAllLeaveStatusDetails getLeaveStatusDetails = new GetAllLeaveStatusDetails();

					/*getLeaveStatusDetails.apply_date = jsonObjectInside.getString("");
                    getLeaveStatusDetails.leave_status = jsonObjectInside.getString("");*/
                    getLeaveStatusDetails.leaveFromDate = jsonObjectInside.getString("LeaveFrom").toString();
                    getLeaveStatusDetails.leaveDesc = jsonObjectInside.getString("LeaveDesc").toString();
                    getLeaveStatusDetails.leaveToDate = jsonObjectInside.getString("LeaveTo").toString();
                    getLeaveStatusDetails.leaveType = jsonObjectInside.getString("Leave").toString();
                    getLeaveStatusDetails.statusDesc = jsonObjectInside.getString("LeaveStatus").toString();
                    getLeaveStatusDetails.adminComment = jsonObjectInside.getString("AdminComments").toString();
                    getLeaveStatusDetails.LeaveId = jsonObjectInside.getString("ID").toString();
                    result.getleaveStatus_arraylist.add(getLeaveStatusDetails);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public AddToFav getLeaveForApproval() {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No Responcse";
        } else {
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonobject = jsonArray.getJSONObject(0);

                result.Status = jsonobject.getInt("Status");
                result.Message = jsonobject.getString("Message");

                JSONArray jsonArrayInside = jsonobject.getJSONArray("");

                for (int i = 0; i < jsonArrayInside.length(); i++) {
                    JSONObject jsonObjectInside = jsonArrayInside.getJSONObject(i);
                    GetLeavesForApprovalDetails getLeaveForApprovalDetails = new GetLeavesForApprovalDetails();

                    getLeaveForApprovalDetails.apply_date = jsonObjectInside.getString("");
                    getLeaveForApprovalDetails.applyed_emp_id = jsonObjectInside.getString("");
                    getLeaveForApprovalDetails.leave_status = jsonObjectInside.getString("");

                    result.getleaveForApproval_arraylist.add(getLeaveForApprovalDetails);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Object forgotPassword(JSONObject json) {
        ForgotPasswordDetails result = new ForgotPasswordDetails();
        if (json == null) {
            result.Error = "No response";
            return result;
        } else {
            try {
                //JSONArray jsonArray = new JSONArray(json);


				/*String msg = jsonobject.getString("Message");
				String Status = jsonobject.getString("Status");
*/
                result.Status = Integer.parseInt(json.get("Status").toString());
                result.Message = json.get("Message").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Object changePassword(JSONObject jsonArrayInside) {

        ChangePasswordDetails result = new ChangePasswordDetails();
        if (json == null) {
            result.Error = "No response";
            return result;
        } else {
            try {
               /*// JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonobject = jsonArrayInside.getJSONObject(0);*/


                result.Status = Integer.parseInt(jsonArrayInside.get("Status").toString());
                result.Message = jsonArrayInside.get("Message").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Object applyLeave(JSONArray jsonArrayInside) {
        ApplyLeaveDetails result = new ApplyLeaveDetails();
        if (json == null) {
            result.Error = "No response";
            return result;
        } else {
            try {
                //JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonobject = jsonArrayInside.getJSONObject(0);
                result.Status = Integer.parseInt(jsonobject.get("Status").toString());
                result.Message = jsonobject.get("Message").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;


    }

    public Object getApproveLeaveStatus() {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No Responce";
        } else {
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);


                result.StatusForLeaveType = jsonObject.getString("Status");
                result.Message = jsonObject.getString("Message");

                JSONArray jsonArrayInside = jsonObject.getJSONArray("lstLeaveRequests");

                for (int i = 0; i < jsonArrayInside.length(); i++) {
                    JSONObject jsonObjectInside = jsonArrayInside.getJSONObject(i);
                    GetLeavesForApprovalDetails getLeaveforapprovalDetails = new GetLeavesForApprovalDetails();
                    getLeaveforapprovalDetails.adminComment = jsonObjectInside.getString("adminComment").toString();
                    getLeaveforapprovalDetails.emp_id = jsonObjectInside.getInt("empId");
                    getLeaveforapprovalDetails.empName = jsonObjectInside.getString("empName").toString();
                    getLeaveforapprovalDetails.leaveDesc = jsonObjectInside.getString("leaveDesc").toString();
                    getLeaveforapprovalDetails.leaveFromDate = jsonObjectInside.getString("leaveFromDate").toString();
                    getLeaveforapprovalDetails.leaveToDate = jsonObjectInside.getString("leaveToDate").toString();
                    getLeaveforapprovalDetails.leaveType = jsonObjectInside.getString("leaveType").toString();
                    getLeaveforapprovalDetails.statusDesc = jsonObjectInside.getString("statusDesc").toString();
                    getLeaveforapprovalDetails.empLeaveId = jsonObjectInside.getString("empLeaveId").toString();

                    //	result.JoiningDate =jsonobject_inside.getString("JoiningDate");
                    result.getleaveForApproval_arraylist.add(getLeaveforapprovalDetails);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Object getPendingLeaveStatus() {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No Responce";
        } else {
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);


                result.StatusForLeaveType = jsonObject.getString("Status");
                result.Message = jsonObject.getString("Message");

                JSONArray jsonArrayInside = jsonObject.getJSONArray("lstLeaveRequests");

                for (int i = 0; i < jsonArrayInside.length(); i++) {
                    JSONObject jsonObjectInside = jsonArrayInside.getJSONObject(i);
                    GetLeavesForApprovalDetails getLeaveforapprovalDetails = new GetLeavesForApprovalDetails();
                    getLeaveforapprovalDetails.adminComment = jsonObjectInside.getString("adminComment").toString();
                    getLeaveforapprovalDetails.emp_id = jsonObjectInside.getInt("empId");
                    //	getLeaveforapprovalDetails.emp_id = jsonObjectInside.getString("empId").toString();
                    getLeaveforapprovalDetails.empName = jsonObjectInside.getString("empName").toString();
                    getLeaveforapprovalDetails.leaveDesc = jsonObjectInside.getString("leaveDesc").toString();
                    getLeaveforapprovalDetails.leaveFromDate = jsonObjectInside.getString("leaveFromDate").toString();
                    getLeaveforapprovalDetails.leaveToDate = jsonObjectInside.getString("leaveToDate").toString();
                    getLeaveforapprovalDetails.leaveType = jsonObjectInside.getString("leaveType").toString();
                    getLeaveforapprovalDetails.statusDesc = jsonObjectInside.getString("statusDesc").toString();
                    String status = getLeaveforapprovalDetails.statusDesc.toString();
                    System.out.println(" Status:- " + status);
                    if (status.equals("Pending")) {
                        result.getleaveForApproval_arraylist.add(getLeaveforapprovalDetails);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Object getRejectedLeaveStatus() {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No Responce";
        } else {
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);


                result.StatusForLeaveType = jsonObject.getString("Status");
                result.Message = jsonObject.getString("Message");

                JSONArray jsonArrayInside = jsonObject.getJSONArray("lstLeaveRequests");

                for (int i = 0; i < jsonArrayInside.length(); i++) {
                    JSONObject jsonObjectInside = jsonArrayInside.getJSONObject(i);
                    GetLeavesForApprovalDetails getLeaveforapprovalDetails = new GetLeavesForApprovalDetails();
                    getLeaveforapprovalDetails.adminComment = jsonObjectInside.getString("adminComment").toString();
                    getLeaveforapprovalDetails.emp_id = jsonObjectInside.getInt("empId");
                    //	getLeaveforapprovalDetails.emp_id = jsonObjectInside.getString("empId").toString();
                    getLeaveforapprovalDetails.empName = jsonObjectInside.getString("empName").toString();
                    getLeaveforapprovalDetails.leaveDesc = jsonObjectInside.getString("leaveDesc").toString();
                    getLeaveforapprovalDetails.leaveFromDate = jsonObjectInside.getString("leaveFromDate").toString();
                    getLeaveforapprovalDetails.leaveToDate = jsonObjectInside.getString("leaveToDate").toString();
                    getLeaveforapprovalDetails.leaveType = jsonObjectInside.getString("leaveType").toString();
                    getLeaveforapprovalDetails.statusDesc = jsonObjectInside.getString("statusDesc").toString();
                    String status = getLeaveforapprovalDetails.statusDesc.toString();
                    System.out.println(" Status:- " + status);

                    String rejectStatus = getLeaveforapprovalDetails.statusDesc.toString();

                    if (rejectStatus.equals("Rejected")) {
                        result.getleaveForApproval_arraylist.add(getLeaveforapprovalDetails);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Object getApprovedLeaveStatus() {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No Responce";
        } else {
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);


                result.StatusForLeaveType = jsonObject.getString("Status");
                result.Message = jsonObject.getString("Message");

                JSONArray jsonArrayInside = jsonObject.getJSONArray("lstLeaveRequests");

                for (int i = 0; i < jsonArrayInside.length(); i++) {
                    JSONObject jsonObjectInside = jsonArrayInside.getJSONObject(i);
                    GetLeavesForApprovalDetails getLeaveforapprovalDetails = new GetLeavesForApprovalDetails();
                    getLeaveforapprovalDetails.adminComment = jsonObjectInside.getString("adminComment").toString();
                    getLeaveforapprovalDetails.emp_id = jsonObjectInside.getInt("empId");

                    getLeaveforapprovalDetails.empName = jsonObjectInside.getString("empName").toString();
                    getLeaveforapprovalDetails.leaveDesc = jsonObjectInside.getString("leaveDesc").toString();
                    getLeaveforapprovalDetails.leaveFromDate = jsonObjectInside.getString("leaveFromDate").toString();
                    getLeaveforapprovalDetails.leaveToDate = jsonObjectInside.getString("leaveToDate").toString();
                    getLeaveforapprovalDetails.leaveType = jsonObjectInside.getString("leaveType").toString();
                    getLeaveforapprovalDetails.statusDesc = jsonObjectInside.getString("statusDesc").toString();

                    String aprrovedStatus = getLeaveforapprovalDetails.statusDesc.toString();


                    if (aprrovedStatus.equals("Approved")) {
                        result.getleaveForApproval_arraylist.add(getLeaveforapprovalDetails);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Object removeFromFav(JSONArray jsonArrayInside) {
        GetUnfavEmpDetails result = new GetUnfavEmpDetails();
        if (json == null) {
            result.Error = "No response";
            return result;
        } else {
            try {
               // JSONArray jsonArrayInside = new JSONArray(json);
                JSONObject jsonobject = jsonArrayInside.getJSONObject(0);
                result.Status = Integer.parseInt(jsonobject.get("Status").toString());
                result.Message = jsonobject.get("Message").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;


    }

    public Object LeaveApproval() {
        GetApproveLeaveDetails result = new GetApproveLeaveDetails();
        if (json == null) {
            result.Error = "No response";
            return result;
        } else {
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonobject = jsonArray.getJSONObject(0);
                result.Status = Integer.parseInt(jsonobject.get("Status").toString());
                result.Message = jsonobject.get("Message").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    public Object GetDeleteLeaveStatus(JSONArray jsonArrayInside) {

        GetDeleteAppliedLeaveDetails result = new GetDeleteAppliedLeaveDetails();
        if (json == null) {
            result.Error = "No response";
            return result;
        } else {
            try {
               // JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonobject = jsonArrayInside.getJSONObject(0);
                result.Status = Integer.parseInt(jsonobject.get("Status").toString());
                result.Message = jsonobject.get("Message").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /*public Object getModuleName()
    {
        // TODO Auto-generated method stub
        return null;
    } */
    public Object getProjectTitle() {
        AddToFav result = new AddToFav();
        if (json == null) {
            result.Error = "No Responce";
        } else {
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);


                //	result.Status = jsonObject.getString("Status");
                result.Status = jsonObject.getInt("Status");
                result.Message = jsonObject.getString("Message");

                JSONArray jsonArrayInside = jsonObject.getJSONArray("lstDropDownData");

                for (int i = 0; i < jsonArrayInside.length(); i++) {
                    JSONObject jsonObjectInside = jsonArrayInside.getJSONObject(i);
                    GetProjectTitleDetails getProjectTitleDetails = new GetProjectTitleDetails();
                    getProjectTitleDetails.projectID = jsonObjectInside.getString("projectID").toString();
                    getProjectTitleDetails.projectName = jsonObjectInside.getString("projectName");
                    result.getProjectTitle_arraylist.add(getProjectTitleDetails);
                    System.out.println("RESULT:- " + result.getProjectTitle_arraylist);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * For getting contacts
     **/
    public GetHolidaysList get_holiday_list(JSONArray jsonArrayInside) {


        Log.d("json11size", "json11size" + jsonArrayInside.length());
        GetHolidaysList result = new GetHolidaysList();

        if (json == null) {
            result.Error = "No response";
            return result;
        } else {
            try {
               /* JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);*/

               /* result.Status = Integer.parseInt(jsonObject.get("Status").toString());
                result.Message = jsonObject.get("Message").toString();*/


                //JSONArray jsonArrayInside = jsonObject.getJSONArray("");

                // if (Integer.parseInt(jsonObject.getString("Status")) == 0) {

                if (jsonArrayInside.length() != 0) {

                    for (int i = 0; i < jsonArrayInside.length(); i++) {

                        JSONObject jsonObjectinside = jsonArrayInside.getJSONObject(i);
                        HolidayListData holiday_data = new HolidayListData();
                        holiday_data.HolidayDate = jsonObjectinside.getString("HolidayDate");
                        holiday_data.HolidayId = jsonObjectinside.getString("HolidayId");
                        holiday_data.Location = jsonObjectinside.getString("Location");
                        holiday_data.LocationId = jsonObjectinside.getString("LocationId");
                        holiday_data.Narration = jsonObjectinside.getString("Narration");
                        result.holiday_arraylist.add(holiday_data);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void holydaylistJsonList(JSONArray filterArrayList) {


        JSONArray jsonArray = new JSONArray();

        jsonArray = filterArrayList;


        Log.d("filterArrayListqqqqqqq", "" + jsonArray);


    }
}
