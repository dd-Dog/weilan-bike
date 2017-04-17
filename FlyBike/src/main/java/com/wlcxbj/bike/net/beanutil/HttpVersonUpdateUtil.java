package com.wlcxbj.bike.net.beanutil;

import android.app.Activity;

import com.google.gson.Gson;
import com.wlcxbj.bike.bean.VersionBean;
import com.wlcxbj.bike.bean.trip.TripToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.net.OkhttpCallBack;
import com.wlcxbj.bike.net.OkhttpEngine;
import com.wlcxbj.bike.net.OkhttpHelper;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by itsdon on 17/4/17.
 */

public class HttpVersonUpdateUtil {
    private static final String TAG = "HttpVersonUpdateUtil";
    private OkhttpHelper okhttpHelper;
    private Activity activity;

    public HttpVersonUpdateUtil(Activity activity){
        this.activity = activity;
        okhttpHelper = OkhttpHelper.getInstance();
    }

    public void checkVersion(String accessToken, final HttpCallbackHandler<VersionBean> httpCallbackHandler){
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("version", Constants.getVersionName(activity));
            jsonBody.put("appType","2");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(jsonBody,activity);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jsonBody.toString());
        okhttpHelper.postReqWithToken(Constants.CHECK_VERSION, requestBody, Constants.API_SERVER_TOKEN_TYPE, accessToken, new OkhttpCallBack() {
            @Override
            public void success(Response response) {
                String result = null;
                try {
                    result = response.body().string();
                    if(response.isSuccessful()){
                        VersionBean versionBean = new Gson().fromJson(result, VersionBean.class);
                        if(httpCallbackHandler != null){
                            httpCallbackHandler.onSuccess(versionBean);
                            LogUtil.d(TAG,"本地版本信息上传成功"+result);
                        }
                    }else{
                        LogUtil.d(TAG,"本地版本信息上传失败"+result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Exception error, String msg) {
                if(httpCallbackHandler != null){
                    httpCallbackHandler.onFailure(error,msg);
                    LogUtil.d(TAG,"本地版本信息上传失败"+msg);
                }
            }
        });

    }

}
