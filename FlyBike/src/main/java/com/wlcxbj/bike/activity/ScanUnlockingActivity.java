package com.wlcxbj.bike.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wlcxbj.bike.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by itsdon on 17/4/13.
 */

public class ScanUnlockingActivity extends BaseActivity{

    @Bind(R.id.ll_webviewContainer)
    LinearLayout webviewContainer_ll;
    @Bind(R.id.tv_bikeNum_unlocking)
    TextView bikeNum_tv;
    @Bind(R.id.pb_unlocking)
    ProgressBar unlocking_pb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }

    public void initView(){
        WebView webView = new WebView(this);
        webView.loadUrl("file:///android_asset/version.gif");
        webviewContainer_ll.addView(webView);

    }


    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_scan_unlocking);
    }
}
