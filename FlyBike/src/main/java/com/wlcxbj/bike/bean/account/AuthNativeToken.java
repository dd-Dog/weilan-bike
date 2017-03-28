package com.wlcxbj.bike.bean.account;

import java.io.Serializable;

import com.wlcxbj.bike.util.LogUtil;

/**
 * Created by Administrator on 2017/2/6.
 */
public class AuthNativeToken implements Serializable {
    private AuthToken authToken;
    private long lastRecentLoginTimeStamp;
    private static final String TAG = "AuthNativeToken";

    public AuthNativeToken(AuthToken authToken, long lastRecentLoginTimeStamp) {
        this.authToken = authToken;
        this.lastRecentLoginTimeStamp = lastRecentLoginTimeStamp;
    }

    /**
     * 判断登陆是否过期
     *
     * @return
     */
    public boolean isExpire() {
        long expires_in = authToken.getExpires_in() * 1000;
        long lastRecentLoginTimeStamp = getLastRecentLoginTimeStamp();
        long presentTimeStamp = System.currentTimeMillis();
        long time_interval = presentTimeStamp - lastRecentLoginTimeStamp;
        LogUtil.e(TAG, "expires_in=" + expires_in);
        LogUtil.e(TAG, "lastRecentLoginTimeStamp=" + lastRecentLoginTimeStamp);
        LogUtil.e(TAG, "presentTimeStamp=" + presentTimeStamp);
        LogUtil.e(TAG, "time_interval=" + time_interval);
        //如果再次登陆时间间隔大于有效期,那么就是过期,否则直接登陆
        if (time_interval < expires_in) {
            return false;
        }
        return true;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public long getLastRecentLoginTimeStamp() {
        return lastRecentLoginTimeStamp;
    }

    public void setLastRecentLoginTimeStamp(long lastRecentLoginTimeStamp) {
        this.lastRecentLoginTimeStamp = lastRecentLoginTimeStamp;
    }

    @Override
    public String toString() {
        return "AuthNativeToken{" +
                "authToken=" + authToken +
                ", lastRecentLoginTimeStamp=" + lastRecentLoginTimeStamp +
                '}';
    }
}
