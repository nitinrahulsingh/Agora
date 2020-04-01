package com.intelegain.agora.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * This is SharedPreferences SingleTon Class used to store small amount of data
 * in private mode
 */
class Sharedprefrences {
    private lateinit var pref: SharedPreferences
    private var editor: SharedPreferences.Editor? = null
    private val name = "AgoraIntelgain"

    /**
     * Default Constructor To make this class work as SingleTon
     */
    private constructor() {}

    /**
     * Constructor is called
     */
    private constructor(context: Context?) {
        if (context != null) {
            pref = context.getSharedPreferences(name, 0)
        }
        editor = pref.edit()
    }

    /**
     * Here all values is to put into shared prefrences
     *
     * @param key   Unique Key for a value
     * @param value Value to be stored
     */
    fun putboolean(key: String?, value: Boolean) {
        editor!!.putBoolean(key, value)
        editor!!.commit()
    }

    fun putString(key: String?, value: String?) {
        editor!!.putString(key, value)
        editor!!.commit()
    }

    fun putInt(key: String?, value: Int) {
        editor!!.putInt(key, value)
        editor!!.commit()
    }

    fun putLong(key: String?, value: Long) {
        editor!!.putLong(key, value)
        editor!!.commit()
    }

    fun putFloat(key: String?, value: Float) {
        editor!!.putFloat(key, value)
        editor!!.commit()
    }

    fun remove(key: String?) {
        editor!!.remove(key)
    }

    fun clear() {
        editor!!.clear()
        //editor.commit();
    }

    fun commit() {
        editor!!.commit()
    }

    /**
     * Here all values is to get String value from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defaultvalue give here defaultValue if not found defalutValue is assigned
     * @return string
     */
    fun getString(key: String?, defaultvalue: String?): String? {
        return pref!!.getString(key, defaultvalue)
    }

    /**
     * Here all values is to get int value from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defaultvalue give here defaultValue if not found defalutValue is assigned
     * @return int
     */
    fun getInt(key: String?, defValue: Int): Int {
        return pref!!.getInt(key, defValue)
    }

    /**
     * Here all values is to get boolean value from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defaultvalue give here defaultValue if not found defalutValue is assigned
     * @return boolean
     */
    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return pref!!.getBoolean(key, defValue)
    }

    /**
     * Here all values is to get long valued from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defaultvalue give here defaultValue if not found defalutValue is assigned
     * @return long
     */
    fun getLong(key: String?, defValue: Long): Long {
        return pref!!.getLong(key, defValue)
    }

    /**
     * Here all values is to get float value from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defaultvalue give here defaultValue if not found
     * defalutValue is assigned
     * @return float
     */
    fun getFloat(key: String?, defValue: Float): Float {
        return pref!!.getFloat(key, defValue)
    }

    companion object {
        private var instance: Sharedprefrences? = null
        /**
         * @param context Parameterized Constructor is called
         */
        @JvmStatic
        fun getInstance(context: Context?): Sharedprefrences {
            return if (instance == null) Sharedprefrences(context).also { instance = it } else instance!!
        }
    }
}