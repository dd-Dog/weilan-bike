package com.wlcxbj.bike.net.beanutil;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.wlcxbj.bike.bean.bike.ReportProblemBean;
import com.wlcxbj.bike.bean.bike.TiketsExchangeToken;
import com.wlcxbj.bike.bean.commonaddress.CommonAddressBean;
import com.wlcxbj.bike.bean.commonaddress.CommonAddressListToken;
import com.wlcxbj.bike.bean.commonaddress.SaveCommonAddressToken;
import com.wlcxbj.bike.bean.other.CouponsToken;
import com.wlcxbj.bike.bean.other.InviteCodeToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.net.OkhttpCallBack;
import com.wlcxbj.bike.net.OkhttpEngine;
import com.wlcxbj.bike.net.OkhttpHelper;
import com.wlcxbj.bike.util.LogUtil;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/13.
 */
public class HttpAccountOtherBeanUtil {

    private static final String TAG = "HttpAccountBeanUtil";
    private static OkhttpHelper okhttpHelper;
    private Context mContext;

    public HttpAccountOtherBeanUtil(Context context) {
        this.okhttpHelper = OkhttpHelper.getInstance();
        this.mContext = context;
    }
    /**
     * 删除常用地址
     *
     * @param accessToken
     */
    public void deketeCommonAddress(String accessToken, long addressID, final
    HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("id", addressID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.DELETE_COMMON_ADDRESS, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "删除常用地址:" + result);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onSuccess(result);
                            } else {
                                LogUtil.e(TAG, "删除常用地址失败:" + result);
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
     * 保存常用地址
     *
     * @param accessToken
     */
    public void saveCommonAddress(String accessToken, CommonAddressBean commonAddressBean, final
    HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("id", (commonAddressBean.getId() == -1 ? "" : commonAddressBean.getId()));
            postBody.put("lng", commonAddressBean.getLng());
            postBody.put("lat", commonAddressBean.getLat());
            postBody.put("addr", commonAddressBean.getAddr());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.SAVE_COMMON_ADDRESS, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取保存常用地址列表信息:" + result);
                                SaveCommonAddressToken saveCommonAddressToken = new Gson()
                                        .fromJson(result, SaveCommonAddressToken.class);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onSuccess(saveCommonAddressToken);
                            } else {
                                LogUtil.e(TAG, "获取保存常用地址列表失败:" + result);
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
     * 获取常用地址列表
     *
     * @param accessToken
     */
    public void getCommonAddressList(String accessToken, final HttpCallbackHandler
            httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.COMMON_ADDRESS_LIST, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取常用地址列表信息:" + result);
                                CommonAddressListToken commonAddressListToken = new Gson()
                                        .fromJson(result, CommonAddressListToken.class);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onSuccess(commonAddressListToken);
                            } else {
                                LogUtil.e(TAG, "获取常用地址列表失败:" + result);
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
     * 兑换优惠券
     *
     * @param accessToken
     * @param couponId
     */
    public void exchangeCoupon(String accessToken, int couponId, final HttpCallbackHandler
            httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("couponId", couponId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.EXCHANGE_COUPON, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取兑换优惠券信息:" + result);
                                if (httpCallbackHandler != null) {
                                    TiketsExchangeToken tiketsExchangeToken = new Gson().fromJson
                                            (result, TiketsExchangeToken.class);
                                    httpCallbackHandler.onSuccess(tiketsExchangeToken);
                                }
                            } else {
                                LogUtil.e(TAG, "获取兑换优惠券信息失败:" + result);
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
     * 获取优惠券列表
     *
     * @param accessToken
     * @param coupType
     * @param coupStatus
     */
    public void getCouponsList(String accessToken, int coupType, int coupStatus, final
    HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("typeSpid", coupType == -1 ? "" : coupType);
            postBody.put("statusSpid", coupStatus == -1 ? "" : coupStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.COUPONS_LIST, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取优惠券列表信息:" + result);
                                CouponsToken couponToken = new Gson().fromJson(result, CouponsToken
                                        .class);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onSuccess(couponToken);
                            } else {
                                LogUtil.e(TAG, "获取优惠券列表信息失败:" + result);
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
     * 修改邀请码
     *
     * @param accessToken
     * @param inviteCode
     */
    public void modifyInviceCode(String accessToken, String inviteCode, final HttpCallbackHandler
            httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("inviteNote", inviteCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.MODIFY_INVICECODE_URL, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取修改邀请码信息:" + result);
                                InviteCodeToken inviteCodeToken = new Gson().fromJson(result,
                                        InviteCodeToken.class);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onSuccess(inviteCodeToken);
                            } else {
                                LogUtil.e(TAG, "获取修改邀请码信息失败:" + result);
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
     * 查询邀请码
     *
     * @param accessToken
     */
    public void getInviteCode(String accessToken, final HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.REQUEST_INVICECODE_URL, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取查询邀请码信息:" + result);
                                InviteCodeToken inviteCodeToken = new Gson().fromJson(result,
                                        InviteCodeToken.class);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onSuccess(inviteCodeToken);
                            } else {
                                LogUtil.e(TAG, "获取查询邀请码信息失败:" + result);
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
     * 报告问题
     *
     * @param accessToken
     * @param reportProblemBean
     */
    public void reportProblem(String accessToken, ReportProblemBean reportProblemBean, final
    HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("tid", reportProblemBean.getTid());
            postBody.put("issueTypeSpid", reportProblemBean.getTid());
            postBody.put("lat", reportProblemBean.getLat());
            postBody.put("lng", reportProblemBean.getLng());
            postBody.put("title", reportProblemBean.getTitle());
            postBody.put("content", reportProblemBean.getContent());
            postBody.put("note", reportProblemBean.getNote());
            postBody.put("img1", reportProblemBean.getLng());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.REPORT_PROBLEM, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "报告问题信息:" + result);
                                if (httpCallbackHandler != null)
                                    httpCallbackHandler.onSuccess(result);
                            } else {
                                LogUtil.e(TAG, "报告问题失败:" + result);
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
