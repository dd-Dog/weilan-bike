package com.wlcxbj.bike.bean.oss;

import java.io.Serializable;

import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.TimeUtil;

/**
 * Created by Administrator on 2017/2/20.
 */
public class StsCredentialsModelNativeToken implements Serializable {
    private long lastRecentLoginTimeStamp;
    private static final String TAG = "StsCredentialsModelNativeToken";
    private StsCredentialsModelToken stsCredentialsModelToken;



    public StsCredentialsModelNativeToken(StsCredentialsModelToken stsCredentialsModelToken, long lastRecentLoginTimeStamp) {
        this.stsCredentialsModelToken = stsCredentialsModelToken;
        this.lastRecentLoginTimeStamp = lastRecentLoginTimeStamp;
    }

    /**
     * 判断sts_token是否过期
     *
     * @return
     */
    public boolean isExpire() {
        String expiration = stsCredentialsModelToken.getStsCredentialsModel().getExpiration();
        long expires_in = TimeUtil.getMillionsFromISODate(expiration);
        long lastRecentLoginTimeStamp = getLastRecentLoginTimeStamp();
        long presentTimeStamp = System.currentTimeMillis();
//        long time_interval = presentTimeStamp - lastRecentLoginTimeStamp;
        LogUtil.e(TAG, "expires_in=" + expires_in);
        LogUtil.e(TAG, "lastRecentLoginTimeStamp=" + lastRecentLoginTimeStamp);
        LogUtil.e(TAG, "presentTimeStamp=" + presentTimeStamp);
//        LogUtil.e(TAG, "time_interval=" + time_interval);
        //如果再次登陆时间间隔大于有效期,那么就是过期,否则直接登陆
        if (presentTimeStamp < expires_in) {
            return false;
        }
        return true;
    }

    public StsCredentialsModelToken getStsCredentialsModelToken() {
        return stsCredentialsModelToken;
    }

    private long getLastRecentLoginTimeStamp() {
        return lastRecentLoginTimeStamp;
    }

    @Override
    public String toString() {
        return "StsCredentialsModelNativeToken{" +
                "lastRecentLoginTimeStamp=" + lastRecentLoginTimeStamp +
                ", stsCredentialsModelToken=" + stsCredentialsModelToken +
                '}';
    }
}
