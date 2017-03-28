package com.libs.zxing;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by bain on 16-11-25.
 */
public class DpPxUtil {
    public static float getPix(Context context, float dp) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        float density = displayMetrics.density;
        return (dp * density + 0.5f);
    }
}
