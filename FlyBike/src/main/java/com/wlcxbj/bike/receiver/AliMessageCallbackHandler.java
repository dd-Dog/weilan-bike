package com.wlcxbj.bike.receiver;

import android.content.Context;

import com.alibaba.sdk.android.push.notification.CPushMessage;

import java.util.Map;

/**
 * Created by Administrator on 2017/2/23.
 */
public interface AliMessageCallbackHandler {
    void onNotification(Context context, String title, String summary, Map<String, String> map);

    void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap);

    void onNotificationRemoved(Context context, String messageId);

    void onNotificationOpened(Context context, String title, String summary, String extraMap);
    void onMessage(Context context, CPushMessage cPushMessage);
    void onNotificationReceivedInApp(Context context, String title, String summary,
                                     Map<String, String> extraMap, int openType, String
                                             openActivity, String openUrl);
}