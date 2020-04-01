package com.intelegain.agora.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.interfeces.Holidaylist;
import com.intelegain.agora.parser.ParserClass;
import com.intelegain.agora.utils.Sharedprefrences;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ConnectException;

public class DataFetchServices {
    public static String respJsonStr = null;
    public static String holydaylist = null;
    public static String contactlist = null;
    public static String favcontactlist = null;
    public static String availableLeaves = null;
    public static String leavedetails = null;
    public static String removeFromfav = null;
    public static String AddFromfav = null;
    public static String deleteleave = null;
    public static String applyleave = null;
    public static String changepassword = null;
    public static String leavetype = null;
    public static String forgotpassword = null;

    public static String attendance = null;
    public Sharedprefrences mSharedPref;
    String url = "";
    String EmployeeID, Name, EmailID, Address, Contact, JoiningDate, BDate, ProbationPeriod, Token, Designation, ConfirmationDate, IsActive;

    String Cust_UserMasterID, Cust_FName, Cust_LName, Cust_Email, CustomerToken;

    JSONObject jsonObject;
    JSONArray jsonArray;

    Context context;
    Holidaylist holidaylist;
    // ArrayList<String> holidayArrayList = new ArrayList<>();

    public DataFetchServices(Context context) {
        this.context = context;
        holidaylist = (Holidaylist) context;

    }

    public DataFetchServices() {
    }


    public String getDataFromWebCall1(JSONObject parameters, String url, Context context) throws Exception {
        HttpParams httpParameters = new BasicHttpParams();
        int timeout1 = 1000 * 5;
        int timeout2 = 1000 * 5;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeout1);
        HttpConnectionParams.setSoTimeout(httpParameters, timeout2);

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        get.setHeader("content-type", "application/json");


        String respJsonStr = null;
        try {
            //StringEntity entity1 = new StringEntity(parameters.toString());


            HttpResponse resp = httpClient.execute(get);
            StatusLine statusline = resp.getStatusLine();

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {
                respJsonStr = EntityUtils.toString(resp.getEntity());
            }

           /* System.out.println("OKAY!");
            System.out.println("RESp:- " + respJsonStr);*/
        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respJsonStr;
    }


    public Object LoginAuth(String method_name, String EmpId, String Password, String DeviceId, String OsType, Context context, int flag) throws Exception {

        mSharedPref = Sharedprefrences.getInstance(context);
        HttpParams httpParameters = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
        HttpConnectionParams.setSoTimeout(httpParameters, 5000);

        Object webserviceResult = null;
        url = Constants.BASE_URL + method_name;
        JSONObject parameters = new JSONObject();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");

            parameters.put("EmpId", EmpId);
            parameters.put("Password", Password);
            parameters.put("DeviceId", DeviceId);
            parameters.put("OsType", OsType);
        }

        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                respJsonStr = EntityUtils.toString(resp.getEntity());

                jsonObject = new JSONObject(respJsonStr);

                EmployeeID = jsonObject.getString("EmployeeID");
                Name = jsonObject.getString("Name");
                EmailID = jsonObject.getString("EmailID");
                Address = jsonObject.getString("Address");
                Contact = jsonObject.getString("Contact");
                JoiningDate = jsonObject.getString("JoiningDate");
                BDate = jsonObject.getString("BDate");
                ProbationPeriod = jsonObject.getString("ProbationPeriod");
                Token = jsonObject.getString("Token");
                Designation = jsonObject.getString("Designation");
                ConfirmationDate = jsonObject.getString("empConfDate");
                IsActive = jsonObject.getString("IsActive");

                //Log.v("asdf", "asdf " + EmployeeID);

                mSharedPref.putString("emp_Id", EmployeeID);
                mSharedPref.putString("emp_Name", Name);
                mSharedPref.putString("emp_emailid", EmailID);
                mSharedPref.putString("emp_address", Address);
                mSharedPref.putString("emp_contactno", Contact);
                mSharedPref.putString("emp_joining_date", JoiningDate);
                mSharedPref.putString("emp_birthDate", BDate);
                mSharedPref.putString("emp_probationperiod", ProbationPeriod);
                mSharedPref.putString("emp_designation", Designation);
                mSharedPref.putString("emp_confirmation_date", ConfirmationDate);
                mSharedPref.putString("Token", Token);

