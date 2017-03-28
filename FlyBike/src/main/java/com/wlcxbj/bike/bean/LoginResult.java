package com.wlcxbj.bike.bean;

/**
 * Created by Administrator on 2016/11/10.
 */
public class LoginResult {

    private int ecode;
    private String msg;
    private LoginInfo data;

    public int getEcode() {
        return ecode;
    }

    public String getMsg() {
        return msg;
    }

    public LoginInfo getData() {
        return data;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "ecode=" + ecode +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
