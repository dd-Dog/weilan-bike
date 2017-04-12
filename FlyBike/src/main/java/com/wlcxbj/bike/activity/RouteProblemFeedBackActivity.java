package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.RouteBean;
import com.wlcxbj.bike.util.TimeUtil;

/**
 * Created by bain on 16-12-1.
 */
public class RouteProblemFeedBackActivity extends BaseActivity {

    @Bind(R.id.ll_bar)
    LinearLayout llBar;
    @Bind(R.id.tv_bikeNum)
    TextView tvBikeNum;
    @Bind(R.id.tv_startTime)
    TextView tvStartTime;
    @Bind(R.id.tv_endTime)
    TextView tvEndTime;
    @Bind(R.id.rl_broken)
    RelativeLayout rlBroken;
    @Bind(R.id.rl_others)
    RelativeLayout rlOthers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        RouteBean bean = (RouteBean) bundle.get("item_feedback");
        tvBikeNum.setText(bean.bikeID);
        tvStartTime.setText(TimeUtil.getTimeStr(bean.startTime));
        tvEndTime.setText(TimeUtil.getTimeStr(bean.endTime));
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_route_problems_feedback);
    }

    @OnClick({R.id.rl_broken, R.id.rl_others, R.id.ib_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_broken:
                startActivity(new Intent(this, BikeBrokenActivity.class));
                break;
            case R.id.rl_others:
                startActivity(new Intent(this, OtherProblemsActivity.class));
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }
}
