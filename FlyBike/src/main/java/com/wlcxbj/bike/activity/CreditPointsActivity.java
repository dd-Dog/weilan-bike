package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        ((TextView)findViewById(R.id.tv_scoreValue)).setText(mAccountToken.getAccount().getPoint());
    }

    private void initView() {
        findViewById(R.id.tv_right).setOnClickListener(this);
        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.tv_positiveRecore).setOnClickListener(this);
        findViewById(R.id.tv_negativeRecore).setOnClickListener(this);

    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_credit_points);
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
            case R.id.tv_positiveRecore:
                //TODO
                break;
            case R.id.tv_negativeRecore:
                //TODO
                break;
//            case R.id.ll_history:
//                startActivity(new Intent(this, CreditHistoryActivity.class));
//                break;
        }
    }
}
