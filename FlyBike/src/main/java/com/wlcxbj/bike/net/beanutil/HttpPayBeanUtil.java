package com.wlcxbj.bike.net.beanutil;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.wlcxbj.bike.bean.pay.ALiPayToken;
import com.wlcxbj.bike.bean.pay.OrderBean;
import com.wlcxbj.bike.bean.pay.WechatPayToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.net.OkhttpCallBack;
import com.wlcxbj.bike.net.OkhttpEngine;
import com.wlcxbj.bike.net.OkhttpHelper;
import com.wlcxbj.bike.util.LogUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/14.
 */
public class HttpPayBeanUtil {

    private static final String TAG = "HttpPayBeanUtil";
    private static OkhttpHelper okhttpHelper;
    private Context mContext;

    public HttpPayBeanUtil(Context context) {
        this.okhttpHelper = OkhttpHelper.getInstance();
        this.mContext = context;
    }


    /**
     * 获取微信支付token
     * @param accessToken
     * @param orderBean
     */
    public void getWechatPayToken(String accessToken, OrderBean orderBean, final HttpCallbackHandler httpCallbackHandler) {
        if (orderBean == null) return;
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("orderTypeSpid", orderBean.getOrderTypeSpid());
            postBody.put("orderAmount", orderBean.getOrderAmount());
            postBody.put("realpayAmount", orderBean.getRealpayAmount());
            postBody.put("cuid", orderBean.getRealpayAmount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.WECHAT_ORDER, requestBody, Constants
                .API_SERVER_TOKEN_TYPE, accessToken, new OkhttpCallBack() {
            @Override
            public void success(Response response) {
                try {
                    String result = response.body().string();
                    LogUtil.e(TAG, "wechatPay result =" + result);
                    if (response.isSuccessful()) {
                        WechatPayToken wechatPayToken = new Gson().fromJson(result, WechatPayToken.class);
                        if (httpCallbackHandler != null)
                            httpCallbackHandler.onSuccess(wechatPayToken);
                    } else {
                        LogUtil.e(TAG, "获取请微信支付信息失败:" + result);
                        if (httpCallbackHandler != null)
                            httpCallbackHandler.onFailure(null, result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Exception error, String msg) {
                LogUtil.e(TAG, "连接服务器失败");
                if (httpCallbackHandler != null)
                    httpCallbackHandler.onFailure(error, msg);
            }
        });
    }

    /**
     * 支付宝支付接口
     * @param accessToken
     */
    public void getAlipayToken(String accessToken, OrderBean orderBean, final HttpCallbackHandler httpCallbackHandler) {
        if (orderBean == null) return;
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("orderTypeSpid", orderBean.getOrderTypeSpid());
            postBody.put("orderAmount", orderBean.getOrderAmount());
            postBody.put("realpayAmount", orderBean.getRealpayAmount());
            postBody.put("cuid", orderBean.getRealpayAmount());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.ALI_PAY, requestBody, Constants
                .API_SERVER_TOKEN_TYPE, accessToken, new OkhttpCallBack() {
            @Override
            public void success(Response response) {
                try {
                    String result = response.body().string();
                    LogUtil.e(TAG, "alipay result =" + result);
                    if (response.isSuccessful()) {
                        ALiPayToken payToken = new Gson().fromJson(result, ALiPayToken.class);
                        if (httpCallbackHandler != null)
                            httpCallbackHandler.onSuccess(payToken);
                    } else {
                        LogUtil.e(TAG, "获取请支付宝支付信息失败:" + result);
                        if (httpCallbackHandler != null)
                            httpCallbackHandler.onFailure(null, result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Exception error, String msg) {
                LogUtil.e(TAG, "连接服务器失败");
                if (httpCallbackHandler != null)
                    httpCallbackHandler.onFailure(error, msg);
            }
        });
    }

}
