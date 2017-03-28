package com.wlcxbj.bike.oss;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import com.wlcxbj.bike.bean.oss.StsCredentialsModelBean;
import com.wlcxbj.bike.bean.oss.StsCredentialsModelNativeToken;
import com.wlcxbj.bike.bean.oss.StsCredentialsModelToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.net.OkhttpEngine;
import com.wlcxbj.bike.net.OkhttpHelper;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.cache.CacheUtil;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2015/12/9 0009.
 * 重载OSSFederationCredentialProvider生成自己的获取STS的功能
 */
public class STSGetter extends OSSFederationCredentialProvider {

    private String stsServer;
    private Context mContext;
    private OkhttpHelper okhttpHelper;
    private int accessType;
    private static final String TAG = "STSGetter";
    StsCredentialsModelNativeToken stsCredentialsModelNativeToken;
    private String tokenStr;

    public STSGetter(String stsServer, Context context, String tokenStr, int accessType) {
        this.stsServer = stsServer;
        okhttpHelper = OkhttpHelper.getInstance();
        this.mContext = context;
        this.tokenStr = tokenStr;
        this.accessType = accessType;
    }

    public OSSFederationToken getFederationToken() {
        if (accessType == Constants.READ) {
            stsCredentialsModelNativeToken = CacheUtil.getSerialToken(mContext, Constants
                    .STS_ACCESS_FILE_READ);
        } else if (accessType == Constants.WRITE) {
            stsCredentialsModelNativeToken = CacheUtil.getSerialToken(mContext, Constants
                    .STS_ACCESS_FILE_WRITE);
        }
        if (stsCredentialsModelNativeToken != null && !stsCredentialsModelNativeToken.isExpire()) {
            StsCredentialsModelBean stsCredentialsModelBean = stsCredentialsModelNativeToken
                    .getStsCredentialsModelToken().getStsCredentialsModel();
            String ak = stsCredentialsModelBean.getAccessKeyId();
            String sk = stsCredentialsModelBean.getAccessKeySecret();
            String token = stsCredentialsModelBean.getSecurityToken();
            String expiration = stsCredentialsModelBean.getExpiration();
            LogUtil.e(TAG, "get ststoken from local cache, stsCredentialsModelBean＝" + stsCredentialsModelBean);
            return new OSSFederationToken(ak, sk, token, expiration);
        }
        LogUtil.e(TAG, "ststoken local no found or expired");
        //这里改成从自己服务器获取access_token
        //1.获取写权限token
        String iconUrl = stsServer;
        JSONObject postBody = new JSONObject();
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        try {
            postBody.put("tokenTypeSpid", accessType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkhttpEngine.addPublicParamsToJsonObject(postBody, mContext);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                postBody.toString());
        String stsJson = okhttpHelper.requestPostBySyn(iconUrl, requestBody, Constants
                .API_SERVER_TOKEN_TYPE, tokenStr);
        LogUtil.e(TAG, "同步请求OSS访问sts:" + stsJson);
        LogUtil.e(TAG, "ACCESS_TYPE=" + accessType);
        try {
            StsCredentialsModelToken stsCredentialsModelToken = new Gson().fromJson(stsJson,
                    StsCredentialsModelToken.class);
            StsCredentialsModelBean stsCredentialsModel = stsCredentialsModelToken
                    .getStsCredentialsModel();

            //cache
            StsCredentialsModelNativeToken stsCredentialsModelNativeToken = new
                    StsCredentialsModelNativeToken(stsCredentialsModelToken, System
                    .currentTimeMillis());
            if (accessType == Constants.READ) {
                boolean b = CacheUtil.cacheSerialToken(mContext, Constants.STS_ACCESS_FILE_READ,
                        stsCredentialsModelNativeToken);
                LogUtil.e(TAG, "缓存读类型的stsToekn" + (b ? "成功" : "失败"));
            } else if (accessType == Constants.WRITE) {
                boolean b = CacheUtil.cacheSerialToken(mContext, Constants.STS_ACCESS_FILE_WRITE,
                        stsCredentialsModelNativeToken);
                LogUtil.e(TAG, "缓存写类型的stsToekn" + (b ? "成功" : "失败"));
                LogUtil.e(TAG, "stsCredentialsModelNativeToken=" + stsCredentialsModelNativeToken);
            }

            String ak = stsCredentialsModel.getAccessKeyId();
            String sk = stsCredentialsModel.getAccessKeySecret();
            String token = stsCredentialsModel.getSecurityToken();
            String expiration = stsCredentialsModel.getExpiration();
            return new OSSFederationToken(ak, sk, token, expiration);
        } catch (Exception e) {
            Log.e("GetSTSTokenFail", e.toString());
            e.printStackTrace();
            return null;
        }
    }

}
