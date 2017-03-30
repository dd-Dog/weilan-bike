package com.wlcxbj.bike.activity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.lang.reflect.Field;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.account.AccountToken;
import com.wlcxbj.bike.bean.account.AuthNativeToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.global.ShareBikeApplication;
import com.wlcxbj.bike.net.beanutil.HttpAccountBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.ToastUtil;
import com.wlcxbj.bike.util.cache.CacheUtil;


/**
 * Created by Administrator on 2016/11/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected AuthNativeToken mAuthNativeToken;
    protected AccountToken mAccountToken;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareBikeApplication.getInstance().push(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initToken();
        setContentViewID();
        initState();
//        AndroidBug54971Workaround.assistActivity(findViewById(android.R.id.content));
    }

    /**
     * 获取传递来的token对象
     * 使用三级缓存机制,如果内存对象为空就从文件缓存取,如果文件缓存为空,就从网络获取
     */
    private void initToken() {
        mAuthNativeToken = (AuthNativeToken) getIntent().getSerializableExtra
                (MapActivity.AUTH_NATIVE_TOKEN);
        mAccountToken = (AccountToken) getIntent().getSerializableExtra
                (MapActivity.ACCOUNT_TOKEN);
        if (mAuthNativeToken == null) {
            LogUtil.e(getClass().getSimpleName(), "mAuthNativeToken内存缓存为空");
            //从本地缓存获取
            mAuthNativeToken = CacheUtil.getSerialToken(this, Constants.AUTH_CACHE_FILE_NAME);
            if (mAuthNativeToken == null) {
                LogUtil.e(getClass().getSimpleName(), "mAuthNativeToken从文件缓存获取失败");
//                ToastUtil.showUIThread(this, getResources().getString(R.string.txt_not_login));
            }else {
                LogUtil.e(getClass().getSimpleName(), "mAuthNativeToken从文件缓存获取成功");
            }
        }
        if (mAccountToken == null) {
            LogUtil.e(getClass().getSimpleName(), "mAccountToken内存缓存为空");
            if (mAuthNativeToken == null) return;
            //从本地缓存获取
            mAccountToken = CacheUtil.getSerialToken(this, Constants.ACCOUNT_TOKEN_CACHE_FILE_NAME);
            if (mAccountToken == null) {
                LogUtil.e(getClass().getSimpleName(), "mAccountToken从文件缓存获取失败");
                //从服务器获取
                HttpAccountBeanUtil httpAccountBeanUtil = new HttpAccountBeanUtil(this);
                httpAccountBeanUtil.getUserInfos(mAuthNativeToken.getAuthToken().getAccess_token(), new HttpCallbackHandler<AccountToken>() {
                    @Override
                    public void onSuccess(AccountToken accountToken) {
                        mAccountToken = accountToken;
                        CacheUtil.cacheSerialToken(BaseActivity.this, Constants.ACCOUNT_TOKEN_CACHE_FILE_NAME, accountToken);
                        LogUtil.e(getClass().getSimpleName(), "mAccountToken从服务器获取成功");
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        LogUtil.e(getClass().getSimpleName(), "mAccountToken从服务器获取失败");
                    }
                });
            }else {
                LogUtil.e(getClass().getSimpleName(), "mAccountToken从文件缓存获取成功");
            }
        }
    }

    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     */
    private void initState() {
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏，这里不能设置透明导航栏，因为会导致如下问题
            //1.华为等有虚拟按键的导航栏，会变为透明，导致view和导航栏重合
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //
            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
            linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = getStatusBarHeight();
            Log.e("BaseActivity", "statusHeight=" + statusHeight);
            //动态的设置隐藏布局的高度
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear_bar
                    .getLayoutParams();
            params.height = statusHeight;
            linear_bar.setLayoutParams(params);
        }
    }

    /**
     * 通过反射的方式获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public abstract void setContentViewID();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareBikeApplication.getInstance().pop(this);
    }
}
