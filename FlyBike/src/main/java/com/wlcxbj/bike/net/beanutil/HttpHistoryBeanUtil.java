package com.wlcxbj.bike.net.beanutil;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.wlcxbj.bike.bean.history.RechargeListBean;
import com.wlcxbj.bike.bean.history.RentDetailToken;
import com.wlcxbj.bike.bean.history.RequestRentHistoryListBean;
import com.wlcxbj.bike.bean.history.RentHistoryListToken;
import com.wlcxbj.bike.bean.history.TransactionListBean;
import com.wlcxbj.bike.bean.history.TransactionListToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.global.Error;
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
public class HttpHistoryBeanUtil {
    private static final String TAG = "HttpTripBeanUtil";
    private static OkhttpHelper okhttpHelper;
    private Context mContext;

    public HttpHistoryBeanUtil(Context context) {
        this.okhttpHelper = OkhttpHelper.getInstance();
        this.mContext = context;
    }

    /**
     * 获取交易明细记录信息
     *
     * @param accessToken
     * @param transactionListBean
     */
    public void getTransactionList(String accessToken, final TransactionListBean
            transactionListBean,
                                   final HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("orderTypeSpids", transactionListBean.getOrderTypeSpids());
            postBody.put("startTime", transactionListBean.getStartTime());
            postBody.put("endTime", transactionListBean.getEndTime());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.TRANSACTION_LIST, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取交易明细记录信息:" + result);
                                TransactionListToken transactionListToken = new Gson().fromJson
                                        (result,
                                        TransactionListToken.class);
                                if (transactionListToken.getErrcode() == Error.OK) {
                                    if (httpCallbackHandler != null)
                                        httpCallbackHandler.onSuccess(transactionListToken);

                                } else {
                                    if (httpCallbackHandler != null)
                                        httpCallbackHandler.onFailure(null, transactionListToken.getErrmsg());
                                }
                            } else {
                                LogUtil.e(TAG, "获取交易明细记录信息:" + result);
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
     * 获取押金充值记录信息
     *
     * @param accessToken
     * @param rechargeListBean
     */
    public void getDepositHistoryList(String accessToken, RechargeListBean rechargeListBean,
                                      final HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("payTypeSpid", rechargeListBean.getPayTypeSpid());
            postBody.put("pageNo", rechargeListBean.getPageNo());
            postBody.put("pageSize", rechargeListBean.getPageSize());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.DEPOSIT_HISTORY_LIST, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取押金充值记录信息:" + result);
//                                RentDetailToken rentDetailToken = new Gson().fromJson(result,
//                                        RentDetailToken.class);
//                                if (httpCallbackHandler != null)
//                                    httpCallbackHandler.onSuccess(rentDetailToken);
                            } else {
                                LogUtil.e(TAG, "获取押金充值记录信息:" + result);
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
     * 获取充值记录信息
     *
     * @param accessToken
     * @param rechargeListBean
     */
    public void getRechargeHistoryList(String accessToken, RechargeListBean rechargeListBean,
                                       final HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("payTypeSpid", rechargeListBean.getPayTypeSpid());
            postBody.put("pageNo", rechargeListBean.getPageNo());
            postBody.put("pageSize", rechargeListBean.getPageSize());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.RECHARGE_HISOTRY_LIST, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取充值记录信息:" + result);
//                                RentDetailToken rentDetailToken = new Gson().fromJson(result,
//                                        RentDetailToken.class);
//                                if (httpCallbackHandler != null)
//                                    httpCallbackHandler.onSuccess(rentDetailToken);
                            } else {
                                LogUtil.e(TAG, "获取充值记录信息:" + result);
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
     * 获取单个租车记录详情
     *
     * @param accessToken
     * @param rentId
     */
    public void getRentDetial(String accessToken, long rentId, final HttpCallbackHandler
            httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("rentId", rentId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.RENT_HISTORY_DETAIL, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取获取单个租车记录详情信息:" + result);
                                RentDetailToken rentDetailToken = new Gson().fromJson(result,
                                        RentDetailToken.class);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onSuccess(rentDetailToken);
                            } else {
                                LogUtil.e(TAG, "获取获取单个租车记录详情失败:" + result);
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
     * 获取租车记录
     *
     * @param accessToken
     * @param rentHistoryListBean
     */
    public void getRentHistory(String accessToken, RequestRentHistoryListBean
            rentHistoryListBean, final
    HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("startTime", rentHistoryListBean.getStartTime());
            postBody.put("endTime", rentHistoryListBean.getEndTime());
            postBody.put("pageNo", rentHistoryListBean.getPageNo());//
            postBody.put("pageSize", rentHistoryListBean.getPageSize());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.RENT_HISTORY_LIST, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取租车记录信息:" + result);
                                if (httpCallbackHandler != null) {
                                    RentHistoryListToken rentHistoryListToken = new Gson()
                                            .fromJson(result, RentHistoryListToken.class);
                                    httpCallbackHandler.onSuccess(rentHistoryListToken);
                                }

                            } else {
                                LogUtil.e(TAG, "获取租车记录信息失败:" + result);
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
