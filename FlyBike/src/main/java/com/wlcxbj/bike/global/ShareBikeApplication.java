package com.wlcxbj.bike.global;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.squareup.leakcanary.LeakCanary;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import cn.sharesdk.framework.ShareSDK;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.account.AuthNativeToken;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.cache.CacheUtil;

/**
 * Created by bain on 16-11-29.
 */
public class ShareBikeApplication extends TinkerApplication {
    private static final String TAG = "ShareBikeApplication";
    private static ShareBikeApplication instance;
    private CloudPushService pushService;
    public static String ip;
    public static String port;
    public static String pushDevicedId;

    public ShareBikeApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.wlcxbj.bike.global.BuglyApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }

    public synchronized static ShareBikeApplication getInstance() {
        if (null == instance) {
            instance = new ShareBikeApplication();
        }
        return instance;
    }

    private ArrayList<Activity> mLilst = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        loadConfig();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        if (TextUtils.equals(getDevModelValue().toUpperCase(), "DEBUG")) {
            LeakCanary.install(this);
            LogUtil.e(TAG,"初始化leakcanary");
        }

        ShareSDK.initSDK(this);

        initCloudChannel(this);

    }

    /**
     * 加载开发环境的配置文件
     *
     * @author wgy
     **/
    private void loadConfig() {
        InputStream in = null;
        try {
            in = getResources().openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(in);
            LogUtil.e(TAG, "KEY IP=" + getDevModelValue().toUpperCase() + "_IP");
            ip = properties.getProperty(getDevModelValue().toUpperCase() + "_IP");
            port = properties.getProperty(getDevModelValue().toUpperCase() + "_PORT");
            if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(port)) {
                ip = properties.getProperty("DEBUG_IP");
                port = properties.getProperty("DEBUG_PORT");
            }
            LogUtil.e(TAG, "IP=" + ip + ", PORT=" + port);
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
    }

    /**
     * 获取开发模式
     *
     * @return 配置文件对应 的值。
     **/
    public String getDevModelValue() {
        ApplicationInfo appInfo = null;
        String msg = "DEBUG";//默认是开发模式
        try {
            //获取meta-data 属性
            appInfo = this.getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            //获取meta-data 下面DEV_MODEL 的值
            msg = appInfo.metaData.getString("DEV_MODEL");
            LogUtil.e(TAG, "当前开发模式：" + msg);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "获取开发模式失败", e);
        }
        return msg;

    }

    public void push(Activity activity) {
        mLilst.add(activity);
    }

    public void pop(Activity activity) {
        mLilst.remove(activity);
    }

    public void exit() {
        for (int i = 0; i < mLilst.size(); i++) {
            Activity activity = mLilst.get(i);
            activity.finish();
        }
    }

    /**
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtil.e(TAG, "init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                LogUtil.e(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- " +
                        "errorMessage:" + errorMessage);
            }
        });
        AuthNativeToken authNativeToken = CacheUtil.getSerialToken(this, Constants
                .AUTH_CACHE_FILE_NAME);
        if (authNativeToken == null) return;
        LogUtil.e(TAG, "初始化云推送");
        pushService.bindAccount(authNativeToken.getAuthToken().getUserId(), new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                LogUtil.e(TAG, "绑定帐户成功：" + s);
            }

            @Override
            public void onFailed(String s, String s1) {
                LogUtil.e(TAG, "绑定帐户失败：" + s + ", " + s1);
            }
        });

        String deviceId = pushService.getDeviceId();
        pushDevicedId = pushService.getDeviceId();
        LogUtil.e(TAG, "deviceId=" + deviceId);
        LogUtil.e(TAG, "deviceId  2 =" + Constants.getDeviceId(this));
    }

    public CloudPushService getPushService() {
        return pushService;
    }
}