                // Log.d("tokemmmmmmmm", mSharedPref.getString("Token", ""));


            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }


        webserviceResult = new ParserClass(respJsonStr).Signup_Parser(jsonObject);
        return webserviceResult;
    }


    public Object CustomerLoginAuth(String method_name, String EmpId, String Password, String DeviceId, String OsType, Context context, int flag) throws Exception {


        mSharedPref = Sharedprefrences.getInstance(context);
        HttpParams httpParameters = new BasicHttpParams();
        /*int timeout1 = 1000 * 5;
        int timeout2 = 1000 * 5;*/
        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
        HttpConnectionParams.setSoTimeout(httpParameters, 5000);
        Object webserviceResult = null;
        url = Constants.CUSTOMERBASE_URL + method_name;
        JSONObject parameters = new JSONObject();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");

            parameters.put("UserMasterID", EmpId);
            parameters.put("Password", Password);
            parameters.put("DeviceId", DeviceId);
            parameters.put("OsType", OsType);
        }


        try {


            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                JSONObject myObject = new JSONObject(EntityUtils.toString(resp.getEntity()));
                jsonObject = myObject.getJSONObject("ObjUsers");


                CustomerToken = myObject.getString("Token");
                Cust_UserMasterID = jsonObject.getString("UserMasterID");
                Cust_FName = jsonObject.getString("FName");
                Cust_LName = jsonObject.getString("LName");
                Cust_Email = jsonObject.getString("Email");

                mSharedPref.putString("CustomerToken", CustomerToken);
                mSharedPref.putString("UserMasterID", Cust_UserMasterID);
                mSharedPref.putString("Cust_FName", Cust_FName);
                mSharedPref.putString("Cust_LName", Cust_LName);
                mSharedPref.putString("Cust_Email", Cust_Email);

            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }


        webserviceResult = new ParserClass(jsonObject.toString()).customer_signup(jsonObject);
        return webserviceResult;
    }


    public Object SearchContacts(String method_name, String search_name, Context context, int flag) throws Exception {

        mSharedPref = Sharedprefrences.getInstance(context);
        Object webserviceResult = null;
        String url1 = Constants.BASE_URL + method_name;
        JSONArray jsoncontact = new JSONArray();

        JSONObject parameters = new JSONObject();


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url1);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");
            post.setHeader("token", mSharedPref.getString("Token", ""));
            post.setHeader("empid", mSharedPref.getString("emp_Id", ""));

            parameters.put("EmpId", mSharedPref.getString("emp_Id", ""));
            Log.d("Header", mSharedPref.getString("emp_Id", ""));
            Log.d("Header11", mSharedPref.getString("Token", ""));
        }


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                contactlist = EntityUtils.toString(resp.getEntity());


                jsoncontact = new JSONArray(contactlist);

            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }

        webserviceResult = new ParserClass(contactlist).get_search_contacts(jsoncontact);
        return webserviceResult;
    }


    public Object AddToFav(String method_name, int fav_emp_id, Context context, int flag) throws Exception {
        mSharedPref = Sharedprefrences.getInstance(context);
        Object webserviceResult = null;
        url = Constants.BASE_URL + method_name;
        JSONObject parameters = new JSONObject();
        JSONArray jsonaddcontact = new JSONArray();


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");
            post.setHeader("token", mSharedPref.getString("Token", ""));
            post.setHeader("empid", mSharedPref.getString("emp_Id", ""));

            parameters.put("EmpId", mSharedPref.getString("emp_Id", ""));
            parameters.put("FavouriteEmpId", fav_emp_id);

           /* Log.d("Header", mSharedPref.getString("emp_Id", ""));
            Log.d("Header11", mSharedPref.getString("Token", ""));*/
        }


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {


                AddFromfav = EntityUtils.toString(resp.getEntity());


                jsonaddcontact = new JSONArray(AddFromfav);

            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }


        webserviceResult = new ParserClass(AddFromfav).addToFav(jsonaddcontact);
        return webserviceResult;
    }


    public Object GetFavEmp(String method_name, Context context, int flag) throws Exception {
        mSharedPref = Sharedprefrences.getInstance(context);
        Object webserviceResult = null;
        url = Constants.BASE_URL + method_name;
        JSONArray jsonfavcontact = new JSONArray();

        JSONObject parameters = new JSONObject();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");
            post.setHeader("token", mSharedPref.getString("Token", ""));
            post.setHeader("empid", mSharedPref.getString("emp_Id", ""));

            parameters.put("EmpId", mSharedPref.getString("emp_Id", ""));

           /* Log.d("Header", mSharedPref.getString("emp_Id", ""));
            Log.d("Header11", mSharedPref.getString("Token", ""));*/
        }


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                favcontactlist = EntityUtils.toString(resp.getEntity());


                jsonfavcontact = new JSONArray(favcontactlist);

            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }



