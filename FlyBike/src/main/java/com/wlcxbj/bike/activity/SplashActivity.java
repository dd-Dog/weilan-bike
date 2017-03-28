package com.wlcxbj.bike.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.account.AccountToken;
import com.wlcxbj.bike.bean.account.AuthNativeToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.net.OkhttpCallBack;
import com.wlcxbj.bike.net.OkhttpHelper;
import com.wlcxbj.bike.util.cache.CacheUtil;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.PreferenceUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by bain on 17-1-20.
 */
public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    private AuthNativeToken authNativeToken;
    private OkhttpHelper okhttpHelper;
    private AccountToken accountToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        okhttpHelper = OkhttpHelper.getInstance();
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        saveUserInfo("liujt", "Fly123456", "56");
        PreferenceUtil.putBoolean(this, Constants.UNBLOCK_BLE_ENABLED, true);
        checkLogin();
    }

    /**
     * 检测本地缓存,是否曾经登陆过,判断是否登陆过期
     */
    private void checkLogin() {
        authNativeToken = CacheUtil.getSerialToken(SplashActivity.this, Constants.AUTH_CACHE_FILE_NAME);
        LogUtil.e(TAG, "反序列化,authNativeToken:" + authNativeToken);
        if (authNativeToken == null) {
            Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
            intent.putExtra(RegisterActivity.WHERE_FROM, RegisterActivity.SPLASH_START);
            startActivity(intent);
            finish();
            LogUtil.e(TAG, "登陆已过期,重新登陆");
        } else {
            if (authNativeToken.isExpire()) {
                Intent registerIntent = new Intent(SplashActivity.this, RegisterActivity.class);
                registerIntent.putExtra(RegisterActivity.WHERE_FROM, RegisterActivity.SPLASH_START);
                startActivity(registerIntent);
                finish();
                LogUtil.e(TAG, "登陆已过期,重新登陆");
            } else {
                checkPass();
                getUserInfo();
                LogUtil.e(TAG, "登陆未过期,有效");
            }
        }
    }


    /**
     * 获取帐户信息
     */
    private void getUserInfo() {
        LogUtil.e(TAG, "getUserInfo");
        //空json字符串是"{}"
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{}");
        String tokeType = "Bearer";

        okhttpHelper.postReqWithToken(Constants.URL_ACCOUNT_INFO, requestBody,tokeType, authNativeToken.getAuthToken().getAccess_token(), new OkhttpCallBack() {
            @Override
            public void success(Response response) {
                try {
                    String result = response.body().string();
                    if (response.isSuccessful()) {

                        LogUtil.e(TAG, "获取帐户信息:" + result);
                        Gson gson = new Gson();
                        accountToken = gson.fromJson(result, AccountToken.class);
                        //缓存帐户信息
                        cacheAccountToken(accountToken);
                    } else {
                        LogUtil.e(TAG, "获取帐户信息失败:" + result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(Exception error, String msg) {
                LogUtil.e(TAG, "连接服务器失败");
            }
        });
    }

    /**
     * 缓存帐户信息
     *
     * @param accountToken
     */
    private void cacheAccountToken(AccountToken accountToken) {
        LogUtil.e(TAG, "缓存帐户信息");
        String dir = getCacheDir().getPath() + File.separator + Constants
                .ACCOUNT_TOKEN_CACHE_FILE_NAME;
        File file = new File(dir);
        if (file.exists()) {
            file.delete();
        }
        CacheUtil.cacheSerialToken(SplashActivity.this, Constants
                .ACCOUNT_TOKEN_CACHE_FILE_NAME, accountToken);
    }

    private void checkPass() {
        //使用Handler发送延迟消息
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                login("liujt", "56");
                return false;
            }
        }).sendEmptyMessageDelayed(0, 1000);
    }

    private void login(String name, String uid) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("user_name", name);
        Bundle bundle = new Bundle();
        //把用户帐户信息传递到MapActivity
        bundle.putSerializable(MapActivity.ACCOUNT_TOKEN, accountToken);
        bundle.putSerializable(MapActivity.AUTH_NATIVE_TOKEN, authNativeToken);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void saveUserInfo(String name, String passwd, String uid) {
        //检查是否保存用户名和密码
        //保存checkbox状态和用户名密码
        PreferenceUtil.putBoolean(this, Constants.SAVEPWD_ENABLED, true ? true :
                false);
        PreferenceUtil.putString(this, Constants.SAVED_USERNAME, true ? name : "");
        PreferenceUtil.putString(this, Constants.SAVED_PASSWD, true ? passwd : "");
        PreferenceUtil.putBoolean(this, Constants.AUTOLOGIN_ENABLED, true ? true
                : false);
        PreferenceUtil.putString(this, Constants.UID, uid);
    }
}
