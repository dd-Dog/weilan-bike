package com.wlcxbj.bike.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.adapter.GuideAdapter;


/**
 * Created by bain on 16-11-28.
 */
public class UserGuideActivity extends BaseActivity {

    public static final String TITLE = "title";
    public static final String PARENT_POS = "parent_pos";
    public static final String CHILD_POS = "child_pos";
    public static final String INDEX = "index";
    private ExpandableListView elv;
    private int selectPos = -1;
    private ArrayList<String[]> childGroups;
    private String[] parent;
    private int[] childResIds = {
            R.array.user_guide_child1,
            R.array.user_guide_child2,
            R.array.user_guide_child3,
            R.array.user_guide_child4,
            R.array.user_guide_child5,
            R.array.user_guide_child6,
            R.array.user_guide_child5,
            R.array.user_guide_child7};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        initData();
    }

    private void initData() {
        parent = getResources().getStringArray(R.array.user_guide_parent);
        childGroups = new ArrayList<>();
        for (int i = 0; i < parent.length; i++) {
            childGroups.add(getResources().getStringArray(childResIds[i]));
        }
        GuideAdapter guideAdapter = new GuideAdapter(this, parent, childGroups);
        elv.setAdapter(guideAdapter);

    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_guide);
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        elv = (ExpandableListView) findViewById(R.id.elv_problems);
        elv.setGroupIndicator(null);
        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                                        long id) {
                //判断是否展开
                if (elv.isGroupExpanded(groupPosition)) {
                    //若已经展开则关闭
                    elv.collapseGroup(groupPosition);
                } else {
                    //展开之前，关闭上一次展开的组
                    elv.collapseGroup(selectPos);
                    elv.expandGroup(groupPosition);
                    selectPos = groupPosition;
                }
                return true;
            }
        });

        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int
                    childPosition, long id) {
                Intent intent = new Intent(UserGuideActivity.this, UserGuidDetailActivity.class);
                int index = 0;
                for (int i = 0; i < groupPosition; i++) {
                    index += childGroups.get(i).length;
                }
                index += childPosition;
                intent.putExtra(INDEX, index);
                intent.putExtra(PARENT_POS, groupPosition);
                intent.putExtra(CHILD_POS, childPosition);
                intent.putExtra(TITLE, childGroups.get(groupPosition)[childPosition]);
                startActivity(intent);
                return false;
            }
        });

        // "联系客服"点击事件
        findViewById(R.id.btn_contactCustomerService).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + "010-60608742"));
                if (ActivityCompat.checkSelfPermission(UserGuideActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    UserGuideActivity.this.startActivity(callIntent);
                }
            }
        });
    }

}
