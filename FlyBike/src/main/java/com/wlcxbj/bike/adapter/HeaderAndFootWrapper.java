package com.wlcxbj.bike.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bain on 16-11-26.
 * 对RecyclerView.Adapter的装饰类，增加Header和Footer,
 * 优点是不影响已经写好的Adapter的代码，只需要把原来的Adapter对象传到HeaderAndFootWrapper的构造函数中，
 * 并用HeaderAndFootWrapper对象替换掉原来使用的Adapter的引用即可
 */
public class HeaderAndFootWrapper extends RecyclerView.Adapter {
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;
    private SparseArrayCompat<View> mHeadersView = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootersView = new SparseArrayCompat<>();
    private static final String TAG = "HeaderAndFootWrapper";
    private RecyclerView.Adapter mInnerAdapter = null;

    public HeaderAndFootWrapper(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    //添加header，使用SparseArrayCompat存储，google对内部存储做了压缩，性能更好，使用类似于HashMap,
    //但是key值是整型的
    public void addHeader(View header) {
        mHeadersView.put(BASE_ITEM_TYPE_HEADER + mHeadersView.size(), header);
    }

    public void removeHeader(int index) {
        mHeadersView.removeAt(index);
    }

    public void addFooter(View footer) {
        mFootersView.put(BASE_ITEM_TYPE_FOOTER + mFootersView.size(), footer);
    }

    public void removeFooter(int index) {
        mFootersView.removeAt(index);
    }




    //是否是header
    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }

    //是否是footer
    private boolean isFooterViewPos(int position) {
        return position >= getHeadersCount() + getRealItemCount();
    }

    //获取除header和footer的条目个数
    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }

    //获取footer个数
    private int getFootersCount() {
        return mFootersView.size();
    }

    //获取header个数
    private int getHeadersCount() {
        return mHeadersView.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //针对Footer和Header做出调整
        if (mHeadersView.get(viewType) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mHeadersView.get(viewType));
            return holder;
        } else if (mFootersView.get(viewType) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(parent.getContext(), mFootersView.get(viewType));
            return holder;
        }

        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //如果是Header或者Footer不用绑定ViewHolder
        if (isFooterViewPos(position)) {
            Log.e(TAG, "position=" + position + "是header");
            return;
        }
        if (isHeaderViewPos(position)) {
            Log.e(TAG, "position=" + position + "是footer");
            return;
        }
        Log.e(TAG, "position=" + position + "是数据");
        //绑定数据的操作
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getFootersCount() + getRealItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            //如果是头布局，返回它对应的SparArray中存储的key值作为viewtype
            return mHeadersView.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootersView.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }
}
