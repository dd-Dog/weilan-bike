package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wlcxbj.bike.R;

/**
 * Created by Administrator on 2017/2/18.
 */
public class CreditPointsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mWave1;
    private static ImageView mWave2;
    private static ImageView mWave3;

    private AnimationSet mAnimationSet1;
    private static AnimationSet mAnimationSet2;
    private static AnimationSet mAnimationSet3;

    private static final int OFFSET = 1000;  //每个动画的播放时间间隔
    private static final int MSG_WAVE2_ANIMATION = 2;
    private static final int MSG_WAVE3_ANIMATION = 3;

    private MyHandler mHandler = new MyHandler();
    private static class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_WAVE2_ANIMATION:
                    mWave2.startAnimation(mAnimationSet2);
                    break;
                case MSG_WAVE3_ANIMATION:
                    mWave3.startAnimation(mAnimationSet3);
                    break;
            }
        }
    };
    private TextView pointsNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        showWaveAnimation();
        initData();
    }

    private void initData() {
        pointsNumber.setText(mAccountToken.getAccount().getPoint());
    }

    private void initView() {
        ImageView mNormal = (ImageView) findViewById(R.id.normal);
        mWave1 = (ImageView) findViewById(R.id.wave1);
        mWave2 = (ImageView) findViewById(R.id.wave2);
        mWave3 = (ImageView) findViewById(R.id.wave3);
        pointsNumber = (TextView) findViewById(R.id.tv_points_number);

        findViewById(R.id.tv_right).setOnClickListener(this);
        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.ll_history).setOnClickListener(this);

        mAnimationSet1 = initAnimationSet();
        mAnimationSet2 = initAnimationSet();
        mAnimationSet3 = initAnimationSet();
    }

    private AnimationSet initAnimationSet() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(0.9f, 2.5f, 0.9f, 2.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(OFFSET * 3);
        sa.setRepeatCount(Animation.INFINITE);// 设置循环
        AlphaAnimation aa = new AlphaAnimation(0.2f, 0.8f);
        aa.setDuration(OFFSET * 3);
        aa.setRepeatCount(Animation.INFINITE);//设置循环
        as.addAnimation(sa);
        as.addAnimation(aa);
        return as;
    }

    private void showWaveAnimation() {
        mWave1.startAnimation(mAnimationSet1);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE2_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE3_ANIMATION, OFFSET * 2);
    }

    private void clearWaveAnimation() {
        mWave1.clearAnimation();
        mWave2.clearAnimation();
        mWave3.clearAnimation();
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_credit_points);
        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_right:
                startActivity(new Intent(this, CreditPointsRuleActivity.class));
                break;
            case R.id.ll_history:
                startActivity(new Intent(this, CreditHistoryActivity.class));
                break;
        }
    }
}
