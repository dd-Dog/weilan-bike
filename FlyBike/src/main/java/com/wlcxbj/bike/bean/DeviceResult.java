package com.wlcxbj.bike.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/10.
 */

/**
 * 返回请求结果信息
 */
public class DeviceResult {
    private String ecode;
    private String emsg;
    private ArrayList<DeviceInfo> data;

    public String getEcode() {
        return ecode;
    }

    public String getEmsg() {
        return emsg;
    }

    public ArrayList<DeviceInfo> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "DeviceResult{" +
                "ecode='" + ecode + '\'' +
                ", emsg='" + emsg + '\'' +
                ", data=" + data +
                '}';
    }
}
