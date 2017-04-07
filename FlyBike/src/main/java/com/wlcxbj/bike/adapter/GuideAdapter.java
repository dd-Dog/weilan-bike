package com.wlcxbj.bike.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wlcxbj.bike.R;

import java.util.ArrayList;

/**
 * Created by itsdon on 17/4/7.
 */

public class GuideAdapter extends BaseExpandableListAdapter {

    private static final String TAG = "GuideAdapter";
    private Context context;
    private String[] parent;
    private ArrayList<String[]> childGroups;

    public GuideAdapter(Context context,String[] parent,ArrayList<String[]> childGroups){
        this.context = context;
        this.parent = parent;
        this.childGroups = childGroups;
    }


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
            convertView = View.inflate(context, R.layout.elv_items_group, null);
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
            convertView = View.inflate(context, R.layout.elv_child_item, null);
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
