package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DecimalFormat;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.bike.EndTripToken;
import com.wlcxbj.bike.util.TimeUtil;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ConsumeResultActivity extends BaseActivity {

    private static final String TAG = "ConsumeResultActivity";
    private TextView tvSimNum;
    private TextView tvPayed;
    private TextView tvTime;
    DecimalFormat df = new DecimalFormat("######0.00");
    private TextView tvStartTime;
    private TextView tvEndtime;
    private EndTripToken endtriptoken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        tvSimNum = (TextView) findViewById(R.id.simnum);
        tvPayed = (TextView) findViewById(R.id.payed);
        tvTime = (TextView) findViewById(R.id.consume_time);
        tvStartTime = (TextView) findViewById(R.id.start_time);
        tvEndtime = (TextView) findViewById(R.id.end_time);
        tvPayed = (TextView) findViewById(R.id.payed);
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setData();
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_consume_result);
    }

    private void setData() {
        Intent data = getIntent();
        endtriptoken = (EndTripToken) data.getSerializableExtra("endtriptoken");
        String simno = endtriptoken.getTid();
        long usingtime = Long.parseLong(endtriptoken.getDurationTime());
        long endtime = Long.parseLong(endtriptoken.getLockedTime());
        tvEndtime.setText("结束时间：" + TimeUtil.getTimeStr(endtime));
        tvSimNum.setText("车辆编号: " + simno);
        tvPayed.setText(endtriptoken.getAmount());
        tvTime.setText("使用时间: " + TimeUtil.getTimeShort(usingtime));
    }
}
