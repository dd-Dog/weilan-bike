package com.wlcxbj.bike.adapter;

/**
 * Created by bain on 17-1-17.
 */

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.TicketsDataEntity;
import com.wlcxbj.bike.bean.other.CouponBean;
import com.wlcxbj.bike.util.TimeUtil;

/**
 * adapter for recyclerview
 */
public class TicketsAdapter extends BaseQuickAdapter<CouponBean> {

    public TicketsAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, CouponBean couponBean) {
        viewHolder.setText(R.id.amount, couponBean.getAmount() + "");
        viewHolder.setText(R.id.ticket_type, "用车券");
        viewHolder.setText(R.id.ticket_deadLine, TimeUtil.getTimeStr(couponBean.getExpiredTime()));
    }

}