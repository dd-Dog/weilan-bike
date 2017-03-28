package com.wlcxbj.bike.util.account;

import android.content.Context;

import com.wlcxbj.bike.bean.account.AuthNativeToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.util.cache.CacheUtil;

/**
 * Created by Administrator on 2017/2/24.
 */
public class AccountUtil {

    public static boolean isLogin(Context context) {
        AuthNativeToken authNativeToken = CacheUtil.getSerialToken(context, Constants.AUTH_CACHE_FILE_NAME);
        if (authNativeToken != null && !authNativeToken.isExpire()) {
            return true;
        }
        return false;
    }
}
