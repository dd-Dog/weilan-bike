package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.amap.api.services.route.WalkPath;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.util.map.AmapUtil;
import com.wlcxbj.bike.adapter.WalkSegmentListAdapter;

public class WalkRouteDetailActivity extends BaseActivity implements View.OnClickListener {
	private WalkPath mWalkPath;
	private TextView mTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.ib_back).setOnClickListener(this);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getIntentData();
		TextView mTitleWalkRoute = (TextView) findViewById(R.id.firstline);
		String dur = AmapUtil.getFriendlyTime((int) mWalkPath.getDuration());
		String dis = AmapUtil
				.getFriendlyLength((int) mWalkPath.getDistance());
		mTitleWalkRoute.setText(dur + "(" + dis + ")");
		ListView mWalkSegmentList = (ListView) findViewById(R.id.bus_segment_list);
		WalkSegmentListAdapter mWalkSegmentListAdapter = new WalkSegmentListAdapter(
				this.getApplicationContext(), mWalkPath.getSteps());
		mWalkSegmentList.setAdapter(mWalkSegmentListAdapter);

	}

	@Override
	public void setContentViewID() {
		setContentView(R.layout.activity_route_detail);
	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		mWalkPath = intent.getParcelableExtra("walk_path");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ib_back:
				finish();
				break;
		}
	}
}
