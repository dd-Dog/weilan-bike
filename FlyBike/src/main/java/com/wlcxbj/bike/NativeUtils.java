package com.wlcxbj.bike;

/**
 * Created by Administrator on 2017/3/21.
 */

/**
 * Created by krubo on 2016/1/20.
 */
public class NativeUtils {
    static {
        System.loadLibrary("EasyBike");
    }

    public static native String getAES_key();
}
