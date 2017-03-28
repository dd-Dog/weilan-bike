package com.wlcxbj.bike.receiver;

import android.content.Context;

import com.alibaba.sdk.android.push.notification.CPushMessage;

import java.util.Map;

/**
 * Created by Administrator on 2017/2/23.
 */
public class AliMessageCallbackHandlerAdapter implements AliMessageCallbackHandler {
    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String>
            map) {

    }

    @Override
    public void onNotificationClickedWithNoAction(Context context, String title, String summary,
                                                  String extraMap) {

    }

    @Override
    public void onNotificationRemoved(Context context, String messageId) {

    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String
            extraMap) {

    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {

    }

    @Override
    public void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {

    }
}
