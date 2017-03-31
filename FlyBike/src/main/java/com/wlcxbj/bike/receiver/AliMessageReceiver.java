package com.wlcxbj.bike.receiver;

import android.content.Context;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import java.util.Map;

import com.wlcxbj.bike.activity.MapActivity;
import com.wlcxbj.bike.util.LogUtil;

/**
 * Created by Administrator on 2017/2/22.
 */
public class AliMessageReceiver extends MessageReceiver {

    private static final String TAG = "AliMessageReceiver";
    private MapActivity mapActivity;
    private AliMessageCallbackHandler aliMessageCallbackHandler;
    public AliMessageReceiver(MapActivity mapActivity) {
        this.mapActivity = mapActivity;
    }

    public AliMessageReceiver() {
    }

    /**
     * 通知接收回调
     *
     * @param context context 上下文环境
     * @param title   title 通知标题
     * @param summary summary 通知内容
     * @param map     extraMap 通知额外参数，包括部分系统自带参数：
     *                _ALIYUN_NOTIFICATION_ID_(V2.3.5及以上):创建通知对应id
     *                _ALIYUN_NOTIFICATION_PRIORITY_(V2.3.5及以上):创建通知对应id。默认不带，需要通过OpenApi设置
     */
    @Override
    protected void onNotification(Context context, String title, String summary, Map<String,
            String> map) {
        super.onNotification(context, title, summary, map);
        LogUtil.e(TAG, "通知接收回调");
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                LogUtil.e(TAG, "@Get diy param : Key=" + entry.getKey() + " , Value=" + entry
                        .getValue());
            }
        } else {
            LogUtil.e(TAG, "@收到通知 && 自定义消息为空");
        }
        LogUtil.e(TAG, "收到一条推送通知 ： " + title);

        if (aliMessageCallbackHandler != null) {
            aliMessageCallbackHandler.onNotification(context, title, summary, map);
        }
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary,
                                               Map<String, String> extraMap, int openType, String
                                                       openActivity, String openUrl) {
        LogUtil.e(TAG, "onNotificationReceivedInApp ： " + " : " + title + " : " + summary + "  "
                + extraMap + " : " + openType + " : " + openActivity + " : " + openUrl);
        aliMessageCallbackHandler.onNotificationReceivedInApp(context, title, summary, extraMap, openType, openActivity, openUrl);
    }

    /**
     * 推送消息的回调方法    *
     *
     * @param context
     * @param cPushMessage
     */
    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        LogUtil.e(TAG, "收到一条推送消息 ： " + cPushMessage.getTitle() + cPushMessage.getContent());
        aliMessageCallbackHandler.onMessage(context, cPushMessage);
    }

    /**
     * 从通知栏打开通知的扩展处理
     *
     * @param context
     * @param title
     * @param summary
     * @param extraMap
     */
    @Override
    public void onNotificationOpened(Context context, String title, String summary, String
            extraMap) {
        CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
//        cloudPushService.setNotificationSoundFilePath();
        LogUtil.e(TAG, "onNotificationOpened ： " + " : " + title + " : " + summary + " : " +
                extraMap);
        aliMessageCallbackHandler.onNotificationOpened(context, title, summary, extraMap);
    }


    @Override
    public void onNotificationRemoved(Context context, String messageId) {
        LogUtil.e(TAG, "onNotificationRemoved ： " + messageId);
        aliMessageCallbackHandler.onNotificationRemoved(context, messageId);
    }


    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String
            summary, String extraMap) {
        LogUtil.e(TAG, "onNotificationClickedWithNoAction ： " + " : " + title + " : " + summary +
                " : " + extraMap);
        aliMessageCallbackHandler.onNotificationClickedWithNoAction(context, title, summary, extraMap);
    }

    public void setAliCallbackHandler(AliMessageCallbackHandler aliCallbackHandler) {
        this.aliMessageCallbackHandler = aliCallbackHandler;
    }
}
