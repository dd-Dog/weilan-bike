package com.wlcxbj.bike.adapter;

/**
 * Created by bain on 17-1-17.
 */

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.MsgDataEntity;

/**
 * adapter for recyclerview
 */
public class MsgAdapter extends BaseQuickAdapter<MsgDataEntity> {

    public MsgAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, MsgDataEntity item) {
        viewHolder.setText(R.id.title, item.msgTitle);
        viewHolder.setVisible(R.id.unread, item.isRead);
        viewHolder.setText(R.id.content, item.msgContent);
    }

}