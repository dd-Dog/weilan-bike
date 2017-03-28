package com.wlcxbj.bike.oss;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import com.alibaba.sdk.android.oss.model.OSSResult;


/**
 * Created by Administrator on 2015/12/21 0021.
 */
public class UICallback<T1 extends OSSRequest, T2 extends OSSResult> implements OSSCompletedCallback<T1,T2> {
    private UIDispatcher uiDispatcher;

    private CallbackGroup onSuc = new CallbackGroup();
    private CallbackGroup onFail = new CallbackGroup();

    public UICallback(UIDispatcher UIDispatcher) {
        this.uiDispatcher = UIDispatcher;
    }

    public UICallback<T1, T2> addCallback(Runnable suc, Runnable fail) {
        if (suc != null) {
            onSuc.add(suc);
        }
        if (fail != null) {
            onFail.add(fail);
        }
        return this;
    }
    public UICallback<T1, T2> addCallback(Runnable r) {
        if (r != null) {
            onSuc.add(r);
            onFail.add(r);
        }
        return this;
    }

    /**
     * 使用handler发送消息,调用请求成功的回调
     * @param request
     * @param result
     */
    public void onSuccess(T1 request, T2 result) {
        Log.d("UICallback", "OnSuccess");
        uiDispatcher.obtainMessage(uiDispatcher.UI_MESSAGE, onSuc).sendToTarget();
    }
    /**
     * 使用handler发送消息,调用请求失败的回调
     * @param request
     * @param clientExcepion
     * @param serviceException
     */
    public void onFailure(T1 request, ClientException clientExcepion, ServiceException serviceException) {
        Log.d("UICallback", "OnFail");
        uiDispatcher.obtainMessage(uiDispatcher.UI_MESSAGE, onFail).sendToTarget();
    }
}
