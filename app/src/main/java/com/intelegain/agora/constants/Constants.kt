package com.intelegain.agora.constants

import android.content.Context
import android.net.ConnectivityManager

object Constants {


    const val CUSTOMERBASE_URL = "http://www.agoracust.sirus/AgoraMobile.api/"


    const val OsTypeVal = "Android"
    const val DeviceIdVal = "f07a13984f6d116a"
    const val DeviceIdText = "DeviceId"
    const val OsTypeText = "OsType"
    const val BASE_URL = "http://emp.intelgain.com/AgoraMobile.api/";
   //const val BASE_URL = "http://52.172.192.203/AgoraMobile.api/";


    const val EmployeeID = "EmployeeID";
    const val KEY_NAME = "Name";
    const val EMAIL_ID = "EmailID";
    const val ADDRESS = "Address";
    const val CONTACT = "Contact";
    const val JOIN_DATE = "JoiningDate";
    const val PROBATION_PERIOD = "ProbationPeriod";
    const val DOB = "BDate";
    const val PREVIOUS_EMPLOYER = "PreviousEmployer";
    const val CURRENT_ADDRESS = "CurrentAddress";
    const val CONFIRM_DATE = "empConfDate";
    const val DESIGNATION = "Designation";
    const val TOKEN = "Token";
    const val UP_LOADED_IMAGE = "UploadedImage";

    @JvmStatic
    fun checkInternetConnection(_activity: Context): Boolean {
        val conMgr = _activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (conMgr.activeNetworkInfo != null && conMgr.activeNetworkInfo.isAvailable)
            true
        else
            false
    }


    /* public static boolean checkInternetConnection(Context _activity) {
        ConnectivityManager conMgr = (ConnectivityManager) _activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable())
            return true;
        else
            return false;
    }*/
}

//}