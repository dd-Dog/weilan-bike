package com.wlcxbj.bike.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.wlcxbj.bike.global.Constants;


/**
 * Created by Administrator on 2016/11/10.
 */
public class PreferenceUtil {

    public static SharedPreferences getSharedPreferences(Context contxt) {
        SharedPreferences sp = contxt.getSharedPreferences(Constants.PREFERENCES_NAME, Context
                .MODE_PRIVATE);
        return sp;
    }

    public static String getString(Context context, String key, String value) {
        return getSharedPreferences(context).getString(key, value);
    }

    public static Boolean getBoolean(Context context, String key, Boolean defValue) {
        return getSharedPreferences(context).getBoolean(key, defValue);
    }

    public static int getInt(Context context, String key, int defValue) {
        return getSharedPreferences(context).getInt(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static void putInt(Context context, String key, int value) {
        getSharedPreferences(context).edit().putInt(key, value).commit();
    }

    public static void putBoolean(Context context, String key, Boolean value) {
        getSharedPreferences(context).edit().putBoolean(key, value).commit();
    }

    public static void remove(Context context, String key) {
        getSharedPreferences(context).edit().remove(key).commit();
    }
}
