package com.wlcxbj.bike.net;

import android.content.Context;
import android.telephony.TelephonyManager;

import org.json.JSONObject;

import com.wlcxbj.bike.global.Constants;

/**
 * Created by Administrator on 2017/2/7.
 */
public class OkhttpEngine {

    /**
     * 输出型函数
     * @param jsonObject
     * @param context
     */
    public static void addPublicParamsToJsonObject(JSONObject jsonObject, Context context) {
//        version	string	APP版本号
//        deviceId	string	设备唯一编号
//        channel	string	客户端类型通道，取值范围： ANDROID, IOS, WEB
        try {
            int version = context.getPackageManager().getPackageInfo(context.getPackageName(),
                        0).versionCode;
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId = telephonyManager.getDeviceId();
            String channel = Constants.CHANNEL;
            jsonObject.put("version", version);
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("channel", channel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
