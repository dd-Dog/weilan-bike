package com.wlcxbj.bike.bean.bike;

/**
 * Created by Administrator on 2017/2/16.
 */
public class CancelReserveToken {
    //{"errcode":0,"errmsg":"OK"}
    private int errcode;
    private String errmsg;

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    @Override
    public String toString() {
        return "CancelReserveToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
