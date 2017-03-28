package com.libs.zxing;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

/**
 * Created by bain on 16-11-23.
 */
public class HelpeDialog extends Dialog{

    private Context context;
    public HelpeDialog(Context context) {
        this(context, 0);
        this.context = context;
        init();
    }

    public HelpeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_helper, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);
    }
}
