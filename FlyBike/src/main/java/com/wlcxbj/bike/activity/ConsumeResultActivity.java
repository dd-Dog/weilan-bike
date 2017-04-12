package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.bike.EndTripToken;
import com.wlcxbj.bike.util.DistanceUtil;
import com.wlcxbj.bike.util.StringUtil;
import com.wlcxbj.bike.util.TimeUtil;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ConsumeResultActivity extends BaseActivity {

    private static final String TAG = "ConsumeResultActivity";
    private EndTripToken endtriptoken;
    DecimalFormat df = new DecimalFormat("######0.00");
    private TextView payedResult_tv;
    private TextView rideTime_tv;
    private TextView rideDistance_tv;
    private TextView rideCalory_tv;
    private TextView walletBalance_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        addListener();
        setData();
    }

    public void initView() {
        payedResult_tv = (TextView) findViewById(R.id.tv_payResult);
        rideTime_tv = (TextView) findViewById(R.id.tv_rideTime);
        rideDistance_tv = (TextView) findViewById(R.id.tv_rideDistance);
        rideCalory_tv = (TextView) findViewById(R.id.tv_rideCalory);
        walletBalance_tv = (TextView) findViewById(R.id.tv_walletBalance);
    }

    public void addListener() {
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsumeResultActivity.this, BikeBrokenActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_consume_result);
    }

    private void setData() {
        Intent data = getIntent();
        endtriptoken = (EndTripToken) data.getSerializableExtra("endtriptoken");
        // 成功支付钱数
        String payedAmount = getString(R.string.payed_result, df.format(Double.parseDouble
                (endtriptoken.getAmount())));
        payedResult_tv.setText(StringUtil.getRiceText(this,payedAmount,4,payedAmount.indexOf(".") + 3,R.color.green_68,0));
        // 骑行时间
        rideTime_tv.setText(TimeUtil.getTimeShort(Long.parseLong(endtriptoken.getDurationTime())));
        //骑行距离
        long rideDistance = data.getLongExtra("rideDistance", 0); // 单位是米
        rideDistance_tv.setText(DistanceUtil.getDistance(Double.parseDouble(String.valueOf
                (rideDistance))));
        //骑行消耗卡路里
        rideCalory_tv.setText(rideDistance / 20 + "卡");
        //钱包余额
        walletBalance_tv.setText(endtriptoken.getBalance() + "元");
    }

}
