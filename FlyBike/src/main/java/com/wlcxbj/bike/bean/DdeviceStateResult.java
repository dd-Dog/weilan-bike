package com.wlcxbj.bike.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/10.
 */
public class DdeviceStateResult {

    private int ecode;
    private String emsg;
    private ArrayList<DeviceStateInfo> data;

    public int getEcode() {
        return ecode;
    }

    public String getEmsg() {
        return emsg;
    }

    public ArrayList<DeviceStateInfo> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "DdeviceStateResult{" +
                "ecode=" + ecode +
                ", emsg='" + emsg + '\'' +
                ", data=" + data +
                '}';
    }
}
