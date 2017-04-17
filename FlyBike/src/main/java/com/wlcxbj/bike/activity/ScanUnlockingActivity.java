package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.event.FinishUnlockingActivityEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    private static final int MAX_BLUETOOTH_PB = 500;
    private static final long DELAY_BT_PB = 20;
    public static final int LENGTH_BT_PB = 1;
    private static final int MSG_UNLOCK_BLUETOOTH = 3333;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_UNLOCK_BLUETOOTH:
                    unlocking_pb.setProgress(unlocking_pb.getProgress() +
                            LENGTH_BT_PB);
                    sendEmptyMessageDelayed(MSG_UNLOCK_BLUETOOTH, DELAY_BT_PB);
                    if (unlocking_pb.getProgress() == MAX_BLUETOOTH_PB) {
                        handler.removeMessages(MSG_UNLOCK_BLUETOOTH);
                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        initUnlockProgressBar();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FinishUnlockingActivityEvent event){
        handler.removeMessages(MSG_UNLOCK_BLUETOOTH);
        finish();
    }


    public void initView(){
        Intent intent = getIntent();
        String bikeId = intent.getStringExtra("bikeId");
        if(!TextUtils.isEmpty(bikeId)){
            bikeNum_tv.setText("车辆ID: "+ bikeId);
        }
        WebView webView = new WebView(this);
        webView.loadUrl("file:///android_asset/version.gif");
        webviewContainer_ll.addView(webView);
    }

    public void initUnlockProgressBar(){
        unlocking_pb.setMax(MAX_BLUETOOTH_PB);
        unlocking_pb.setProgress(0);
        handler.sendEmptyMessageDelayed(MSG_UNLOCK_BLUETOOTH,DELAY_BT_PB);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_scan_unlocking);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
