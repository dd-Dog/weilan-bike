package com.wlcxbj.bike.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.MsgDataEntity;
import com.wlcxbj.bike.bean.history.RentBikeBean;
import com.wlcxbj.bike.util.TimeUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */

public class MyRoutesAdapter extends BaseQuickAdapter<RentBikeBean> {
    public MyRoutesAdapter(int layoutResId, List<RentBikeBean> data, Context context) {
        super(layoutResId, data);
        mContext = context;
    }
    private Context mContext;
    @Override
    protected void convert(BaseViewHolder baseViewHolder, RentBikeBean item) {
        baseViewHolder.setText(R.id.tv_time, TimeUtil.getTimeStr(item.getIntoAccountTime()));
        baseViewHolder.setText(R.id.tv_bikeNum, (item.getTid() + ""));
        baseViewHolder.setText(R.id.tv_using_money, mContext.getResources().getString(R.string.currency_symble) + item.getAmount());
    }
}
