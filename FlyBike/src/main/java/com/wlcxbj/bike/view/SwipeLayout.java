package com.wlcxbj.bike.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.wlcxbj.bike.R;

/**
 * Created by Administrator on 2016/8/26.
 */
public class SwipeLayout extends FrameLayout {

    private View contentView;
    private View deleteView;
    private int contentWidth;
    private int contentHeight;
    private int deleteWidth;
    private ViewDragHelper viewDragHelper;
    private SwipeState currentState = SwipeState.Close;

    private enum SwipeState {
        Open, Close
    }

    private int touchSlop;

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = viewDragHelper.shouldInterceptTouchEvent(ev);
        //判断当前是否有打开的
        if (!SwipeLayoutManager.SINSTANCE.enableSwipe(this)) {
            Log.d("SwipeLayout", "有打开的Item---222---");
            SwipeLayoutManager.SINSTANCE.closeSwipeLayout();
            result = false;
        }
        return result;
    }

    /**
     * 当view从界面移除的时候
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //清空已经打开的
        SwipeLayoutManager.SINSTANCE.clearSwipeLayout();
    }


    long downTime;
    float x, y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果子view没有处理点击事件,那么最终还是由父View来处理,所以这里还要判断一下
        if (!SwipeLayoutManager.SINSTANCE.enableSwipe(this)) {
            //请求父view不要拦截
            requestDisallowInterceptTouchEvent(true);
            //不满足滑动条件,返回true,也不能让ListView接收触摸事件
            //其实直接return即可,即使return false父view也不会处理
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                downTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                //1.获取移动坐标
                float moveX = event.getX();
                float moveY = event.getY();
                //2.计算移动距离
                float deltaX = moveX - x;
                float deltaY = moveY - y;
                //判断滑动方向
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    //水平方向,滑动条目,则请求父ViewListView不应该拦截
                    requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                //自己实现点击事件
                long touchTime = System.currentTimeMillis() - downTime;
                //获取按下到抬起移动的距离
                float dx = event.getX() - x;
                float dy = event.getY() - y;
                float distance = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
                //如果时间小于400ms,并且距离小于touchSlop,就作为点击事件
                if (touchTime < 400 && distance < touchSlop) {

                }
                break;

        }

        viewDragHelper.processTouchEvent(event);
        return true;
    }

    ViewDragHelper.Callback callBack = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            //多年横向滑动的条件,大于0就行
            return 100;
        }

        /**
         * 修正移动的值
         * @param child
         * @param left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //限制contentView移动的值
            if (child == contentView) {
                if (left > 0) {
                    left = 0;
                }
                if (left < -deleteWidth) {
                    left = -deleteWidth;
                }
            } else if (child == deleteView) {
                //限制deleteView的移动
                if (left < contentWidth - deleteWidth) {
                    left = contentWidth - deleteWidth;
                }
                if (left > contentWidth) {
                    left = contentWidth;
                }
            }
            return left;
        }

        /**
         * 该方法一般用来做伴随移动
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //contentView移动的时候,deleteView也伴随移动
            if (changedView == contentView) {
                int newLeft = deleteView.getLeft() + dx;
                deleteView.layout(newLeft, deleteView.getTop(), newLeft + deleteWidth, deleteView
                        .getBottom());

            } else if (changedView == deleteView) {
                //当deleteView移动时,contgentView伴随滑动
                int newLeft = contentView.getLeft() + dx;
                contentView.layout(newLeft, contentView.getTop(), newLeft + contentWidth,
                        contentView.getBottom());
            }

            if (contentView.getLeft() == 0 && currentState != SwipeState.Close) {
                //切换状态为关闭
                currentState = SwipeState.Close;
                SwipeLayoutManager.SINSTANCE.clearSwipeLayout();
            } else if (contentView.getLeft() == -deleteWidth && currentState != SwipeState.Open) {
                //切换状态为打开
                currentState = SwipeState.Open;
                SwipeLayoutManager.SINSTANCE.setSwipeLayout(SwipeLayout.this);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (contentView.getLeft() < -deleteWidth / 2) {
                //向左
                open();
            } else {
                //向右
                close();
            }
        }
    };

    //关闭条目
    public void close() {
        viewDragHelper.smoothSlideViewTo(contentView, 0, contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    //打开条目
    private void open() {
        viewDragHelper.smoothSlideViewTo(contentView, -deleteWidth, contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    @Override
    public void computeScroll() {
        //持续刷新
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void init() {
//        touchSlop =
        viewDragHelper = ViewDragHelper.create(this, callBack);
    }

    /**
     * 初始化子View,该方法在填充子View完后,onMeasure()执行之前执行
     */
    private View.OnClickListener deleteClickLisnter;
    private View.OnClickListener editClickLisnter;

    public void setOnDeleteClickListener(View.OnClickListener listener) {
        this.deleteClickLisnter = listener;
    }

    public void setOnEditClickListener(View.OnClickListener listener) {
        this.editClickLisnter = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        deleteView = getChildAt(1);

        deleteView.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClickLisnter != null) {
                    deleteClickLisnter.onClick(v);
                }
            }
        });

        deleteView.findViewById(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editClickLisnter != null) {
                    editClickLisnter.onClick(v);
                }
            }
        });

    }

    /**
     * 在onMeasure()执行完之后执行
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentWidth = contentView.getMeasuredWidth();
        contentHeight = contentView.getMeasuredHeight();
        deleteWidth = deleteView.getMeasuredWidth();
        int deleteHeight = deleteView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        contentView.layout(0, 0, contentWidth, contentHeight);
        deleteView.layout(contentWidth, 0, contentWidth + deleteWidth, contentHeight);
    }


    public void setOnClickListener(OnClickListener listener) {
        OnClickListener listener1 = listener;
    }

    public interface OnClickListener {
    }

}
