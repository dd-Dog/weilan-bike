package com.wlcxbj.bike.thread;

import android.app.Activity;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * Created by Administrator on 2017/2/14.
 */

/**
 * 阿里支付线程
 */
public class AliPayThread extends Thread {
    public static final int SDK_PAY_FLAG = 1;
    private AliPaySyncCallbackHandler aliPaySyncCallbackHandler;
    private String mOrderInfo;
    private Activity mActivity;

    public AliPayThread(Activity activity, String orderInfo) {
        this.mActivity = activity;
        this.mOrderInfo = orderInfo;
    }

    @Override
    public void run() {
        PayTask alipay = new PayTask(mActivity);
        //设置为沙箱模式,正式版本应该注释掉
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        Map<String, String> result = alipay.payV2(mOrderInfo, true);
        Log.i("msp", result.toString());

        if (aliPaySyncCallbackHandler != null) {
            aliPaySyncCallbackHandler.payBack(result);
        }
    }

    public void setAliPaySyncCallbackHandler(AliPaySyncCallbackHandler aliPaySyncCallbackHandler) {
        this.aliPaySyncCallbackHandler = aliPaySyncCallbackHandler;
    }

    public interface AliPaySyncCallbackHandler {
        void payBack(Map<String, String> result);
    }
}
