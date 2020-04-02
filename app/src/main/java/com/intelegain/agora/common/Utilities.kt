package com.intelegain.agora.common

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * This class is Utility class which is used due to same code is used again and
 * again in many of the class so make them in util class so can be used by any
 * class.
 *
 */
class Utilities private constructor() {
    /*
	 * This is interface for NeutralButton Alert
	 */
    interface OnSingleAlert {
        fun onClickOk(id: Int)
    }

    fun getDateandTime(milliSeconds: Long, dateFormat: String?): String { // Create a DateFormatter object for displaying date in specified
// format.
        val formatter: DateFormat = SimpleDateFormat(dateFormat)
        // Create a calendar object that will convert the date and time value in
// milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    /**
     * TAG
     */
    private val TAG = "Utility"

    /**
     * This method is used to show toast
     *
     * @param context
     * context
     * @param message
     * message to show in toast
     */
    fun toast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * This method is used to show toast
     *
     * @param context
     * context
     * @param message
     * message to show in toast
     */
    fun toast(context: Context?, message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * This method is used to show log
     *
     * @param TAG
     * Your class name
     * @param msg
     * your message to show in log
     */
    fun log(TAG: String?, msg: String?) {
        Log.i(TAG, msg)
    }

    /**
     * This method is used to check if the entered string is null, blank, or
     * "null"
     *
     * @param str
     * set String to check
     * @return true if null else false
     */
    fun isEmptyOrNull(str: String): Boolean {
        return !(!TextUtils.isEmpty(str) && str != "null")
    }

    /**
     * Checks whether the given email address is valid.
     *
     * @param email
     * represents the email address.
     * @return true if the email is valid, false otherwise.
     * @since 09-Feb-2009
     */
    fun isEmail(email: String?): Boolean {
        if (email == null) {
            return false
        }
        // Assigning the email format regular expression
        val emailPattern = "^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})"
        return email.matches(Regex(emailPattern))
    }

    companion object {

        @JvmStatic
		@get:Synchronized
        val instance = Utilities()
    }
}