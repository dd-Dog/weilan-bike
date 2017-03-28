package com.wlcxbj.bike.bean.bike;

/**
 * Created by Administrator on 2017/3/2.
 */
public class TiketsExchangeToken {
    //{"errcode":0,"errmsg":"OK"}
    private int errcode;
    private String errmsg;


    @Override
    public String toString() {
        return "TiketsExchangeToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }
}
