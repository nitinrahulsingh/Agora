package com.intelegain.agora.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This is SharedPreferences SingleTon Class used to store small amount of data
 * in private mode
 */
public class Sharedprefrences {
    private static Sharedprefrences instance;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private final String name = "AgoraIntelgain";

    /**
     * @param context Parameterized Constructor is called
     */
    public static Sharedprefrences getInstance(Context context) {
        return instance == null ? instance = new Sharedprefrences(context) : instance;
    }

    /**
     * Default Constructor To make this class work as SingleTon
     */
    private Sharedprefrences() {
    }

    /**
     * Constructor is called
     */
    private Sharedprefrences(Context context) {
        pref = context.getSharedPreferences(name, 0);
        editor = pref.edit();
    }

    /**
     * Here all values is to put into shared prefrences
     *
     * @param key   Unique Key for a value
     * @param value Value to be stored
     */
    public void putboolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public void remove(String key) {
        editor.remove(key);


    }

    public void clear() {
        editor.clear();
        //editor.commit();


    }

    public void commit() {
        editor.commit();
    }

    /**
     * Here all values is to get String value from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defaultvalue give here defaultValue if not found defalutValue is assigned
     * @return string
     */
    public String getString(String key, String defaultvalue) {
        return pref.getString(key, defaultvalue);
    }

    /**
     * Here all values is to get int value from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defaultvalue give here defaultValue if not found defalutValue is assigned
     * @return int
     */
    public int getInt(String key, int defValue) {
        return pref.getInt(key, defValue);
    }

    /**
     * Here all values is to get boolean value from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defaultvalue give here defaultValue if not found defalutValue is assigned
     * @return boolean
     */
    public boolean getBoolean(String key, boolean defValue) {
        return pref.getBoolean(key, defValue);
    }

    /**
     * Here all values is to get long valued from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defaultvalue give here defaultValue if not found defalutValue is assigned
     * @return long
     */
    public long getLong(String key, long defValue) {
        return pref.getLong(key, defValue);
    }

    /**
     * Here all values is to get float value from shared preferences
     *
     * @param key          retrieve by unique key
     * @param defaultvalue give here defaultValue if not found
     *                     defalutValue is assigned
     * @return float
     */
    public float getFloat(String key, float defValue) {
        return pref.getFloat(key, defValue);
    }
}