package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.account.AccountToken;
import com.wlcxbj.bike.bean.account.BasicUserInfoToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.net.beanutil.HttpAccountBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.util.cache.CacheUtil;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.PreferenceUtil;
import com.wlcxbj.bike.util.ToastUtil;

/**
 * Created by bain on 16-11-30.
 */
public class ModifyNickNameActivity extends BaseActivity {

    public static final String NEW_NICKNAME = "new_nickname";
    @Bind(R.id.ll_bar)
    LinearLayout llBar;
    @Bind(R.id.et_newName)
    EditText etNewName;
    private HttpAccountBeanUtil httpUserBeanUtil;
    private static final String TAG = "ModifyNickNameActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        if (mAuthNativeToken == null) {
            LogUtil.e(TAG, "mAuthNativeToken == null");
            return;
        }
        if (mAccountToken == null) {
            LogUtil.e(TAG, "mAccountToken == null");
            return;
        }
        httpUserBeanUtil = new HttpAccountBeanUtil(this);
        etNewName.setText(mAccountToken.getBasicInfo().getNickName());
        etNewName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() != R.id.et_newName) return false;
                Drawable[] drawables = etNewName.getCompoundDrawables();
                Drawable drawableRight = drawables[2];
                if (event.getX() > (etNewName.getWidth() - drawableRight.getIntrinsicWidth() - 20)) {
                    etNewName.setText("");
                }
                return false;
            }
        });
        LogUtil.e(TAG, "mAccountToken=" + mAccountToken);
        String nickName = getIntent().getStringExtra("nickname");
        etNewName.setText(nickName + "");
    }


    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_nickname);
    }

    @OnClick({R.id.et_newName, R.id.ib_back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_right:
                if (TextUtils.isEmpty(etNewName.getText().toString())) {
                    ToastUtil.show(this, getResources().getString(R.string.tip_158));
                    return;
                }
                String nickName = etNewName.getText().toString();
                //调用对象的方法前一定要判断是否是空的
                if (mAccountToken != null) {
                    LogUtil.e(TAG, "mAccountToken=" + mAccountToken);
                    if (mAccountToken.getBasicInfo() != null) {
                        if (TextUtils.equals(nickName, mAccountToken.getBasicInfo().getNickName()
                        )) {
                            return;
                        }
                    }
                }
                PreferenceUtil.putString(this, Constants.USER_NICK_NAME, nickName);
                httpUserBeanUtil.updateUserInfo(mAuthNativeToken.getAuthToken().getAccess_token()
                        , nickName, mAccountToken.getBasicInfo().getAvatarUrl(), new
                                HttpCallbackHandler<BasicUserInfoToken>() {
                                    @Override
                                    public void onSuccess(BasicUserInfoToken basicUserInfoToken) {
                                        //更新内存缓存和本地缓存
                                        mAccountToken.setBasicInfo(basicUserInfoToken
                                                .getBasicInfo());
                                        updateAccountToken(mAccountToken);

                                    }

                                    @Override
                                    public void onFailure(Exception error, String msg) {
                                        ToastUtil.showUIThread(ModifyNickNameActivity.this,
                                                getResources().getString(R.string.tip_159));
                                    }
                                });
                setResult(RESULT_OK, new Intent().putExtra(NEW_NICKNAME, nickName));
                finish();
                break;
        }
    }

    /**
     * 缓更新帐户本地缓存信息
     *
     * @param accountToken
     */
    private void updateAccountToken(AccountToken accountToken) {
        LogUtil.e(TAG, "缓存用户信息");
        String dir = getCacheDir().getPath() + File.separator + Constants
                .ACCOUNT_TOKEN_CACHE_FILE_NAME;
        File file = new File(dir);
        if (file.exists()) {
            file.delete();
        }
        boolean b = CacheUtil.cacheSerialToken(ModifyNickNameActivity.this, Constants
                .ACCOUNT_TOKEN_CACHE_FILE_NAME, accountToken);
        LogUtil.e(TAG, "更新用户信息成功:" + accountToken.toString());
    }
}
