package com.wlcxbj.bike.ble.packet.util;

import android.util.Log;

/**
 * Created by Administrator on 2017/3/9.
 */
public class LogUtil {
    private static final boolean flag = true;
    public static void e(Class clazz, String msg) {
        if (flag) {
            Log.e(clazz.getSimpleName(), msg);

        }
    }
}
