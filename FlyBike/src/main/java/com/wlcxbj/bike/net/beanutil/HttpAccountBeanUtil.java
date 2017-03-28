package com.wlcxbj.bike.net.beanutil;

import android.app.Activity;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import com.wlcxbj.bike.bean.account.AccountToken;
import com.wlcxbj.bike.bean.account.AuthToken;
import com.wlcxbj.bike.bean.account.BalanceInfoToken;
import com.wlcxbj.bike.bean.account.BasicUserInfoToken;
import com.wlcxbj.bike.bean.account.BusinessParamsToken;
import com.wlcxbj.bike.bean.account.IdentityToken;
import com.wlcxbj.bike.bean.account.SmsCodeBean;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.global.Error;
import com.wlcxbj.bike.net.OkhttpCallBack;
import com.wlcxbj.bike.net.OkhttpEngine;
import com.wlcxbj.bike.net.OkhttpHelper;
import com.wlcxbj.bike.util.ToastUtil;
import com.wlcxbj.bike.util.cache.CacheUtil;
import com.wlcxbj.bike.util.LogUtil;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/13.
 */
public class HttpAccountBeanUtil {
    private static final String TAG = "HttpAccountBeanUtil";
    private static OkhttpHelper okhttpHelper;
    private Activity mContext;

    public HttpAccountBeanUtil(Activity context) {
        this.okhttpHelper = OkhttpHelper.getInstance();
        this.mContext = context;
        CacheUtil cacheUtil = new CacheUtil();
    }



