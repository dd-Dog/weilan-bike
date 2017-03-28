package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import com.wlcxbj.bike.R;


/**
 * Created by bain on 16-11-28.
 */
public class UserGuideActivity extends BaseActivity {

    public static final String TITLE = "title";
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
        GuideAdapter guideAdapter = new GuideAdapter();
        elv.setAdapter(guideAdapter);
    }

    private void initData() {
        parent = getResources().getStringArray(R.array.user_guide_parent);
        childGroups = new ArrayList<>();
        for (int i = 0; i < parent.length; i++) {
            childGroups.add(getResources().getStringArray(childResIds[i]));
        }
    }

    class GuideAdapter extends BaseExpandableListAdapter {

        //  获得某个父项的某个子项
        @Override
        public Object getChild(int parentPos, int childPos) {
            return childGroups.get(parentPos)[childPos];
        }

        //  获得父项的数量
        @Override
        public int getGroupCount() {
            return parent.length;
        }

        //  获得某个父项的子项数目
        @Override
        public int getChildrenCount(int parentPos) {
            return childGroups.get(parentPos).length;
        }

        //  获得某个父项
        @Override
        public Object getGroup(int parentPos) {
            return parent[parentPos];
        }

        //  获得某个父项的id
        @Override
        public long getGroupId(int parentPos) {
            return parentPos;
        }

        //  获得某个父项的某个子项的id
        @Override
        public long getChildId(int parentPos, int childPos) {
            return childPos;
        }

        //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
        @Override
        public boolean hasStableIds() {
            return false;
        }

        //  获得父项显示的view
        @Override
        public View getGroupView(int parentPos, boolean b, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.elv_items_group, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.group);
            ImageView arrow = (ImageView) convertView.findViewById(R.id.arrow);
            //设置箭头
            if (b) {
                arrow.setImageResource(R.drawable.open);
            } else {
                arrow.setImageResource(R.drawable.close);
            }
            tv.setText(parent[parentPos]);
            return convertView;
        }

        //  获得子项显示的view
        @Override
        public View getChildView(int parentPos, int childPos, boolean b, View convertView, ViewGroup
                viewGroup) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.elv_child_item, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.child);
            tv.setText(childGroups.get(parentPos)[childPos]);
            return convertView;
        }

        //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

    public static final String PARENT_POS = "parent_pos";
    public static final String CHILD_POS = "child_pos";
    public static final String INDEX = "index";

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
    }

}
