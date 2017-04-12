package com.wlcxbj.bike.util;

import android.app.Activity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
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

    public static SpannableString getRiceText(Activity activity,String str,int start,int end,int colorRsd,int fontsize){
        SpannableString spanStr = new SpannableString(str);
        spanStr.setSpan(new ForegroundColorSpan(activity.getResources().getColor(colorRsd)),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       if(fontsize > 0){
           spanStr.setSpan(new AbsoluteSizeSpan(fontsize),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       }
        return spanStr;
    }

    public static SpannableString getRichTextForAmount(Activity activity, String str){
        SpannableString spanStr = new SpannableString(str);
        spanStr.setSpan(new AbsoluteSizeSpan(DpPxUtil.sp2px(activity,14f)),0,1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }


}
