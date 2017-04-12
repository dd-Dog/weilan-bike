package com.wlcxbj.bike.util.properties;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.util.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2017/3/2.
 */
public class PropertiesUtil {
    private static final String TAG  = "PropertiesUtil";
    public static final String TABLEID = "_TABLEID";
    public static final String OSS_BUCKET = "_BUCKET";

    public static String getProperties(Context context, String key) {
        InputStream in = null;
        String value;
        try {
            in = context.getResources().openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(in);
            value = properties.getProperty(getDevModelValue(context).toUpperCase() + key);
            return value;
        } catch (IOException e) {
            Log.e(TAG, "load properties error", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    /**
     * 获取开发模式
     *
     * @return 配置文件对应 的值。
     **/
    public static String getDevModelValue(Context context) {
        ApplicationInfo appInfo = null;
        String msg = "DEBUG";//默认是开发模式
        try {
            //获取meta-data 属性
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            //获取meta-data 下面DEV_MODEL 的值
            msg = appInfo.metaData.getString("DEV_MODEL");
            msg = "release";
            LogUtil.e(TAG, "当前开发模式：" + msg);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "获取开发模式失败", e);
        }
        return msg.toUpperCase();

    }
}
