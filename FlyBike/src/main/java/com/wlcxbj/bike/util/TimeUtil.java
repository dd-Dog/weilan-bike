package com.wlcxbj.bike.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/16.
 */
public class TimeUtil {
    static DecimalFormat df = new DecimalFormat("######0.00");

    public static String getTimeShort(long time) {
        StringBuilder sb = new StringBuilder();
        int hoursMillon = 60 * 60 * 1000;
        int minuteMillon = 60 * 1000;
        int hour = (int) (time / hoursMillon);
        int lastMinuTime = (int) (time % hoursMillon);
        int minutes = lastMinuTime / minuteMillon;
        int lastSecond = lastMinuTime % minuteMillon / 1000;
        sb.append(hour).append("小时").append(minutes).append("分").append(lastSecond).append("秒");
        return sb.toString();
    }

    public static int getMinites(long time) {
        return (int) (time / (1000 * 60));
    }

    public static String getTimeStr(long millions) {
        Date date = new Date(millions);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        return sdf.format(date);
    }

    public static String getTimeStr(String millions) {
        if (TextUtils.isEmpty(millions)) return null;
        long mill = Long.parseLong(millions);
        return getTimeStr(mill);
    }

    public static String getCoastByTime(long usingtime) {
        return df.format(getMinites(usingtime) * 0.02 + 0.5);
    }


    /**
     * 时间格式："2017-02-20T10:51:35Z"
     *
     * @param time
     * @return
     */
    public static long getMillionsFromISODate(String time) {
        if (!time.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z")) {
            return -1;
        }
        time = time.replaceFirst("T", " ").replaceFirst("Z", "");
        Date date = formatDate(time);
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.HOUR_OF_DAY, 8);
        return ca.getTime().getTime();
    }

    public static Date formatDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMiniteSecondsStr(int seconds) {
        String minite = seconds / 60 < 10 ? ("0" + seconds / 60) : (seconds / 60 + "");
        String second = seconds % 60 < 10 ? ("0" + seconds % 60) : (seconds % 60 + "");
        ;
        return minite + ":" + second;
    }
}
