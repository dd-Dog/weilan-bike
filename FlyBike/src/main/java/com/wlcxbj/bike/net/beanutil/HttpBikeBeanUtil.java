package com.wlcxbj.bike.net.beanutil;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.wlcxbj.bike.bean.bike.BikeBean;
import com.wlcxbj.bike.bean.bike.BikePswBean;
import com.wlcxbj.bike.bean.bike.BikePswToken;
import com.wlcxbj.bike.bean.bike.BikeReserveToken;
import com.wlcxbj.bike.bean.bike.CancelReserveToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.global.Error;
import com.wlcxbj.bike.net.OkhttpCallBack;
import com.wlcxbj.bike.net.OkhttpEngine;
import com.wlcxbj.bike.net.OkhttpHelper;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.ToastUtil;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/13.
 */
public class HttpBikeBeanUtil {
    private static final String TAG = "HttpBikeBeanUtil";
    private static OkhttpHelper okhttpHelper;
    private Activity mContext;

    public HttpBikeBeanUtil(Activity context) {
        this.okhttpHelper = OkhttpHelper.getInstance();
        this.mContext = context;
    }

    /**
     * 获取车辆密码(更新)
     *
     * @param accessToken
     * @param bikePswBean
     */
    public void getBikePsw2(String accessToken, final BikePswBean bikePswBean, final
    HttpCallbackHandler
            httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("tno", bikePswBean.getTno());
            postBody.put("userlng", bikePswBean.getUserlng());
            postBody.put("userlat", bikePswBean.getUserlat());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.BIKE_PSW2, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            BikePswToken bikePswToken = new Gson().fromJson(result,
                                    BikePswToken.class);
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取车辆密码:" + result);
                                if (httpCallbackHandler != null && bikePswToken.getErrcode() ==
                                        Error.OK) {
                                    httpCallbackHandler.onSuccess(bikePswToken);
                                } else {
                                    httpCallbackHandler.onFailure(null, result);
                                    ToastUtil.showUIThread(mContext, bikePswToken.getErrmsg());
                                }
                            } else {
                                LogUtil.e(TAG, "获取车辆密码:" + result);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onFailure(null, bikePswToken.getErrmsg());
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
     * 请求取消预定车辆
     *
     * @param accessToken
     */
    public void cancelReservationBike(String accessToken, String reservationId, final
    HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("reservationId", reservationId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.CANCLE_RESERVATION, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "请求取消预定车辆信息:" + result);
                                CancelReserveToken cancelReserveToken = new Gson().fromJson(result,
                                        CancelReserveToken.class);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onSuccess(cancelReserveToken);
                            } else {
                                LogUtil.e(TAG, "请求取消预定车辆失败:" + result);
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
     * 请求预定车辆
     *
     * @param accessToken
     */
    public void reserveBike(String accessToken, BikeBean bikeBean, final HttpCallbackHandler
            httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("tid", bikeBean.getTid());
            postBody.put("plateno", bikeBean.getPlateno());
            postBody.put("bikelng", bikeBean.getBikelng());
            postBody.put("bikelat", bikeBean.getBikelat());
            postBody.put("userlng", bikeBean.getUserlng());
            postBody.put("userlat", bikeBean.getUserlat());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.RESERVE_BIKE, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取请求预定车辆信息:" + result);
                                BikeReserveToken bikeReserveToken = new Gson().fromJson(result,
                                        BikeReserveToken.class);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onSuccess(bikeReserveToken);
                            } else {
                                LogUtil.e(TAG, "获取请求预定车辆失败:" + result);
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
     * 请求车辆密码
     *
     * @param accessToken 锁的编号通过扫码二维码获取	与plateno二者必填其一
     */
    public void getBikePassword(String accessToken, BikePswBean bikePswBean, final
    HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("tid", bikePswBean.getTid());
            postBody.put("plateno", bikePswBean.getPlateno());
            postBody.put("userlng", bikePswBean.getUserlng());
            postBody.put("userlat", bikePswBean.getUserlat());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.REQUEST_BIKE_PSW, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取请求车辆密码信息:" + result);
                                BikePswToken bikePswToken = new Gson().fromJson(result,
                                        BikePswToken.class);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onSuccess(bikePswToken);
                            } else {
                                LogUtil.e(TAG, "获取请求车辆密码信息失败:" + result);
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
