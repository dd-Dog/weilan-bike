package com.wlcxbj.bike.net.beanutil;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.wlcxbj.bike.bean.trip.TripPointBean;
import com.wlcxbj.bike.bean.trip.TripToken;
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
public class HttpTripBeanUtil {
    private static final String TAG = "HttpTripBeanUtil";
    private static OkhttpHelper okhttpHelper;
    private Context mContext;

    public HttpTripBeanUtil(Context context) {
        this.okhttpHelper = OkhttpHelper.getInstance();
        this.mContext = context;
    }

    /**
     * 结束行程
     * @param accessToken
     * @param tripPointBean
     */
    public void endTrip(String accessToken, TripPointBean tripPointBean, final HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("tid", tripPointBean.getTid());
            postBody.put("bikelng", tripPointBean.getBikelng());
            postBody.put("bikelat", tripPointBean.getBikelat());
            postBody.put("userlng", tripPointBean.getUserlng());
            postBody.put("userlat", tripPointBean.getUserlat());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.TRIP_STOP_TIMING, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取结束行程信息:" + result);
//                                TripToken tripToken = new Gson().fromJson(result, TripToken.class);
//                                if (httpCallbackHandler != null)
//                                    httpCallbackHandler.onSuccess(tripToken);
                            } else {
                                LogUtil.e(TAG, "获取结束行程失败:" + result);
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
     * 开始行程
     * @param accessToken
     * @param tripPointBean
     */
    public void startTrip(String accessToken, TripPointBean tripPointBean, final HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("tid", tripPointBean.getTid());
            postBody.put("bikelng", tripPointBean.getBikelng());
            postBody.put("bikelat", tripPointBean.getBikelat());
            postBody.put("userlng", tripPointBean.getUserlng());
            postBody.put("userlat", tripPointBean.getUserlat());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.TRIP_START_TIMING, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取开始行程信息:" + result);
                                TripToken tripToken = new Gson().fromJson(result, TripToken.class);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onSuccess(tripToken);
                            } else {
                                LogUtil.e(TAG, "获取开始行程失败:" + result);
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
