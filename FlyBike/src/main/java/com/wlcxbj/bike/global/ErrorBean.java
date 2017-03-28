package com.wlcxbj.bike.global;

/**
 * Created by Administrator on 2017/2/16.
 */
public class ErrorBean {
    public int errorCode;
    public String errorStr;

    public ErrorBean(int errorCode, String errorStr) {
        this.errorCode = errorCode;
        this.errorStr = errorStr;
    }
}
