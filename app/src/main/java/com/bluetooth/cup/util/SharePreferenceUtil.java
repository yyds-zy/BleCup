package com.bluetooth.cup.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {

    private static final String TAG = SharePreferenceUtil.class.getSimpleName();

    private static final String DEFAULT_SHARE_NAME = "bluetooth_cup";
    public static final String SERVICE_UUID = "service_uuid";
    public static final String SERVICE_UUID_NOTIFY = "service_uuid_notify";
    public static final String SERVICE_UUID_WRITE = "service_uuid_write";
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_MAC_ADDRESS = "mac_address";
    public static final String DEVICE_CONNECT_STATE = "device_connect_state";
    public static final String CUP_RECORD_INFO = "record_info";


    private static SharedPreferences sharedPreferences;

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(DEFAULT_SHARE_NAME, Context.MODE_PRIVATE);
    }

    public static void put(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static String get(String key) {
        return sharedPreferences.getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void put(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void put(String key, Long value) {
        sharedPreferences.edit().putLong(key, value).apply();
    }

    public static Long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public static Long getLong(String key, Long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static void put(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void put(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    public static float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public static void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public static void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
