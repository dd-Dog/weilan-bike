package com.wlcxbj.bike.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.global.Constants;

/**
 * Created by bain on 16-11-30.
 */
public class AboutUsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_about_us);
        //app信息
        ((TextView) findViewById(R.id.version)).setText(Constants.getVersionName(this));
        ((TextView) findViewById(R.id.app_name)).setText(getResources().getString(R.string.app_name));
        ((TextView) findViewById(R.id.company)).setText("微蓝畅享北京科技有限公司");
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
