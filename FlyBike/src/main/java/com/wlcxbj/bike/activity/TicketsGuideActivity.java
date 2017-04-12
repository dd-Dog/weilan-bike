package com.wlcxbj.bike.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wlcxbj.bike.R;

/**
 * Created by Administrator on 2017/2/22.
 */
public class TicketsGuideActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayout lLContent = (LinearLayout) findViewById(R.id.ll_content);
        String[] titles = getResources().getStringArray(R.array.tickets_guide_title);
        String[] contents = getResources().getStringArray(R.array.tickets_guide_content);
        for (int i = 0; i < titles.length; i++) {
            lLContent.addView(getItemView(titles[i], contents[i]));
        }
    }

    private View getItemView(String key, String value) {
        View view = View.inflate(this, R.layout.item_tickets_guide, null);
        TextView content = (TextView) view.findViewById(R.id.tv_content);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(key);
        content.setText(value);
        return view;
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_tickets_guide);
    }
}
