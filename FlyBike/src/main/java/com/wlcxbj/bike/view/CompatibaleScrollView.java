package com.wlcxbj.bike.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by bain on 16-12-2.
 */
public class CompatibaleScrollView extends ScrollView {
    private ScrollViewListener mListener;
    public CompatibaleScrollView(Context context) {
        super(context);
    }

    public CompatibaleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompatibaleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface ScrollViewListener {
        void onScrollChanged(CompatibaleScrollView scrollView, int x, int y, int oldx, int oldy);
    }

    public void setScrollViewListener(ScrollViewListener listener) {
        this.mListener = listener;
    }
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener != null) {
            mListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
