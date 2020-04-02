package com.intelegain.agora.parser

import android.util.Log
import com.intelegain.agora.interfeces.Holidaylist
import com.intelegain.agora.model.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/*********************************************************************************
 * This class is for getting response from webservice. Data is in JSON Format
 * includes Message, Status, data(this is in Array)
 */
class ParserClass(json: String?) : Holidaylist {
    var json: String? = ""
    @Throws(JSONException::class)
    fun Signup_Parser(jsonobject_inside: JSONObject): UserData {
        val result = UserData()
        if (json == null) {
            result.Error = "No response"
            return result
        } else {
            try {
                if (jsonobject_inside.length() != 0) {
                    val userdata = UserData()
                    val AccountNo = jsonobject_inside.getString("AccountNo")
                    val EmpId = jsonobject_inside.getString("EmployeeID")
                    userdata.JoiningDate = jsonobject_inside.getString("JoiningDate")
                    userdata.ConfirmationDate = jsonobject_inside.getString("empConfDate")
                    userdata.Name = jsonobject_inside.getString("Name")
                    userdata.EmployeeID = jsonobject_inside.getString("EmployeeID")
                    userdata.Address = jsonobject_inside.getString("Address")
                    userdata.Contact = jsonobject_inside.getString("Contact")
                    userdata.EmailID = jsonobject_inside.getString("EmailID")
                    userdata.ProbationPeriod = jsonobject_inside.getString("ProbationPeriod")
                    userdata.JoiningDate = jsonobject_inside.getString("JoiningDate")
                    userdata.AccountNo = jsonobject_inside.getString("AccountNo")
                    userdata.BDate = jsonobject_inside.getString("BDate")
                    userdata.Designation = jsonobject_inside.getString("Designation")
                    //  userdata.IsSuperAdmin = jsonobject_inside.getString("IsSuperAdmin");
                    result.user_login_data.add(userdata)
                    Log.e("TAG", " =========== $AccountNo")
                    Log.e("TAG", " =========== $EmpId")
                    println("Joining Date:- " + result.JoiningDate)
                    println("Address:- " + result.Address)
                    println("Contact:- " + result.Contact)
                    println("Account No:- " + result.AccountNo)
                    println("Email id:- " + result.EmailID)
                    println("Probation Period :- " + result.JoiningDate)
                    println("Confirmation Date:- " + result.ConfirmationDate)
                    println("Employee Id:- " + result.EmployeeID)
                    println("Name :- " + result.Name)
                    println("Designation :- " + result.Designation)
                    println("IsSuperAdmin :- " + result.IsSuperAdmin)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return result
    }

    @Throws(JSONException::class)
    fun customer_signup(jsonobject_inside: JSONObject): CustomerData {
        val result = CustomerData()
        if (json == null) {
            result.Error = "No response"
            return result
        } else {
            try {
                if (jsonobject_inside.length() != 0) {
                    val userdata = CustomerData()
                    userdata.UserMasterID = jsonobject_inside.getString("UserMasterID")
                    userdata.FName = jsonobject_inside.getString("FName")
                    userdata.LName = jsonobject_inside.getString("LName")
                    userdata.Email = jsonobject_inside.getString("Email")
                    result.customer_login_data.add(userdata)
                    println("UserMasterIDUserMasterID:- " + result.UserMasterID)
                    println("UserMasterIDAddress:- " + result.FName)
                    println("UserMasterIDContact:- " + result.LName)
                    println("UserMasterIDAccount No:- " + result.Email)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return result
    }

    /**
     * For getting contacts
     */
    fun get_search_contacts(jsonArrayInside: JSONArray): SearchContacts {
        val result = SearchContacts()
        if (json == null) {
            result.Error = "No response"
            return result
        } else {
            try {
                if (jsonArrayInside.length() != 0) {
                    for (i in 0 until jsonArrayInside.length()) {
                        val jsonObjectinside = jsonArrayInside.getJSONObject(i)
                        val search_contacts = SearchContactsDetails()
                        search_contacts.emp_name = jsonObjectinside.getString("empName")
                        search_contacts.emp_id = jsonObjectinside.getString("empid").toInt()
                        search_contacts.phone_number = jsonObjectinside.getString("empContact")
                        search_contacts.email = jsonObjectinside.getString("empEmail")
                        search_contacts.flag = jsonObjectinside.getString("Flag")
                        Log.e("TAG", "== " + "search_contacts_details.get(position).phone_number")
                        result.search_contacts_arraylist.add(search_contacts)
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return result
    }

    /**
     * For Adding to Favourite
     */
    fun addToFav(jsonInside: JSONArray): AddToFav {
        val result = AddToFav()
        if (json == null) {
            result.Error = "No responce"
        } else {
            try { //JSONArray jsonArray = new JSONArray(json);
                val jsonObject = jsonInside.getJSONObject(0)
                result.Status = jsonObject.getString("Status").toInt()
                result.Message = jsonObject.getString("Message")
                Log.e("TAG", "Insert Status:" + jsonObject.getString("Status").toInt())
                Log.e("TAG", "Insert Message:" + jsonObject.getString("Message"))
            } catch (e: Exception) {
            }
        }
        return result
    }

    /**
     * For getting Favourite
     */
    fun getFavEmp(jsonArrayInside: JSONArray): AddToFav {
        val result = AddToFav()
        if (json == null) {
            result.Error = "No responce"
        } else {
            try {
                if (jsonArrayInside.length() != 0) {
                    for (i in 0 until jsonArrayInside.length()) {
                        val jsonObjectfavcontact = jsonArrayInside.getJSONObject(i)
                        val getfavempdetails = GetFavEmpDeatils()
                        getfavempdetails.FavouriteEmpId = jsonObjectfavcontact.getString("empid").toInt()
                        getfavempdetails.FavouriteEmpName = jsonObjectfavcontact.getString("empName")
                        getfavempdetails.FavouriteEmpContact = jsonObjectfavcontact.getString("empContact")
                        getfavempdetails.FavouriteEmpEmail = jsonObjectfavcontact.getString("empEmail")
                        getfavempdetails.FavouriteFlag = jsonObjectfavcontact.getString("Flag")
                        result.getfavemp_arraylist.add(getfavempdetails)
                    }
                }
            } catch (e: Exception) {
            }
        }
        return result
    }

    /**
     * For getting Timesheet Dates
     */
    fun calenderDate(jsonArrayInside: JSONArray): AddToFav {
        val result = AddToFav()
        if (json == null) {
            result.Error = "No responce"
        } else {
            try {
                if (jsonArrayInside.length() != 0) {
                    for (i in 0 until jsonArrayInside.length()) {
                        val jsonobject_data = jsonArrayInside.getJSONObject(i)
                        val calenderdataDetails = CalenderDataDetails()
                        calenderdataDetails.Discription = jsonobject_data.getString("description")
                        calenderdataDetails.AttendanceStatus = jsonobject_data.getString("attStatus")
                        calenderdataDetails.AttInTime = jsonobject_data.getString("attInTime")
                        calenderdataDetails.AttOutTime = jsonobject_data.getString("attOutTime")
                        calenderdataDetails.WorkingHours = jsonobject_data.getString("workinghours")
                        calenderdataDetails.MissedDate = jsonobject_data.getString("attendanceDate")
                        calenderdataDetails.TimesheetHours = jsonobject_data.getString("timesheethours")
                        calenderdataDetails.Flag = jsonobject_data.getString("description")
                        result.gettimsheetdates_arraylist.add(calenderdataDetails)
                        println("CallenderMissedDate " + calenderdataDetails.MissedDate)
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun getAvailableLeaves(jsonArrayInside: JSONArray): AddToFav {
        val result = AddToFav()
        if (json == null) {
            result.Error = "No responce"
        } else {
            try {
                if (jsonArrayInside.length() != 0) {
                    for (i in 0 until jsonArrayInside.length()) {
                        val jsonObjectInside = jsonArrayInside.getJSONObject(i)
                        val availableLeavesDetails = GetAvailableLeavesDetails()
                        availableLeavesDetails.BalanceCL = jsonObjectInside.getString("BalanceCL")
                        availableLeavesDetails.BalancePL = jsonObjectInside.getString("BalancePL")
                        availableLeavesDetails.BalanceSL = jsonObjectInside.getString("BalanceSL")
                        availableLeavesDetails.BalanceCO = jsonObjectInside.getString("BalanceCO")
                        availableLeavesDetails.ConsumedCL = jsonObjectInside.getString("ConsumedCL")
                        availableLeavesDetails.ConsumedCO = jsonObjectInside.getString("ConsumedCO")
                        availableLeavesDetails.ConsumedPL = jsonObjectInside.getString("ConsumedPL")
                        availableLeavesDetails.ConsumedSL = jsonObjectInside.getString("ConsumedSL")
                        availableLeavesDetails.TotalCL = jsonObjectInside.getString("TotalCL")
                        availableLeavesDetails.TotalCO = jsonObjectInside.getString("TotalCO")
                        availableLeavesDetails.TotalPL = jsonObjectInside.getString("TotalPL")
                        availableLeavesDetails.TotalSL = jsonObjectInside.getString("TotalSL")
                        result.getAvailableLeaves_arraylist.add(availableLeavesDetails)
                        println("RESULT:- " + result.getAvailableLeaves_arraylist)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun getLeaveType(jsonArrayInside: JSONArray): AddToFav {
        val result = AddToFav()
        if (json == null) {
            result.Error = "No Responce"
        } else {
            try {
                for (i in 0 until jsonArrayInside.length()) {
                    val jsonObjectInside = jsonArrayInside.getJSONObject(i)
                    val getLeaveTypeDetails = GetLeaveTypeDetails()
                    getLeaveTypeDetails.statusID = jsonObjectInside.getString("Leave").toString()
                    getLeaveTypeDetails.statusDescription = jsonObjectInside.getString("LeaveDesc")
                    result.getLeaveType_arraylist.add(getLeaveTypeDetails)
                    println("RESULT:- " + result.getLeaveType_arraylist)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun insertleaves(): AddToFav {
        val result = AddToFav()
        if (json == null) {
            result.Error = "No Responce"
        } else {
            try {
                val jsonArray = JSONArray(json)
                val jsonObject = jsonArray.getJSONObject(0)
                result.Status = jsonObject.getInt("Status")
                result.Message = jsonObject.getString("Message")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun getLeaveStatus(jsonArrayInside: JSONArray): AddToFav {
        val result = AddToFav()
        if (json == null) {
            result.Error = "No Responce"
        } else {
            try { /*  JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                result.Status = jsonObject.getInt("Status");
                result.Message = jsonObject.getString("Message");

                JSONArray jsonArrayInside = jsonObject.getJSONArray("lstLeaveStatus");*/
                for (i in 0 until jsonArrayInside.length()) {
                    val jsonObjectInside = jsonArrayInside.getJSONObject(i)
                    val getLeaveStatusDetails = GetAllLeaveStatusDetails()
                    /*getLeaveStatusDetails.apply_date = jsonObjectInside.getString("");
                    getLeaveStatusDetails.leave_status = jsonObjectInside.getString("");*/getLeaveStatusDetails.leaveFromDate = jsonObjectInside.getString("LeaveFrom").toString()
                    getLeaveStatusDetails.leaveDesc = jsonObjectInside.getString("LeaveDesc").toString()
                    getLeaveStatusDetails.leaveToDate = jsonObjectInside.getString("LeaveTo").toString()
                    getLeaveStatusDetails.leaveType = jsonObjectInside.getString("Leave").toString()
                    getLeaveStatusDetails.statusDesc = jsonObjectInside.getString("LeaveStatus").toString()
                    getLeaveStatusDetails.adminComment = jsonObjectInside.getString("AdminComments").toString()
                    getLeaveStatusDetails.LeaveId = jsonObjectInside.getString("ID").toString()
                    result.getleaveStatus_arraylist.add(getLeaveStatusDetails)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    val leaveForApproval: AddToFav
        get() {
            val result = AddToFav()
            if (json == null) {
                result.Error = "No Responcse"
            } else {
                try {
                    val jsonArray = JSONArray(json)
                    val jsonobject = jsonArray.getJSONObject(0)
                    result.Status = jsonobject.getInt("Status")
                    result.Message = jsonobject.getString("Message")
                    val jsonArrayInside = jsonobject.getJSONArray("")
                    for (i in 0 until jsonArrayInside.length()) {
                        val jsonObjectInside = jsonArrayInside.getJSONObject(i)
                        val getLeaveForApprovalDetails = GetLeavesForApprovalDetails()
                        getLeaveForApprovalDetails.apply_date = jsonObjectInside.getString("")
                        getLeaveForApprovalDetails.applyed_emp_id = jsonObjectInside.getString("")
                        getLeaveForApprovalDetails.leave_status = jsonObjectInside.getString("")
                        result.getleaveForApproval_arraylist.add(getLeaveForApprovalDetails)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return result
        }

    fun forgotPassword(json: JSONObject?): Any {
        val result = ForgotPasswordDetails()
        if (json == null) {
            result.Error = "No response"
            return result
        } else {
            try { //JSONArray jsonArray = new JSONArray(json);
/*String msg = jsonobject.getString("Message");
				String Status = jsonobject.getString("Status");
*/
                result.Status = json["Status"].toString().toInt()
                result.Message = json["Message"].toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun changePassword(jsonArrayInside: JSONObject): Any {
        val result = ChangePasswordDetails()
        if (json == null) {
            result.Error = "No response"
            return result
        } else {
            try { /*// JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonobject = jsonArrayInside.getJSONObject(0);*/
                result.Status = jsonArrayInside["Status"].toString().toInt()
                result.Message = jsonArrayInside["Message"].toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun applyLeave(jsonArrayInside: JSONArray): Any {
        val result = ApplyLeaveDetails()
        if (json == null) {
            result.Error = "No response"
            return result
        } else {
            try { //JSONArray jsonArray = new JSONArray(json);
                val jsonobject = jsonArrayInside.getJSONObject(0)
                result.Status = jsonobject["Status"].toString().toInt()
                result.Message = jsonobject["Message"].toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return result
    }

    //	result.JoiningDate =jsonobject_inside.getString("JoiningDate");
    val approveLeaveStatus: Any
        get() {
            val result = AddToFav()
            if (json == null) {
                result.Error = "No Responce"
            } else {
                try {
                    val jsonArray = JSONArray(json)
                    val jsonObject = jsonArray.getJSONObject(0)
                    result.StatusForLeaveType = jsonObject.getString("Status")
                    result.Message = jsonObject.getString("Message")
                    val jsonArrayInside = jsonObject.getJSONArray("lstLeaveRequests")
                    for (i in 0 until jsonArrayInside.length()) {
                        val jsonObjectInside = jsonArrayInside.getJSONObject(i)
                        val getLeaveforapprovalDetails = GetLeavesForApprovalDetails()
                        getLeaveforapprovalDetails.adminComment = jsonObjectInside.getString("adminComment").toString()
                        getLeaveforapprovalDetails.emp_id = jsonObjectInside.getInt("empId")
                        getLeaveforapprovalDetails.empName = jsonObjectInside.getString("empName").toString()
                        getLeaveforapprovalDetails.leaveDesc = jsonObjectInside.getString("leaveDesc").toString()
                        getLeaveforapprovalDetails.leaveFromDate = jsonObjectInside.getString("leaveFromDate").toString()
                        getLeaveforapprovalDetails.leaveToDate = jsonObjectInside.getString("leaveToDate").toString()
                        getLeaveforapprovalDetails.leaveType = jsonObjectInside.getString("leaveType").toString()
                        getLeaveforapprovalDetails.statusDesc = jsonObjectInside.getString("statusDesc").toString()
                        getLeaveforapprovalDetails.empLeaveId = jsonObjectInside.getString("empLeaveId").toString()
                        //	result.JoiningDate =jsonobject_inside.getString("JoiningDate");
                        result.getleaveForApproval_arraylist.add(getLeaveforapprovalDetails)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return result
        }

    //	getLeaveforapprovalDetails.emp_id = jsonObjectInside.getString("empId").toString();
    val pendingLeaveStatus: Any
        get() {
            val result = AddToFav()
            if (json == null) {
                result.Error = "No Responce"
            } else {
                try {
                    val jsonArray = JSONArray(json)
                    val jsonObject = jsonArray.getJSONObject(0)
                    result.StatusForLeaveType = jsonObject.getString("Status")
                    result.Message = jsonObject.getString("Message")
                    val jsonArrayInside = jsonObject.getJSONArray("lstLeaveRequests")
                    for (i in 0 until jsonArrayInside.length()) {
                        val jsonObjectInside = jsonArrayInside.getJSONObject(i)
                        val getLeaveforapprovalDetails = GetLeavesForApprovalDetails()
                        getLeaveforapprovalDetails.adminComment = jsonObjectInside.getString("adminComment").toString()
                        getLeaveforapprovalDetails.emp_id = jsonObjectInside.getInt("empId")
                        //	getLeaveforapprovalDetails.emp_id = jsonObjectInside.getString("empId").toString();
                        getLeaveforapprovalDetails.empName = jsonObjectInside.getString("empName").toString()
                        getLeaveforapprovalDetails.leaveDesc = jsonObjectInside.getString("leaveDesc").toString()
                        getLeaveforapprovalDetails.leaveFromDate = jsonObjectInside.getString("leaveFromDate").toString()
                        getLeaveforapprovalDetails.leaveToDate = jsonObjectInside.getString("leaveToDate").toString()
                        getLeaveforapprovalDetails.leaveType = jsonObjectInside.getString("leaveType").toString()
                        getLeaveforapprovalDetails.statusDesc = jsonObjectInside.getString("statusDesc").toString()
                        val status = getLeaveforapprovalDetails.statusDesc.toString()
                        println(" Status:- $status")
                        if (status == "Pending") {
                            result.getleaveForApproval_arraylist.add(getLeaveforapprovalDetails)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return result
        }

    //	getLeaveforapprovalDetails.emp_id = jsonObjectInside.getString("empId").toString();
    val rejectedLeaveStatus: Any
        get() {
            val result = AddToFav()
            if (json == null) {
                result.Error = "No Responce"
            } else {
                try {
                    val jsonArray = JSONArray(json)
                    val jsonObject = jsonArray.getJSONObject(0)
                    result.StatusForLeaveType = jsonObject.getString("Status")
                    result.Message = jsonObject.getString("Message")
                    val jsonArrayInside = jsonObject.getJSONArray("lstLeaveRequests")
                    for (i in 0 until jsonArrayInside.length()) {
                        val jsonObjectInside = jsonArrayInside.getJSONObject(i)
                        val getLeaveforapprovalDetails = GetLeavesForApprovalDetails()
                        getLeaveforapprovalDetails.adminComment = jsonObjectInside.getString("adminComment").toString()
                        getLeaveforapprovalDetails.emp_id = jsonObjectInside.getInt("empId")
                        //	getLeaveforapprovalDetails.emp_id = jsonObjectInside.getString("empId").toString();
                        getLeaveforapprovalDetails.empName = jsonObjectInside.getString("empName").toString()
                        getLeaveforapprovalDetails.leaveDesc = jsonObjectInside.getString("leaveDesc").toString()
                        getLeaveforapprovalDetails.leaveFromDate = jsonObjectInside.getString("leaveFromDate").toString()
                        getLeaveforapprovalDetails.leaveToDate = jsonObjectInside.getString("leaveToDate").toString()
                        getLeaveforapprovalDetails.leaveType = jsonObjectInside.getString("leaveType").toString()
                        getLeaveforapprovalDetails.statusDesc = jsonObjectInside.getString("statusDesc").toString()
                        val status = getLeaveforapprovalDetails.statusDesc.toString()
                        println(" Status:- $status")
                        val rejectStatus = getLeaveforapprovalDetails.statusDesc.toString()
                        if (rejectStatus == "Rejected") {
                            result.getleaveForApproval_arraylist.add(getLeaveforapprovalDetails)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return result
        }

    val approvedLeaveStatus: Any
        get() {
            val result = AddToFav()
            if (json == null) {
                result.Error = "No Responce"
            } else {
                try {
                    val jsonArray = JSONArray(json)
                    val jsonObject = jsonArray.getJSONObject(0)
                    result.StatusForLeaveType = jsonObject.getString("Status")
                    result.Message = jsonObject.getString("Message")
                    val jsonArrayInside = jsonObject.getJSONArray("lstLeaveRequests")
                    for (i in 0 until jsonArrayInside.length()) {
                        val jsonObjectInside = jsonArrayInside.getJSONObject(i)
                        val getLeaveforapprovalDetails = GetLeavesForApprovalDetails()
                        getLeaveforapprovalDetails.adminComment = jsonObjectInside.getString("adminComment").toString()
                        getLeaveforapprovalDetails.emp_id = jsonObjectInside.getInt("empId")
                        getLeaveforapprovalDetails.empName = jsonObjectInside.getString("empName").toString()
                        getLeaveforapprovalDetails.leaveDesc = jsonObjectInside.getString("leaveDesc").toString()
                        getLeaveforapprovalDetails.leaveFromDate = jsonObjectInside.getString("leaveFromDate").toString()
                        getLeaveforapprovalDetails.leaveToDate = jsonObjectInside.getString("leaveToDate").toString()
                        getLeaveforapprovalDetails.leaveType = jsonObjectInside.getString("leaveType").toString()
                        getLeaveforapprovalDetails.statusDesc = jsonObjectInside.getString("statusDesc").toString()
                        val aprrovedStatus = getLeaveforapprovalDetails.statusDesc.toString()
                        if (aprrovedStatus == "Approved") {
                            result.getleaveForApproval_arraylist.add(getLeaveforapprovalDetails)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return result
        }

    fun removeFromFav(jsonArrayInside: JSONArray): Any {
        val result = GetUnfavEmpDetails()
        if (json == null) {
            result.Error = "No response"
            return result
        } else {
            try { // JSONArray jsonArrayInside = new JSONArray(json);
                val jsonobject = jsonArrayInside.getJSONObject(0)
                result.Status = jsonobject["Status"].toString().toInt()
                result.Message = jsonobject["Message"].toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun LeaveApproval(): Any {
        val result = GetApproveLeaveDetails()
        if (json == null) {
            result.Error = "No response"
            return result
        } else {
            try {
                val jsonArray = JSONArray(json)
                val jsonobject = jsonArray.getJSONObject(0)
                result.Status = jsonobject["Status"].toString().toInt()
                result.Message = jsonobject["Message"].toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return result
    }

    fun GetDeleteLeaveStatus(jsonArrayInside: JSONArray): Any {
        val result = GetDeleteAppliedLeaveDetails()
        if (json == null) {
            result.Error = "No response"
            return result
        } else {
            try { // JSONArray jsonArray = new JSONArray(json);
                val jsonobject = jsonArrayInside.getJSONObject(0)
                result.Status = jsonobject["Status"].toString().toInt()
                result.Message = jsonobject["Message"].toString()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return result
    }//	result.Status = jsonObject.getString("Status");

    /*public Object getModuleName()
    {
        // TODO Auto-generated method stub
        return null;
    } */
    val projectTitle: Any
        get() {
            val result = AddToFav()
            if (json == null) {
                result.Error = "No Responce"
            } else {
                try {
                    val jsonArray = JSONArray(json)
                    val jsonObject = jsonArray.getJSONObject(0)
                    //	result.Status = jsonObject.getString("Status");
                    result.Status = jsonObject.getInt("Status")
                    result.Message = jsonObject.getString("Message")
                    val jsonArrayInside = jsonObject.getJSONArray("lstDropDownData")
                    for (i in 0 until jsonArrayInside.length()) {
                        val jsonObjectInside = jsonArrayInside.getJSONObject(i)
                        val getProjectTitleDetails = GetProjectTitleDetails()
                        getProjectTitleDetails.projectID = jsonObjectInside.getString("projectID").toString()
                        getProjectTitleDetails.projectName = jsonObjectInside.getString("projectName")
                        result.getProjectTitle_arraylist.add(getProjectTitleDetails)
                        println("RESULT:- " + result.getProjectTitle_arraylist)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return result
        }

    /**
     * For getting contacts
     */
    fun get_holiday_list(jsonArrayInside: JSONArray): GetHolidaysList {
        Log.d("json11size", "json11size" + jsonArrayInside.length())
        val result = GetHolidaysList()
        if (json == null) {
            result.Error = "No response"
            return result
        } else {
            try { /* JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);*/
/* result.Status = Integer.parseInt(jsonObject.get("Status").toString());
                result.Message = jsonObject.get("Message").toString();*/
//JSONArray jsonArrayInside = jsonObject.getJSONArray("");
// if (Integer.parseInt(jsonObject.getString("Status")) == 0) {
                if (jsonArrayInside.length() != 0) {
                    for (i in 0 until jsonArrayInside.length()) {
                        val jsonObjectinside = jsonArrayInside.getJSONObject(i)
                        val holiday_data = HolidayListData()
                        holiday_data.HolidayDate = jsonObjectinside.getString("HolidayDate")
                        holiday_data.HolidayId = jsonObjectinside.getString("HolidayId")
                        holiday_data.Location = jsonObjectinside.getString("Location")
                        holiday_data.LocationId = jsonObjectinside.getString("LocationId")
                        holiday_data.Narration = jsonObjectinside.getString("Narration")
                        result.holiday_arraylist.add(holiday_data)
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return result
    }

    override fun holydaylistJsonList(filterArrayList: JSONArray?) {
        var jsonArray: JSONArray? = JSONArray()
        jsonArray = filterArrayList
        Log.d("filterArrayListqqqqqqq", "" + jsonArray)
    }

    init {
        this.json = json
    }
}