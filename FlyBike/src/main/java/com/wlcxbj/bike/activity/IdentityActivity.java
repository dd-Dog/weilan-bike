package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.account.AccountToken;
import com.wlcxbj.bike.bean.account.AuthNativeToken;
import com.wlcxbj.bike.bean.account.IdentityToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.net.beanutil.HttpAccountBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.util.ToastUtil;
import com.wlcxbj.bike.util.cache.CacheUtil;
import com.wlcxbj.bike.util.LogUtil;

/**
 * Created by bain on 17-1-20.
 */
public class IdentityActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "IdentityActivity";
    private Button btnConfirm;
    private EditText etFullName;
    private EditText etIdentifyCode;
    private boolean hasFullName = false;
    private boolean hasIdenCode = false;
//    private TextView checkInput;
    private AuthNativeToken mAuthNativeToken;
    private AccountToken mAccountToken;
    private HttpAccountBeanUtil httpUserBeanUtil;
    private FrameLayout flIdentify;
    private View noIdnetify;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpUserBeanUtil = new HttpAccountBeanUtil(this);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_identify);
        getSupportActionBar().hide();

        initData();
        if (!TextUtils.isEmpty(mAccountToken.getRealInfo().getIdno()) && !TextUtils.isEmpty
                (mAccountToken.getRealInfo().getRealName())) {
            //如果提交过实名认证,后台有记录
            if (mAccountToken.getRealInfo().getVerifySpid() == 1) {
                //正在处理中
                initUnderHandling();
            } else if (mAccountToken.getRealInfo().getVerifySpid() == 2) {

            }
        } else {
            initNotIdentityView();
        }
    }

    private void initData() {
        mAuthNativeToken = CacheUtil.getSerialToken(getApplicationContext(), Constants
                .AUTH_CACHE_FILE_NAME);
        mAccountToken = CacheUtil.getSerialToken(getApplicationContext(), Constants
                .ACCOUNT_TOKEN_CACHE_FILE_NAME);
        LogUtil.e(TAG, "mAuthNativeToken=" + mAuthNativeToken + ", mAccountToken=" + mAccountToken);
    }

    private void initUnderHandling() {
        flIdentify = (FrameLayout) findViewById(R.id.fl_identify);
        View content = View.inflate(this, R.layout.layout_under_identify, null);
        flIdentify.addView(content);
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showUnderHandling() {
        if (flIdentify == null) return;
        flIdentify.removeView(noIdnetify);
        initUnderHandling();
    }

    private void initNotIdentityView() {
        flIdentify = (FrameLayout) findViewById(R.id.fl_identify);
        noIdnetify = View.inflate(this, R.layout.layout_not_identify, null);
        flIdentify.addView(noIdnetify);
        findViewById(R.id.ib_back).setOnClickListener(this);
        btnConfirm = (Button) findViewById(R.id.btn_submmit);
        etFullName = (EditText) findViewById(R.id.et_fullname);
        etIdentifyCode = (EditText) findViewById(R.id.et_identity_code);
//        checkInput = (TextView) findViewById(R.id.tv_inputcheck);
//        checkInput.setVisibility(View.INVISIBLE);
        etIdentifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validate(s.toString())) {
                    hasIdenCode = true;
                } else {
                    hasIdenCode = false;
                }
                toggleButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    hasFullName = true;
                } else {
                    hasFullName = false;
                }
                toggleButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnConfirm.setOnClickListener(this);
        findViewById(R.id.aboard_identify).setOnClickListener(this);
        toggleButton();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.aboard_identify:
                startActivity(new Intent(this, AboardIdentifyActivity.class));
                break;
            case R.id.btn_submmit:
                LogUtil.e(TAG, "commit");
                String fullName = etFullName.getText().toString().trim();
                String identityCode = etIdentifyCode.getText().toString().trim();
                requestIdentify(fullName, identityCode);
                break;
        }
    }

    /**
     * 请求实名认证
     */
    private void requestIdentify(String fullname, String identitycode) {
        LogUtil.e(TAG, "向服务器请求实名认证");
        if (TextUtils.isEmpty(fullname) || TextUtils.isEmpty(identitycode)) {
            return;
        }
        httpUserBeanUtil.commitIdentity(mAuthNativeToken.getAuthToken().getAccess_token(),
                identitycode, fullname, new HttpCallbackHandler<IdentityToken>() {
                    @Override
                    public void onSuccess(IdentityToken identityToken) {
                        LogUtil.e(TAG, "获取实名认证信息:" + identityToken);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                ToastUtil.showUIThread(IdentityActivity.this, getResources().getString(R.string.tip_134));
                                CacheUtil.clearCache(getApplicationContext(), Constants
                                        .ACCOUNT_TOKEN_CACHE_FILE_NAME);

                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        LogUtil.e(TAG, "获取实名认证信息失败:" + msg);
                    }
                });

    }

    public boolean validate(String no) {
        //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        //通过Pattern获得Matcher
        Matcher idNumMatcher = idNumPattern.matcher(no);
        return idNumMatcher.matches();
    }

    private void toggleButton() {
        if (hasFullName && hasIdenCode) {
            btnConfirm.setClickable(true);
            btnConfirm.setBackgroundResource(R.drawable.wallet_selector);
        } else {
            btnConfirm.setClickable(false);
            btnConfirm.setBackgroundResource(R.drawable.recharge_button_bg_gray);
        }
    }
}
