package com.wlcxbj.bike.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wlcxbj.bike.R;

/**
 * Created by Administrator on 2017/2/20.
 */
public class UserGuidDetailActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        TextView content = (TextView) findViewById(R.id.tv_content);
        int childPos = getIntent().getIntExtra(UserGuideActivity.CHILD_POS, -1);
        int parentPos = getIntent().getIntExtra(UserGuideActivity.PARENT_POS, -1);
        int index = getIntent().getIntExtra(UserGuideActivity.INDEX, -1);
        String titleStr =  getIntent().getStringExtra(UserGuideActivity.TITLE);
        if (childPos != -1 && parentPos != -1) {
            String[] stringArray = getResources().getStringArray(R.array.user_guide_detail);
            title.setText(titleStr);
            content.setText(stringArray[index]);
        }
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_userguide_detail);
        getSupportActionBar().hide();
    }
}
