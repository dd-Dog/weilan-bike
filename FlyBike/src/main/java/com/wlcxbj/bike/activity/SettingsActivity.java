package com.wlcxbj.bike.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.util.DialogUtil;
import com.wlcxbj.bike.util.PreferenceUtil;
import com.wlcxbj.bike.util.cache.CacheUtil;

import android.content.pm.IPackageStatsObserver;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by bain on 16-11-28.
 */
public class SettingsActivity extends BaseActivity {
    @Bind(R.id.rl_clear_cache)
    RelativeLayout rl_clear_cache;
    @Bind(R.id.rl_deposit)
    RelativeLayout rl_deposit;
    @Bind(R.id.rl_recharge_rule)
    RelativeLayout rlRechargeRule;
    @Bind(R.id.btn_logout)
    Button btn_logout;
    @Bind(R.id.ib_back)
    FrameLayout ib_back;
    @Bind(R.id.cache_size)
    TextView cacheSize;
    @Bind(R.id.rl_ble)
    RelativeLayout rlBle;
    private PackageManager mPm;
    private static final String TAG = "SettingsActivity";
    private CheckBox cbBle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getCacheSize();
        Boolean bleEnabled = PreferenceUtil.getBoolean(this, Constants.UNBLOCK_BLE_ENABLED, false);
        cbBle.setChecked(bleEnabled);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_settings);
        cbBle = (CheckBox) findViewById(R.id.rb_ble);
        cbBle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PreferenceUtil.putBoolean(SettingsActivity.this, Constants.UNBLOCK_BLE_ENABLED, true);
                }else {
                    PreferenceUtil.putBoolean(SettingsActivity.this, Constants.UNBLOCK_BLE_ENABLED, false);
                }
            }
        });
    }

    @OnClick({R.id.ib_back, R.id.rl_clear_cache, R.id.rl_consumer_rule, R.id.rl_deposit, R.id
            .rl_recharge_rule, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.rl_clear_cache:
                showClearCacheDialog();
                break;
            case R.id.rl_consumer_rule:
                startActivity(new Intent(this, ConsumerRuleActivity.class));
                break;
            case R.id.rl_deposit:
                startActivity(new Intent(this, DepositActivity.class));
                break;
            case R.id.rl_recharge_rule:
                startActivity(new Intent(this, RechargeRuleActivity.class));
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void showClearCacheDialog() {
    DialogUtil.showDoubleButtonDialog(this,"确定清理本地缓存记录",new DialogUtil.DoubleButtonListener(){
           @Override
           public void onLeftBtnClick() {

           }

           @Override
           public void onRightBtnClick() {
               cleanCache();
           }
       });
    }

    /**
     * 清除缓存
     */
    private void cleanCache() {
        if (mPm == null) return;
        try {
            Method freeStorageAndNotify = mPm.getClass().getMethod("freeStorageAndNotify", long
                    .class, IPackageDataObserver.class);
            freeStorageAndNotify.invoke(mPm, getEnvironmentSize() - 1L, mDataObserver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        getCacheSize();
    }

    private static long getEnvironmentSize() {
        File localFile = Environment.getDataDirectory();
        long l1;
        if (localFile == null)
            l1 = 0L;
        while (true) {

            String str = localFile.getPath();
            StatFs localStatFs = new StatFs(str);
            long l2 = localStatFs.getBlockSize();
            l1 = localStatFs.getBlockCount() * l2;
            return l1;
        }
    }

    private void logout() {
//        PreferenceUtil.remove(this, Constants.SAVED_USERNAME);
//        PreferenceUtil.remove(this, Constants.SAVED_PASSWD);
//        PreferenceUtil.remove(this, Constants.SAVEPWD_ENABLED);
//        ShareBikeApplication.getInstance().exit();
        CacheUtil.clearCache(this, Constants.ACCOUNT_TOKEN_CACHE_FILE_NAME);
        CacheUtil.clearCache(this, Constants.AUTH_CACHE_FILE_NAME);
        finish();
    }

    /**
     * 通过反射获取缓存大小
     */
    public void getCacheSize() {
        mPm = getPackageManager();
        try {
            Method getPackageSizeInfo = mPm.getClass().getMethod("getPackageSizeInfo", String
                    .class, IPackageStatsObserver
                    .class);
            getPackageSizeInfo.invoke(mPm, getPackageName(), mStatsObserver);
            Log.e(TAG, "获取缓存大小");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    IPackageDataObserver.Stub mDataObserver = new IPackageDataObserver.Stub() {
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws
                RemoteException {
            Log.e(TAG, "packageName=" + packageName + ", succeeded=" + succeeded);

        }
    };

    /**
     * 缓存获取监听器
     */
    IPackageStatsObserver.Stub mStatsObserver = new IPackageStatsObserver.Stub() {

        @Override
        public void onGetStatsCompleted(final PackageStats pStats, boolean succeeded) throws
                RemoteException {
            Log.e(TAG, "缓存大小：" + pStats.cacheSize);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cacheSize.setText(getResources().getString(R.string.cache_size) + Formatter.formatFileSize(getApplicationContext(),
                            pStats.cacheSize));
                    cacheSize.invalidate();
                }
            });

        }
    };
}
