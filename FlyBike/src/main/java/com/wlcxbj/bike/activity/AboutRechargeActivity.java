package com.wlcxbj.bike.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.util.DpPxUtil;

/**
 * Created by bain on 17-1-20.
 */
public class AboutRechargeActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout tips;
    private TextView tvDes;
    private Object tipTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showData();
    }

    private void showData() {
        tvDes.setText(getResources().getString(R.string.charge_des));
        TextView tip1 = new TextView(this);
        tips.addView(getTipTextView(getResources().getString(R.string.tip_1)));
        tips.addView(getTipTextView(getResources().getString(R.string.tip_2)));
        tips.addView(getTipTextView(getResources().getString(R.string.tip_3)));
        tips.addView(getTipTextView(getResources().getString(R.string.tip_1)));
        tips.addView(getTipTextView(getResources().getString(R.string.tip_2)));
        tips.addView(getTipTextView(getResources().getString(R.string.tip_3)));
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_about_recharge);
        getSupportActionBar().hide();
        findViewById(R.id.ib_back).setOnClickListener(this);
        tvDes = (TextView) findViewById(R.id.charge_des);
        tips = (LinearLayout) findViewById(R.id.tips);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
        }
    }

    public TextView getTipTextView(String content) {
        TextView tipTextView = new TextView(this);
        tipTextView.setTextSize(14);
        tipTextView.setTextColor(getResources().getColor(R.color.user_text));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = (int) DpPxUtil.getPix(this, 16);
        layoutParams.rightMargin = (int) DpPxUtil.getPix(this, 16);
        layoutParams.bottomMargin = (int)DpPxUtil.getPix(this, 10);
        tipTextView.setLayoutParams(layoutParams);
        tipTextView.setText(content);
        return tipTextView;
    }
}
