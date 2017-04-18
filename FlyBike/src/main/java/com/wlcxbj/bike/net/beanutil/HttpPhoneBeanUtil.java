package com.wlcxbj.bike.net.beanutil;

import android.app.Activity;

import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.global.ShareBikeApplication;
import com.wlcxbj.bike.net.OkhttpCallBack;
import com.wlcxbj.bike.net.OkhttpEngine;
import com.wlcxbj.bike.net.OkhttpHelper;
import com.wlcxbj.bike.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by itsdon on 17/4/14.
 */

public class HttpPhoneBeanUtil {

    private static final String TAG = "HttpPhoneBeanUtil";
    private OkhttpHelper okhttpHelper;
    private Activity activity;

    public HttpPhoneBeanUtil(Activity activity){
        this.activity = activity;
        okhttpHelper = OkhttpHelper.getInstance();
    }

    public void submitDeviceId2Server(String accessToken,String pushId,final HttpCallbackHandler httpCallbackHandler){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("pushId",pushId);
            LogUtil.d(TAG,"推送id"+pushId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(jsonBody, activity);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jsonBody.toString());
        okhttpHelper.postReqWithToken(Constants.POST_DEVICEID_TO_SERVEER, requestBody, Constants.API_SERVER_TOKEN_TYPE, accessToken, new OkhttpCallBack() {
            @Override
            public void success(Response response) {
                String result = response.body().toString();
                if(response.isSuccessful()){
                    LogUtil.d(TAG,"提交deviceId成功响应"+result);
                    if(httpCallbackHandler != null){
                        httpCallbackHandler.onSuccess(null);
                    }
                }else{
                    LogUtil.d(TAG,"提交deviceId失败响应"+result);
                }

            }

            @Override
            public void failure(Exception error, String msg) {
                LogUtil.d(TAG,"提交deviceId失败响应"+msg);
                if(httpCallbackHandler != null){
                    httpCallbackHandler.onFailure(error,msg);
                }
            }
        });

    }



}