    /**
     * 获取业务参数
     *
     * @param accessToken
     */
    public void getBusinessParams(String accessToken, final HttpCallbackHandler
            httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.ACCOUNT_BUSINESS_PARAMS_URL, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                LogUtil.e(TAG, "获取业务参数:" + result);
                                BusinessParamsToken businessParamsToken = new Gson().fromJson
                                        (result, BusinessParamsToken.class);
                                if (httpCallbackHandler != null && businessParamsToken.getErrcode() == Error.OK) {
                                    httpCallbackHandler.onSuccess(businessParamsToken);
                                }else {
                                    httpCallbackHandler.onFailure(null, result);
                                    ToastUtil.showUIThread(mContext, "获取系统参数失败");
                                }
                            } else {
                                LogUtil.e(TAG, "获取业务参数:" + result);
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
     * 提交实名认证信息
     *
     * @param accessToken
     * @param identitycode
     * @param fullname
     */
    public void commitIdentity(String accessToken, String identitycode, String fullname, final
    HttpCallbackHandler httpCallbackHandler) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("idno", identitycode);
            jsonBody.put("realName", fullname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(jsonBody, mContext);
        RequestBody requsetBody = RequestBody.create(MediaType.parse("application/json"),
                jsonBody.toString());
        if (okhttpHelper != null) {
            okhttpHelper.postReqWithToken(Constants.REAL_USER_INFO_AUTH, requsetBody, Constants
                            .API_SERVER_TOKEN_TYPE,
                    accessToken, new
                            OkhttpCallBack() {
                                @Override
                                public void success(Response response) {
                                    try {
                                        String result = response.body().string();
                                        LogUtil.e(TAG, "获取实名认证信息:" + result);
                                        if (response.isSuccessful()) {
                                            IdentityToken identityToken = new Gson().fromJson
                                                    (result, IdentityToken.class);
                                            if (httpCallbackHandler != null && identityToken.getErrcode() == Error.OK) {
                                                httpCallbackHandler.onSuccess(identityToken);
                                            }else {
                                                ToastUtil.showUIThread(mContext, identityToken.getErrmsg());
                                            }
                                        } else {
                                            if (httpCallbackHandler != null)
                                                httpCallbackHandler.onFailure(null, result);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void failure(Exception error, String msg) {
                                    if (httpCallbackHandler != null)
                                        httpCallbackHandler.onFailure(error, msg);
                                }
                            });
        }
    }


    /**
     * 获取帐户实名认证信息
     *
     * @param accessToken
     */
    public void getAccountRealInfo(String accessToken, final HttpCallbackHandler
            httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.ACCOUNT_REAL_INFO_URL, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                //{"errcode":0,"errmsg":"OK","basicInfo":{"avatarUrl":null,
                                // "nickName":"sssssshnnaaa"}
                                LogUtil.e(TAG, "获取实名认证信息:" + result);
//                                if (httpCallbackHandler != null && identityToken.getErrcode() == Error.OK) {
//                                    httpCallbackHandler.onSuccess(identityToken);
//                                }else {
//                                    ToastUtil.showUIThread(mContext, identityToken.getErrmsg());
//                                }
                            } else {
                                LogUtil.e(TAG, "获取实名认证信息失败:" + result);
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
     * 获取帐户余额积分信息
     *
     * @param accessToken
     */
    public void getAccountBalanceInfo(String accessToken, final HttpCallbackHandler
            httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.ACCOUNT_BALANCE_URL, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                //{"errcode":0,"errmsg":"OK","basicInfo":{"avatarUrl":null,
                                // "nickName":"sssssshnnaaa"}
                                LogUtil.e(TAG, "获取余额积分信息:" + result);
                                BalanceInfoToken balanceInfoToken = new Gson().fromJson
                                        (result, BalanceInfoToken.class);
                                if (httpCallbackHandler != null && balanceInfoToken.getErrcode() == Error.OK) {
                                    httpCallbackHandler.onSuccess(balanceInfoToken);
                                }else {
                                    ToastUtil.showUIThread(mContext, balanceInfoToken.getErrmsg());
                                }
                            } else {
                                LogUtil.e(TAG, "获取余额积分信息失败:" + result);
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
     * 获取帐户基本信息
     *
     * @param accessToken
     */
    public void getBasicUserInfo(String accessToken, final HttpCallbackHandler
            httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.URL_BASIC_ACCOUNT_INFO, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {
                                //{"errcode":0,"errmsg":"OK","basicInfo":{"avatarUrl":null,
                                // "nickName":"sssssshnnaaa"}
                                LogUtil.e(TAG, "获取帐户基本信息:" + result);
                                BasicUserInfoToken basicUserInfoToken = new Gson().fromJson
                                        (result, BasicUserInfoToken.class);
                                if (httpCallbackHandler != null && basicUserInfoToken.getErrcode() == Error.OK) {
                                    httpCallbackHandler.onSuccess(basicUserInfoToken);
                                }else {
                                    ToastUtil.showUIThread(mContext, basicUserInfoToken.getErrmsg());
                                }
                            } else {
                                LogUtil.e(TAG, "获取帐户信息失败:" + result);
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
     * 获取账户信息
     */
    public void getUserInfos(final String accessToken, final HttpCallbackHandler
            httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        okhttpHelper.postReqWithToken(Constants.URL_ACCOUNT_INFO, requestBody, Constants
                        .API_SERVER_TOKEN_TYPE,
                accessToken, new OkhttpCallBack() {
                    @Override
                    public void success(Response response) {
                        try {
                            String result = response.body().string();
                            if (response.isSuccessful()) {

                                LogUtil.e(TAG, "获取帐户信息:" + result);
                                Gson gson = new Gson();
                                AccountToken accountToken = gson.fromJson(result, AccountToken
                                        .class);
                                if (httpCallbackHandler != null && accountToken.getErrcode() == Error.OK) {
                                    httpCallbackHandler.onSuccess(accountToken);
                                }else {
                                    ToastUtil.showUIThread(mContext, accountToken.getErrmsg());
                                }
                            } else {
                                LogUtil.e(TAG, "获取帐户信息失败:" + result);
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
     * 更新用户基本信息
     *
     * @param nickName  昵称
     * @param avaterUrl 头像在OOS服务器的地址
     */
    public void updateUserInfo(String accessToken, String nickName, String avaterUrl, final
    HttpCallbackHandler httpCallbackHandler) {
        LogUtil.e(TAG, "更新用户信息");
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("nickName", nickName);
            postBody.put("avatarUrl", avaterUrl);
            OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
            LogUtil.e(TAG, "postBody=" + postBody.toString());
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                    postBody.toString());
            okhttpHelper.postReqWithToken(Constants.UPDATE_USER_INFO_URL, requestBody, Constants
                            .API_SERVER_TOKEN_TYPE,
                    accessToken, new OkhttpCallBack() {
                        @Override
                        public void success(Response response) {
                            try {
                                String result = response.body().string();
                                if (response.isSuccessful()) {
                                    //{"errcode":0,"errmsg":"OK","basicInfo":{"avatarUrl":null,
                                    // "nickName":"sssssshnn"}}
                                    LogUtil.e(TAG, "更新用户信息:" + result);
                                    BasicUserInfoToken basicUserInfoToken = new Gson()
                                            .fromJson(result, BasicUserInfoToken.class);
                                    if (httpCallbackHandler != null && basicUserInfoToken.getErrcode() == Error.OK) {
                                        httpCallbackHandler.onSuccess(basicUserInfoToken);
                                    }else {
                                        ToastUtil.showUIThread(mContext, basicUserInfoToken.getErrmsg());
                                    }
                                } else {
                                    if (httpCallbackHandler != null)
                                        httpCallbackHandler.onFailure(null, result);
                                    LogUtil.e(TAG, "更新用户信息失败:" + result);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(Exception error, String msg) {
                            LogUtil.e(TAG, "MSG=" + msg);
                            if (httpCallbackHandler != null)
                                httpCallbackHandler.onFailure(error, msg);
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登陆接口,获取登陆授权token
     *
     * @param username 用户名
     * @param psw      验证码
     */
    public void getAuthToken(String username, String psw, final HttpCallbackHandler
            httpCallbackHandler) {
        try {
            RequestBody formBody = new FormBody.Builder().add("grant_type", Constants.GRANT_TYPE)
                    .add("username", username)
                    .add("password", psw)
                    .add("version", Constants.getVerionCode(mContext) + "")
                    .add("deviceId", Constants.getDeviceId(mContext))
                    .add("channel", Constants.CHANNEL)
                    .build();
            okhttpHelper.postReqWithAuthHeader(Constants.LOGIN_SERVER, formBody, Constants
                    .AUTH_BASE_KEY, Constants.AUTH_BASE_VALUE, new OkhttpCallBack() {

                @Override
                public void success(Response response) {
                    try {
                        if (response.isSuccessful()) {
                            String result = response.body().string();
                            LogUtil.e(TAG, "response code:" + response.code() + ",response body:" +
                                    result);
                            Gson gson = new Gson();
                            AuthToken authToken = gson.fromJson(result, AuthToken.class);
                            if (httpCallbackHandler != null)
                                httpCallbackHandler.onSuccess(authToken);
                        } else {
                            if (httpCallbackHandler != null)
                                httpCallbackHandler.onFailure(null, response.body().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(Exception error, String msg) {
                    if (httpCallbackHandler != null)
                        httpCallbackHandler.onFailure(error, msg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取短信验证码
     *
     * @param mobile 手机号
     */
    public void getSmsCodeBean(String mobile, final HttpCallbackHandler httpCallbackHandler) {
        JSONObject postBody = new JSONObject();
        try {
            postBody.put("mobile", mobile);
            OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
            LogUtil.e(TAG, "postBody=" + postBody.toString());
            final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                    postBody.toString());
            okhttpHelper.postReq(Constants.SMS_CODE, requestBody, new OkhttpCallBack() {
                @Override
                public void success(Response response) {
                    try {
                        String result = response.body().string();
                        if (response.isSuccessful()) {
                            LogUtil.e(TAG, result);
                            SmsCodeBean smsCodeBean = new Gson().fromJson(result, SmsCodeBean
                                    .class);
                            if (httpCallbackHandler != null && smsCodeBean.getErrcode() == Error.OK) {
                                httpCallbackHandler.onSuccess(smsCodeBean);
                            }else {
                                ToastUtil.showUIThread(mContext, smsCodeBean.getErrmsg());
                            }
                        } else {
                            if (httpCallbackHandler != null)
                                httpCallbackHandler.onFailure(null, result);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(Exception error, String msg) {
                    LogUtil.e(TAG, "MSG=" + msg);
                    if (httpCallbackHandler != null)
                        httpCallbackHandler.onFailure(error, msg);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
