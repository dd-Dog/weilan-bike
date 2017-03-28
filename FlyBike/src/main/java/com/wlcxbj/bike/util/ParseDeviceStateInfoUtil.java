package com.wlcxbj.bike.util;

import android.text.TextUtils;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import com.wlcxbj.bike.bean.DeviceStateInfo;


/**
 * Created by Administrator on 2016/11/14.
 */
public class ParseDeviceStateInfoUtil {

    public static LatLng getLatLng(DeviceStateInfo info) {
        //获取纬经度
        String latStr = info.getLat();
        String lngStr = info.getLng();
        if (TextUtils.isEmpty(latStr) || TextUtils.isEmpty(lngStr)) {
            return null;
        }
        double lat = Double.parseDouble(latStr);
        double lng = Double.parseDouble(lngStr);
        LatLng latLng = new LatLng(lat, lng);
        return latLng;
    }

    public static LatLonPoint getLatLonPoint(DeviceStateInfo info) {
        //获取纬经度
        String latStr = info.getLat();
        String lngStr = info.getLng();
        if (TextUtils.isEmpty(latStr) || TextUtils.isEmpty(lngStr)) {
            return null;
        }
        double lat = Double.parseDouble(latStr);
        double lng = Double.parseDouble(lngStr);
        LatLonPoint latLng = new LatLonPoint(lat, lng);
        return latLng;
    }
}
