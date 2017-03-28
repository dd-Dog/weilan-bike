package com.wlcxbj.bike.bean.account;

/**
 * Created by Administrator on 2017/2/13.
 */
public class SmsCodeBean {

    private int errcode;
    private String errmsg;
    private String code;

    @Override
    public String toString() {
        return "SmsCodeBean{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getCode() {
        return code;
    }
}
