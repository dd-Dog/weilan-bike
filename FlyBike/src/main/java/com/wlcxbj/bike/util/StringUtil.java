package com.wlcxbj.bike.util;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.wlcxbj.bike.R;

/**
 * Created by itsdon on 17/3/29.
 */

public class StringUtil {


    public static SpannableString getRichText(Activity activity, String str){
        SpannableString spanStr = new SpannableString(str);
        spanStr.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.green_68)),4,str.indexOf(".")+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public static SpannableString getRiceText(Activity activity,String str,int start,int end,int colorRsd){
        SpannableString spanStr = new SpannableString(str);
        spanStr.setSpan(new ForegroundColorSpan(activity.getResources().getColor(colorRsd)),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

}
