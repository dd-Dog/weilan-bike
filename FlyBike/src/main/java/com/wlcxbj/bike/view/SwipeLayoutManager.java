package com.wlcxbj.bike.view;

/**
 * Created by Administrator on 2016/8/26.
 * 负责打开的item的记录，清除，以及判断是否有打开的条目
 */
public enum SwipeLayoutManager {
    SINSTANCE;

    private static SwipeLayout mSwipeLayout = null;

    public static void setSwipeLayout(SwipeLayout swipeLayout) {
        mSwipeLayout = swipeLayout;
    }

    public static void clearSwipeLayout() {
        mSwipeLayout = null;
    }

    public static boolean enableSwipe(SwipeLayout swipeLayout) {
        if (mSwipeLayout == null) {
            return true;
        } else {
            return mSwipeLayout == swipeLayout;
        }
    }

    public static SwipeLayout getOpenSwipeLayout() {
        return mSwipeLayout;
    }

    public static void closeSwipeLayout() {
        if (mSwipeLayout != null) {
            mSwipeLayout.close();
        }
    }
}
