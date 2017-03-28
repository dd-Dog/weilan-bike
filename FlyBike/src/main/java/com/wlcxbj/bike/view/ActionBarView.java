package com.wlcxbj.bike.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wlcxbj.bike.R;

/**
 * Created by bain on 16-11-30.
 */
public class ActionBarView extends RelativeLayout {

    private TextView tvCenter;
    private TextView tvRight;
    private RelativeLayout actionBar;

    public ActionBarView(Context context) {
        this(context, null);
    }

    public ActionBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ActionBarAttrs);
        String center = typedArray.getString(R.styleable.ActionBarAttrs_centerText);
        String right = typedArray.getString(R.styleable.ActionBarAttrs_rightText);
        boolean rightEnalbed = typedArray.getBoolean(R.styleable.ActionBarAttrs_rightEnable, true);
        int bgColor = typedArray.getColor(R.styleable.ActionBarAttrs_actionBar_background, context.getResources().getColor(R.color.green));
        int rightTxtClor = typedArray.getColor(R.styleable.ActionBarAttrs_right_text_color, context.getResources().getColor(R.color.WHITE));
        int rightTxtSize = typedArray.getInteger(R.styleable.ActionBarAttrs_right_text_size, 16);

        typedArray.recycle();
        //初始化控件
        tvCenter.setText(center);
        tvCenter.getPaint().setFakeBoldText(true);
        tvRight.setText(right);
        tvRight.setVisibility(rightEnalbed? View.VISIBLE : View.INVISIBLE);
        tvRight.setTextColor(rightTxtClor);
        tvRight.setTextSize(rightTxtSize);
        actionBar.setBackgroundColor(bgColor);
    }

    private void init() {
        actionBar = (RelativeLayout) View.inflate(getContext(), R.layout.layout_actionbar, null);
        tvCenter = (TextView) actionBar.findViewById(R.id.tv_center);
        tvRight = (TextView) actionBar.findViewById(R.id.tv_right);
        addView(actionBar);
    }

}
