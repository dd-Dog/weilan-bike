package com.wlcxbj.bike.view;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.wlcxbj.bike.R;

/**
 * Created by Administrator on 2017/3/3.
 */
public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        this(context, -1);
    }

    public CustomDialog(Context context, int themeResId) {
        super(context, R.style.quick_option_dialog);
    }

    protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams
                .WRAP_CONTENT);
    }
}
