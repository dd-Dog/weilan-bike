package com.wlcxbj.bike.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wlcxbj.bike.R;

/**
 * Created by bain on 16-11-30.
 */
public class UpdateVersionActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.ib_back).setOnClickListener(this);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        getVersion();
    }

    private void getVersion() {
        String version = null;
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvVersion.setText(getResources().getString(R.string.tip_178) + version);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_check_update);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;

        }
    }
}
