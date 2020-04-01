package com.intelegain.agora.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 
 * This class is Utility class which is used due to same code is used again and
 * again in many of the class so make them in util class so can be used by any
 * class.
 * 
 **/
public final class Utilities 
{
	private Utilities()
	{
	}
	public static synchronized Utilities getInstance() 
	{
		return instance;
	}
	/*
	 * This is interface for NeutralButton Alert
	 */
	public interface OnSingleAlert 
	{
		public void onClickOk(int id);
	}
	public String getDateandTime(long milliSeconds, String dateFormat)
	{
		// Create a DateFormatter object for displaying date in specified
		// format.
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		// Create a calendar object that will convert the date and time value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}
	/**
	 * TAG
	 */
	private String TAG = "Utility";
	private static final Utilities instance = new Utilities();

	/**
	 * This method is used to show toast
	 * 
	 * @param context
	 *            context
	 * @param message
	 *            message to show in toast
	 */
	public void toast(final Context context, final String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * This method is used to show toast
	 * 
	 * @param context
	 *            context
	 * @param message
	 *            message to show in toast
	 */
	public void toast(final Context context, final int message)
	{
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	/**
	 * This method is used to show log
	 * 
	 * @param TAG
	 *            Your class name
	 * @param msg
	 *            your message to show in log
	 */
	public void log(String TAG, String msg)
	{
		Log.i(TAG, msg);
	}

	/**
	 * This method is used to check if the entered string is null, blank, or
	 * "null"
	 * 
	 * @param str
	 *            set String to check
	 * @return true if null else false
	 */
	public boolean isEmptyOrNull(String str)
	{
		return !(!TextUtils.isEmpty(str) && !str.equals("null"));
	}

	/**
	 * Checks whether the given email address is valid.
	 * 
	 * @param email
	 *            represents the email address.
	 * @return true if the email is valid, false otherwise.
	 * @since 09-Feb-2009
	 */
	public boolean isEmail(String email)
	{
		if (email == null) 
		{
			return false;
		}
		// Assigning the email format regular expression
		String emailPattern = "^([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4})";
		return email.matches(emailPattern);
	}
}
