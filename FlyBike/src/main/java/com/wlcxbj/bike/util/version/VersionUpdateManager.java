package com.wlcxbj.bike.util.version;

import android.app.Activity;
import android.content.Context;

import com.wlcxbj.bike.bean.VersionBean;
import com.wlcxbj.bike.bean.account.AuthNativeToken;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.net.beanutil.HttpVersonUpdateUtil;
import com.wlcxbj.bike.util.LogUtil;

/**
 * Created by itsdon on 17/4/17.
 */

public class VersionUpdateManager {
    private static final String TAG = "VersionUpdateManager";
    private Context context;
    private HttpVersonUpdateUtil httpVersonUpdateUtil;

    public VersionUpdateManager(Context context){
        this.context = context;
        httpVersonUpdateUtil = new HttpVersonUpdateUtil((Activity) context);
    }

    public void checkVersion(String accessToken,HttpCallbackHandler<VersionBean> callbackHandler){
        httpVersonUpdateUtil.checkVersion(accessToken,callbackHandler);
    }
}
