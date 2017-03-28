package com.wlcxbj.bike.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.wlcxbj.bike.R;


/**
 * Created by bain on 16-11-29.
 */
public class StateView extends FrameLayout {

    private View loadingView;
    private View successView;
    private View empytView;
    private View errorView;
    private OnReloadListener mListener;

    public StateView(Context context) {
        this(context, null);
    }

    public StateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化布局，正在加载页面，加载失败界面
     * 加载成功界面包括有数据和无数据的情况，这由具体的使用类来实现
     */
    private void init() {
        loadingView = View.inflate(getContext(), R.layout.loading_layout, null);
        errorView = View.inflate(getContext(), R.layout.loading_fail, null);
        errorView.findViewById(R.id.reload).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onReload();
                }
            }
        });
        addView(loadingView);
        addView(errorView);
        hideAllView();
    }

    /**
     * 显示加载失败布局
     */
    public void showErrorView() {
        hideAllView();
        if (errorView != null) {
            errorView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示加载成功但是数据为空的布局
     */
    public void showEmpytView() {
        hideAllView();
        if (empytView != null) {
            empytView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 显示加载成功布局
     */
    public void showSuccessView() {
        hideAllView();
        if (successView != null) {
            successView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置加载成功界面
     *
     * @param view
     */
    public void setSuccessView(View view) {
        if (view != null) {
            successView = view;
            successView.setVisibility(View.VISIBLE);
            addView(successView);
        }
    }

    /**
     * 设置加载成功，但没有数据的界面
     *
     * @param view
     */
    public void setEmpytView(View view) {
        if (view != null) {
            empytView = view;
            empytView.setVisibility(View.VISIBLE);
            addView(empytView);
        }
    }

    /**
     * 隐藏所有布局
     */
    public void hideAllView() {
        loadingView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        if (empytView != null) {
            empytView.setVisibility(View.INVISIBLE);
        }
        if (successView != null) {
            successView.setVisibility(View.INVISIBLE);
        }
    }

    public void setOnReloadListener(OnReloadListener listener) {
        mListener = listener;
    }

    public interface OnReloadListener {
        void onReload();
    }
}
