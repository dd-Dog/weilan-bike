package com.wlcxbj.bike.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.wlcxbj.bike.R;

/**
 * Created by bain on 17-1-19.
 */
public class FundBackActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_fund_back);
        getSupportActionBar().hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.btn_confirm:

                break;
        }
    }
}