/*
        try {

            parameters.put("EmpId", EmployeeID);

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //System.out.println("PARAMETER:- " + parameters);
        // String jsonResponceString = getDataFromWebCall(parameters, url, context, 1);
        webserviceResult = new ParserClass(favcontactlist).getFavEmp(jsonfavcontact);
        return webserviceResult;
    }


    public Object getTimeSheetDates(String method_name, String strfirstDate, String strlastDate, Context context, int flag) throws Exception {
        mSharedPref = Sharedprefrences.getInstance(context);
        Object webServiceResult = null;
        JSONObject parameters = new JSONObject();
        url = Constants.BASE_URL + method_name;
        JSONArray jsonattendance = new JSONArray();


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");
            post.setHeader("token", mSharedPref.getString("Token", ""));
            post.setHeader("empid", mSharedPref.getString("emp_Id", ""));

            parameters.put("EmpId", mSharedPref.getString("emp_Id", ""));
            parameters.put("Startdate", strfirstDate);
            parameters.put("EndDate", strlastDate);

            /*Log.d("Header", mSharedPref.getString("emp_Id", ""));
            Log.d("Header11", mSharedPref.getString("Token", ""));*/
        }


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCodeattendance ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                attendance = EntityUtils.toString(resp.getEntity());


                jsonattendance = new JSONArray(attendance);

            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }


        webServiceResult = new ParserClass(attendance).calenderDate(jsonattendance);
        return webServiceResult;
    }

    public Object getAvailableLeave(String method_name, String empId, Context context, int flag) throws Exception {
        mSharedPref = Sharedprefrences.getInstance(context);
        Object webServiceResult = null;
        JSONObject parameters = new JSONObject();
        url = Constants.BASE_URL + method_name;
        JSONArray jsonavailableleaves = new JSONArray();


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");
            post.setHeader("token", mSharedPref.getString("Token", ""));
            post.setHeader("empid", mSharedPref.getString("emp_Id", ""));

            parameters.put("EmpId", mSharedPref.getString("emp_Id", ""));

          /*  Log.d("Header", mSharedPref.getString("emp_Id", ""));
            Log.d("Header11", mSharedPref.getString("Token", ""));*/
        }


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                availableLeaves = EntityUtils.toString(resp.getEntity());


                jsonavailableleaves = new JSONArray(availableLeaves);


                JSONObject jsonObject = jsonavailableleaves.getJSONObject(0);


                String TotalCL = jsonObject.getString("BalanceCL");
                String TotalCO = jsonObject.getString("BalanceCO");
                String TotalPL = jsonObject.getString("BalancePL");
                String TotalSL = jsonObject.getString("BalanceSL");

                mSharedPref.putString("TotalCL", TotalCL);
                mSharedPref.putString("TotalCO", TotalCO);
                mSharedPref.putString("TotalPL", TotalPL);
                mSharedPref.putString("TotalSL", TotalSL);


            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }


        webServiceResult = new ParserClass(availableLeaves).getAvailableLeaves(jsonavailableleaves);
        return webServiceResult;
    }

    /*private String getString(int keyEmpId) {
        return null;
    }*/

    public Object getLStatus(String method_name, String emp_id, Context context, int flag) throws Exception {
        mSharedPref = Sharedprefrences.getInstance(context);
        Object webServiceRequest = null;
        JSONObject parameters = new JSONObject();
        JSONArray jsonleavestatus = new JSONArray();
        url = Constants.BASE_URL + method_name;

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        if (flag == 1) {

            post.setHeader("content-type", "application/json");
            post.setHeader("token", mSharedPref.getString("Token", ""));
            post.setHeader("empid", mSharedPref.getString("emp_Id", ""));

            parameters.put("EmpId", mSharedPref.getString("emp_Id", ""));
            parameters.put("Leave", 0);
            parameters.put("LeaveStatus", 0);
            parameters.put("Year", 2016);

            /*Log.d("Header", mSharedPref.getString("emp_Id", ""));
            Log.d("Header11", mSharedPref.getString("Token", ""));*/
        }


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                leavedetails = EntityUtils.toString(resp.getEntity());


                jsonleavestatus = new JSONArray(leavedetails);

            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }

        webServiceRequest = new ParserClass(leavedetails).getLeaveStatus(jsonleavestatus);
        return webServiceRequest;
    }


    public Object ForgotPass(String methodname, String EmpId,
                             String EmailId, Context context) throws Exception {
        Object webServiceRequest = null;
        url = Constants.BASE_URL + methodname;

        JSONObject responce = new JSONObject();
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("EmpId", EmpId);
            parameters.put("EmailId", EmailId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                forgotpassword = EntityUtils.toString(resp.getEntity());

                //System.out.println("forgotpassword" + forgotpassword);


                responce = new JSONObject(forgotpassword);


            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }

        webServiceRequest = new ParserClass(forgotpassword).forgotPassword(responce);


        return webServiceRequest;
    }

    public Object ChangePass(String methodname, String EmpId, String old_password, String new_password,
                             String confirm_password, Context context, int flag) throws Exception {

        mSharedPref = Sharedprefrences.getInstance(context);
        Object webserviceResult = null;
        url = Constants.BASE_URL + methodname;
        JSONObject parameters = new JSONObject();
        JSONObject jsonchangepassword = new JSONObject();


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");
            post.setHeader("token", mSharedPref.getString("Token", ""));
            post.setHeader("empid", mSharedPref.getString("emp_Id", ""));

            parameters.put("EmpId", EmpId);
            parameters.put("OldPassword", old_password);
            parameters.put("NewPassword", new_password);
            parameters.put("ConfirmPassword", confirm_password);

            /*Log.d("Header", mSharedPref.getString("emp_Id", ""));
            Log.d("Header11", mSharedPref.getString("Token", ""));*/
        }


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {
                changepassword = EntityUtils.toString(resp.getEntity());
                jsonchangepassword = new JSONObject(changepassword);

            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }


        webserviceResult = new ParserClass(changepassword).changePassword(jsonchangepassword);
        return webserviceResult;
    }

    public Object ApplyLeave(String methodname, String EmpId,
                             String leaveType, String strFromDate, String strToDate, String strReasonForLeave, Context context, int flag) throws Exception {
        Object webserviceResult = null;
        url = Constants.BASE_URL + methodname;

        JSONArray jsonapplyLeave = new JSONArray();

        JSONObject parameters = new JSONObject();


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");
            post.setHeader("token", mSharedPref.getString("Token", ""));
            post.setHeader("empid", mSharedPref.getString("emp_Id", ""));

            parameters.put("EmpId", EmpId);
            parameters.put("Leave", leaveType);
            parameters.put("LeaveFrom", strFromDate);
            parameters.put("LeaveTo", strToDate);
            parameters.put("Reason", strReasonForLeave);

            /*Log.d("Header", mSharedPref.getString("emp_Id", ""));
            Log.d("Header11", mSharedPref.getString("Token", ""));*/
        }


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                applyleave = EntityUtils.toString(resp.getEntity());


                jsonapplyLeave = new JSONArray(applyleave);

            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }


        webserviceResult = new ParserClass(applyleave).applyLeave(jsonapplyLeave);
        // System.out.println("PARAMETER:- " + parameters);
        return webserviceResult;
    }

    public Object GetLeaveType(String methodname, Context context, int flag) throws Exception {


        mSharedPref = Sharedprefrences.getInstance(context);
        Object webserviceResult = null;
        String url1 = Constants.BASE_URL + methodname;
        JSONArray jsonleavetype = new JSONArray();

        JSONObject parameters = new JSONObject();


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url1);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");
            post.setHeader("token", mSharedPref.getString("Token", ""));
            post.setHeader("empid", mSharedPref.getString("emp_Id", ""));

            parameters.put("EmpId", mSharedPref.getString("emp_Id", ""));

            /*Log.d("Header", mSharedPref.getString("emp_Id", ""));
            Log.d("Header11", mSharedPref.getString("Token", ""));*/
        }


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            // Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                leavetype = EntityUtils.toString(resp.getEntity());


                jsonleavetype = new JSONArray(leavetype);

            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }


        webserviceResult = new ParserClass(leavetype).getLeaveType(jsonleavetype);
        return webserviceResult;
    }

    public Object getLStatus(String methodname,
                             Context context) throws Exception {

        Object webserviceResult = null;
        url = Constants.BASE_URL + methodname;
        JSONObject parameters = new JSONObject();

        String jsonResponceString = getDataFromWebCall1(parameters, url, context);

        // System.out.println("Response String:- " + jsonResponceString);

        webserviceResult = new ParserClass(jsonResponceString).getApproveLeaveStatus();
        return webserviceResult;
    }

    public Object getLStatusForPendingLeaves(String methodname,
                                             Context context) throws Exception {
        Object webserviceResult = null;
        url = Constants.BASE_URL + methodname;
        JSONObject parameters = new JSONObject();

        String jsonResponceString = getDataFromWebCall1(parameters, url, context);
        // System.out.println("Response String:- " + jsonResponceString);
        webserviceResult = new ParserClass(jsonResponceString).getPendingLeaveStatus();
        return webserviceResult;
    }

    public Object getLStatusForRejectedLeaves(String methodname,
                                              Context context) throws Exception {
        Object webserviceResult = null;
        url = Constants.BASE_URL + methodname;
        JSONObject parameters = new JSONObject();

        String jsonResponceString = getDataFromWebCall1(parameters, url, context);
        // System.out.println("Response String:- " + jsonResponceString);
        webserviceResult = new ParserClass(jsonResponceString).getRejectedLeaveStatus();
        return webserviceResult;
    }

    public Object getLStatusForApprovedLeaves(String methodname,
                                              Context context) throws Exception {
        Object webserviceResult = null;
        url = Constants.BASE_URL + methodname;
        JSONObject parameters = new JSONObject();

        String jsonResponceString = getDataFromWebCall1(parameters, url, context);
        // System.out.println("Response String:- " + jsonResponceString);
        webserviceResult = new ParserClass(jsonResponceString).getApprovedLeaveStatus();
        return webserviceResult;
    }

    public Object RemoveEmpID(String methodname, int emp_id, String EmpId, Context context, int flag) throws Exception {

        mSharedPref = Sharedprefrences.getInstance(context);
        Object webserviceResult = null;
        url = Constants.BASE_URL + methodname;
        JSONObject parameters = new JSONObject();
        JSONArray jsonRemoveFav = new JSONArray();


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");
            post.setHeader("token", mSharedPref.getString("Token", ""));
            post.setHeader("empid", mSharedPref.getString("emp_Id", ""));

            parameters.put("EmpId", mSharedPref.getString("emp_Id", ""));
            parameters.put("FavouriteEmpId", emp_id);

            /*Log.d("Header", mSharedPref.getString("emp_Id", ""));
            Log.d("Header11", mSharedPref.getString("Token", ""));*/
        }


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                removeFromfav = EntityUtils.toString(resp.getEntity());


                jsonRemoveFav = new JSONArray(removeFromfav);

            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }

        webserviceResult = new ParserClass(removeFromfav).removeFromFav(jsonRemoveFav);
        // System.out.println("PARAMETER:- " + parameters);
        return webserviceResult;
    }

    public Object getApproveLeave(String methodname,
                                  String empLeaveId, String strFromDate, String strToDate, String SanctionById, String SanctionByName, String leaveStatus,
                                  String sanctionedDate, String adminComment, Context context) throws Exception {
        Object webserviceResult = null;
        url = Constants.BASE_URL + methodname;
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("empLeaveId", empLeaveId);
            parameters.put("strFromDate", strFromDate);
            parameters.put("strToDate", strToDate);
            parameters.put("SanctionById", SanctionById);
            parameters.put("SanctionByName", SanctionByName);
            parameters.put("leaveStatus", leaveStatus);
            parameters.put("sanctionedDate", sanctionedDate);
            parameters.put("adminComment", adminComment);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //System.out.println("PARAMETER:- " + parameters);
        return webserviceResult;
    }

    public Object getDeleteAppiledLeave(
            String methodname, String empLeaveId, Context context, int flag) throws Exception {
        Object webserviceResult = null;
        url = Constants.BASE_URL + methodname;
        JSONObject parameters = new JSONObject();
        JSONArray jsondeleteleave = new JSONArray();


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");
            post.setHeader("token", mSharedPref.getString("Token", ""));
            post.setHeader("empid", mSharedPref.getString("emp_Id", ""));

            parameters.put("EmpLeaveId", empLeaveId);

           /* Log.d("Header", mSharedPref.getString("emp_Id", ""));
            Log.d("Header11", mSharedPref.getString("Token", ""));*/
        }


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                deleteleave = EntityUtils.toString(resp.getEntity());


                jsondeleteleave = new JSONArray(deleteleave);

            }

        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }

        webserviceResult = new ParserClass(deleteleave).GetDeleteLeaveStatus(jsondeleteleave);
        // System.out.println("PARAMETER:- " + parameters);
        return webserviceResult;

    }

    public Object GetProjectTitle(String methodname,
                                  Context context) throws Exception {
        Object webserviceResult = null;
        url = Constants.BASE_URL + methodname;
        JSONObject parameters = new JSONObject();

        String jsonResponceString = getDataFromWebCall1(parameters, url, context);
        // System.out.println("Response String:- " + jsonResponceString);
        webserviceResult = new ParserClass(jsonResponceString).getProjectTitle();
        return webserviceResult;
    }


    public Object GetHolidays(String method_name, Context context, int flag) throws Exception {
        mSharedPref = Sharedprefrences.getInstance(context);
        Object webserviceResult = null;
        url = Constants.BASE_URL + method_name;
        JSONArray json11 = new JSONArray();

        JSONObject parameters = new JSONObject();


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        if (flag == 1) {
            post.setHeader("content-type", "application/json");
            post.setHeader("token", mSharedPref.getString("Token", ""));
            post.setHeader("empid", mSharedPref.getString("emp_Id", ""));

            //parameters.put("EmpId",  mSharedPref.getString("emp_Id", ""));
            /*Log.d("Header", mSharedPref.getString("emp_Id", ""));
            Log.d("Header11", mSharedPref.getString("Token", ""));*/
        }


        try {
            StringEntity entity = new StringEntity(parameters.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            StatusLine statusline = resp.getStatusLine();

            //Log.e("TAG", "getStatusCode ====" + statusline.getStatusCode());

            if (statusline.getStatusCode() == HttpStatus.SC_OK) {

                holydaylist = EntityUtils.toString(resp.getEntity());


                json11 = new JSONArray(holydaylist);

            }


        } catch (ConnectException e) {
            Toast.makeText(context, "ERROR.Time Out!!!", Toast.LENGTH_LONG).show();
        }


        webserviceResult = new ParserClass(holydaylist).get_holiday_list(json11);
        return webserviceResult;
    }
}