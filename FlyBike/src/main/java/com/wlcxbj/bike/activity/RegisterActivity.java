package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.sdk.android.push.CommonCallback;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.account.AccountToken;
import com.wlcxbj.bike.bean.account.AuthNativeToken;
import com.wlcxbj.bike.bean.account.AuthToken;
import com.wlcxbj.bike.bean.account.SmsCodeBean;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.global.ShareBikeApplication;
import com.wlcxbj.bike.net.OkhttpHelper;
import com.wlcxbj.bike.net.beanutil.HttpAccountBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.net.beanutil.HttpPhoneBeanUtil;
import com.wlcxbj.bike.util.cache.CacheUtil;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.ToastUtil;

/**
 * Created by Administrator on 2016/11/10.
 */

public class RegisterActivity extends BaseActivity implements View.OnFocusChangeListener {
    public static final String WHERE_FROM = "wherefrom";
    public static String startfrom = null;
    public static final String SPLASH_START = "splash_start";
    public static final String MAP_START = "map_start";
    private static final String CONRACT_TEXT = "点击确定,即表示已经阅读并同意《用车服务条款》";
    private static final int startIndex = CONRACT_TEXT.indexOf("《");
    private static final int endIndex = CONRACT_TEXT.indexOf("》");
    public static final String TAG = "RegisterActivity";
    private static final String ZH_CODE = "86";
    private static final String regexStr = "^1[34578]\\d{9}$";
    private static final int COUNT_AFTER_GET_CHECKNUM = 11111;
    private static int COUNTER = 0;
    @Bind(R.id.et_phoneNumber)
    EditText etPhoneNumber;
    @Bind(R.id.et_checkNumber)
    EditText etCheckNumber;
    @Bind(R.id.get_checkNumber)
    Button getCheckNumber;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    private String mPhoneNum;
    private String username = "liujt";
    private String password = "Fly123456";
    private AuthNativeToken authNativeToken;
    private HttpAccountBeanUtil httpUserBeanUtil;
    private boolean getSmsCodeSuccess = false;
    //提交设备Id到server
    private HttpPhoneBeanUtil httpPhoneBeanUtil;
    private ShareBikeApplication application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getCheckNumber.setClickable(false);
        btnConfirm.setClickable(false);
        startfrom = getIntent().getStringExtra(RegisterActivity.WHERE_FROM);
        application = (ShareBikeApplication) getApplication();
        initSpan();
        initListener();
        OkhttpHelper okhttpHelper = OkhttpHelper.getInstance();
        httpUserBeanUtil = new HttpAccountBeanUtil(this);
        COUNTER = 60;
    }


    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private RegisterActivity thisActivity;

        public MyHandler(RegisterActivity thisActivity) {
            this.thisActivity = thisActivity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case COUNT_AFTER_GET_CHECKNUM:
                    if (COUNTER == 0) {
                        thisActivity.getCheckNumber.setClickable(true);
                        thisActivity.getCheckNumber.setBackgroundResource(R.color.green);
                        thisActivity.getCheckNumber.setText("重新获取");
                        thisActivity.mHandler.removeMessages(COUNT_AFTER_GET_CHECKNUM);
                    } else {
                        thisActivity.getCheckNumber.setText("重新获取(" + COUNTER-- + "s)");
                        thisActivity.mHandler.sendEmptyMessageDelayed(COUNT_AFTER_GET_CHECKNUM,
                                1000);
                    }
                    break;
            }
        }
    }

    ;

    /**
     * 获取验证码
     *
     * @param mobile
     */
    public void getCheckNumberFromServer(String mobile) {

        httpUserBeanUtil.getSmsCodeBean(mobile, new HttpCallbackHandler<SmsCodeBean>() {
            @Override
            public void onSuccess(SmsCodeBean bean) {
//                updateGetCheckNumberButton();
                setGetSmsCodeSuccess(true);
                LogUtil.e(TAG, "请求验证码成功:" + bean.toString());
            }

            @Override
            public void onFailure(Exception error, String msg) {
                LogUtil.e(TAG, "请求验证码失败");
                setGetSmsCodeSuccess(false);
                LogUtil.e(TAG, "MSG=" + msg);
                ToastUtil.showUIThread(RegisterActivity.this, msg);
            }
        });
    }

    private void updateGetCheckNumberButton() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getCheckNumber.setClickable(false);
                getCheckNumber.setBackgroundColor(Color.GRAY);
                COUNTER = 60;
                getCheckNumber.setText("重新获取(" + COUNTER-- + "s)");
                mHandler.sendEmptyMessageDelayed(COUNT_AFTER_GET_CHECKNUM, 1000);
            }
        });
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_register);
    }

    private void initListener() {
        etCheckNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && etPhoneNumber.getText().toString().matches(regexStr)) {
                    btnConfirm.setClickable(true);
                    btnConfirm.setBackgroundResource(R.drawable.recharge_button_bg_green);
                } else {
                    btnConfirm.setClickable(false);
                    btnConfirm.setBackgroundResource(R.drawable.recharge_button_bg_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtil.e(TAG, "COUNTER=" + COUNTER);
                if (!(COUNTER > 0 && COUNTER < 60)) {
                    if (s.toString().matches(regexStr)) {
                        getCheckNumber.setClickable(true);
                        getCheckNumber.setBackgroundResource(R.color.green);
                    } else {
                        getCheckNumber.setClickable(false);
                        getCheckNumber.setBackgroundResource(R.color.gray);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initSpan() {
        //设置span
        Parcel p = Parcel.obtain();
        p.writeString("SERIF");
        p.writeInt(Typeface.BOLD_ITALIC);
        p.writeInt(10);

        SpannableStringBuilder ssb = new SpannableStringBuilder(CONRACT_TEXT);
        ssb.setSpan(new TextAppearanceSpan(p), startIndex, endIndex, Spanned
                .SPAN_INCLUSIVE_INCLUSIVE);
        p.recycle();
    }


    @OnClick({R.id.ll_bar, R.id.ib_back, R.id.et_phoneNumber, R.id.et_checkNumber,
            R.id.get_checkNumber, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                onBackPressed();
                break;
            case R.id.get_checkNumber:
                mPhoneNum = etPhoneNumber.getText().toString();
                //获取验证码
                if (mPhoneNum.matches(regexStr)) {
                    Log.e(TAG, "请求验证码");
                    mHandler.removeCallbacksAndMessages(COUNT_AFTER_GET_CHECKNUM);
                    getCheckNumberFromServer(mPhoneNum);
                    updateGetCheckNumberButton();
                    etCheckNumber.requestFocus();
                } else {
                    ToastUtil.show(getApplicationContext(), getResources().getString(R.string
                            .tip_167));
                }
                break;
            case R.id.btn_confirm:
                Log.e(TAG, "提交验证码");
                String mobile = etPhoneNumber.getText().toString();;
                String psw = etCheckNumber.getText().toString();
                if (isGetSmsCodeSuccess()) {
                    loginCheck(mobile, psw);
                } else {
                    ToastUtil.showUIThread(this, getResources().getString(R.string.tip_168));
                }
                btnConfirm.setClickable(false);
                btnConfirm.setBackgroundResource(R.drawable.recharge_button_bg_gray);
                break;
        }
    }

    /**
     * 登陆服务器
     *
     * @param mobile
     * @param psw
     */
    private void loginCheck(String mobile, String psw) {
        if (TextUtils.isEmpty(mobile) || psw.isEmpty()) {
            ToastUtil.show(this, getResources().getString(R.string.tip_169));
            return;
        }
        LogUtil.e(TAG, "开始连接服务器");
        httpUserBeanUtil.getAuthToken(mobile, psw, new HttpCallbackHandler<AuthToken>() {
            @Override
            public void onSuccess(AuthToken authToken) {
                LogUtil.e(TAG, "获取authToken＝" + authToken);
                //本地缓存
                cacheAuthToken(authToken);
                getUserInfo();
                initCloudPush();
                submitDeviceId2Server();

            }

            @Override
            public void onFailure(Exception error, String msg) {
                ToastUtil.showUIThread(RegisterActivity.this, getResources().getString(R.string
                        .tip_170));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnConfirm.setClickable(false);
                        btnConfirm.setBackgroundResource(R.drawable.recharge_button_bg_gray);
                    }
                });
            }
        });

    }

    /**
     * 初始化阿里云推送
     */
    private void initCloudPush() {
        if (authNativeToken == null) return;
        application.getPushService().bindAccount(authNativeToken.getAuthToken().getUserId(), new
                CommonCallback() {

                    @Override
                    public void onSuccess(String s) {
                        LogUtil.e(TAG, "绑定帐户成功：" + s);
                    }

                    @Override
                    public void onFailed(String s, String s1) {
                        LogUtil.e(TAG, "绑定帐户失败：" + s + ", " + s1);
                    }
                });
    }

    /**
     * 缓存登陆信息
     */
    private void cacheAuthToken(AuthToken authToken) {
        //序列化到本地
        authNativeToken = new AuthNativeToken(authToken, System.currentTimeMillis());
        boolean b = CacheUtil.cacheSerialToken(this, Constants
                .AUTH_CACHE_FILE_NAME, authNativeToken);
        LogUtil.e(TAG, b ? "缓存成功" : "缓存失败");
    }

    /**
     * 监听edittext内手机号是否正确输入
     *
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText phoneNum = (EditText) v;
        String phoneNumber = phoneNum.getText().toString().trim();
        String checkNum = etCheckNumber.getText().toString().trim();
        switch (v.getId()) {
            case R.id.et_phoneNumber:
                LogUtil.e(TAG, "COUNTER=" + COUNTER);
                if (phoneNumber.matches(regexStr)) {
                    getCheckNumber.setClickable(true);
                    getCheckNumber.setBackgroundResource(R.color.green);
                } else {
                    getCheckNumber.setClickable(false);
                    getCheckNumber.setBackgroundResource(R.color.gray);
                }
                break;
            case R.id.et_checkNumber:
                if (TextUtils.isEmpty(checkNum) && phoneNumber.matches(regexStr)) {
                    btnConfirm.setClickable(true);
                    btnConfirm.setBackgroundResource(R.drawable.recharge_button_bg_green);
                } else {
                    btnConfirm.setClickable(false);
                    btnConfirm.setBackgroundResource(R.drawable.recharge_button_bg_gray);
                }
                break;
        }
    }

    private void getUserInfo() {
        LogUtil.e(TAG, "getUserInfo");
        LogUtil.e(TAG, "getUserInfo"+authNativeToken.getAuthToken().getAccess_token());
        httpUserBeanUtil.getUserInfos(authNativeToken.getAuthToken().getAccess_token(), new
                HttpCallbackHandler<AccountToken>() {
                    @Override
                    public void onSuccess(AccountToken accountToken) {
                        //缓存帐户信息
                        cacheAccountToken(accountToken);
                        //进入MapActivity
                        startThatActivity(accountToken);
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        LogUtil.e(TAG, "获取帐户信息失败:" + msg);
                    }
                });
    }

    /**
     * start map with auth and account info
     *
     * @param accountToken
     */
    private void startThatActivity(AccountToken accountToken) {
        LogUtil.e(TAG, "WHERE_FROM=" + startfrom);
        if (TextUtils.equals(startfrom, SPLASH_START)) {
            Intent intent = new Intent(RegisterActivity.this, MapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MapActivity.ACCOUNT_TOKEN, accountToken);
            bundle.putSerializable(MapActivity.AUTH_NATIVE_TOKEN, authNativeToken);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } else if (TextUtils.equals(startfrom, MAP_START)) {
            Intent intent = new Intent(RegisterActivity.this, MapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MapActivity.ACCOUNT_TOKEN, accountToken);
            bundle.putSerializable(MapActivity.AUTH_NATIVE_TOKEN, authNativeToken);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Intent intent = new Intent(RegisterActivity.this, MapActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MapActivity.ACCOUNT_TOKEN, accountToken);
            bundle.putSerializable(MapActivity.AUTH_NATIVE_TOKEN, authNativeToken);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
        COUNTER = 60;
    }

    /**
     * 缓存帐户信息
     *
     * @param accountToken
     */
    private void cacheAccountToken(AccountToken accountToken) {
        LogUtil.e(TAG, "缓存用户信息");
        String dir = getCacheDir().getPath() + File.separator + Constants
                .ACCOUNT_TOKEN_CACHE_FILE_NAME;
        File file = new File(dir);
        if (file.exists()) {
            file.delete();
        }
        CacheUtil.cacheSerialToken(RegisterActivity.this, Constants
                .ACCOUNT_TOKEN_CACHE_FILE_NAME, accountToken);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (TextUtils.equals(startfrom, MAP_START)) {
            //如果是从MAP启动的,那么就直接finish掉,并返回数据
            setResult(RESULT_CANCELED);
            finish();
        } else if (TextUtils.equals(startfrom, SPLASH_START)) {
            startActivity(new Intent(this, MapActivity.class));
            finish();
        }
    }

    public boolean isGetSmsCodeSuccess() {
        return getSmsCodeSuccess;
    }

    public void setGetSmsCodeSuccess(boolean getSmsCodeSuccess) {
        this.getSmsCodeSuccess = getSmsCodeSuccess;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 提交设备Id到服务器
     */
    public void submitDeviceId2Server() {
        httpPhoneBeanUtil = new HttpPhoneBeanUtil(this);
        LogUtil.d(TAG, "开始上传pushID");
      if(authNativeToken == null) return;
        httpPhoneBeanUtil.submitDeviceId2Server(authNativeToken.getAuthToken().getAccess_token(),application.getPushService().getDeviceId() ,new HttpCallbackHandler() {
            @Override
            public void onSuccess(Object o) {
                LogUtil.d(TAG, "上传pushDeviceId成功");
            }

            @Override
            public void onFailure(Exception error, String msg) {
                LogUtil.d(TAG, "上传pushDeviceId失败");
            }
        });
    }
}
