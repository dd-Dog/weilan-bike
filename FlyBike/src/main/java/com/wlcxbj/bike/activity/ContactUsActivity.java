package com.wlcxbj.bike.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wlcxbj.bike.R;

/**
 * Created by bain on 16-11-30.
 */
public class ContactUsActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_contact_us);
    }
}
