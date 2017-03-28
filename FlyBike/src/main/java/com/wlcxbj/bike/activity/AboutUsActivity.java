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
public class AboutUsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_about_us);
        TextView version = (TextView) findViewById(R.id.version);
        TextView appName = (TextView) findViewById(R.id.app_name);
        TextView company = (TextView) findViewById(R.id.company);
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取版本号
        version.setText(getVersion());
        appName.setText(getResources().getString(R.string.app_name));
        company.setText("微蓝畅享北京科技有限公司");

    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int version = info.versionCode;
            return this.getString(R.string.version_name) + version + ".0";
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }
}
