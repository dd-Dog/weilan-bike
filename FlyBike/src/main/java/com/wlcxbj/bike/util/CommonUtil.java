package com.wlcxbj.bike.util;

import android.content.Context;

/**
 * Created by itsdon on 17/4/13.
 */

public class CommonUtil {

    /**
     *  获取手机屏幕宽度
     * @param context
     * @return
     */
    public static int  getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     *  获取手机屏幕高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
