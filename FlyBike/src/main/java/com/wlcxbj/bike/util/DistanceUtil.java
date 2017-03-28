package com.wlcxbj.bike.util;

import java.text.DecimalFormat;

/**
 * Created by itsdon on 17/3/24.
 */

public class DistanceUtil {

    private static DecimalFormat df = new DecimalFormat("######0.00");
    public static String getDistance(double distance){

         if(distance < 0){
             return "0千米";
         }
         if(distance < 1000){
             return df.format(distance)+"米";
         }
        return df.format(distance/1000.0)+"千米";
    }

}
