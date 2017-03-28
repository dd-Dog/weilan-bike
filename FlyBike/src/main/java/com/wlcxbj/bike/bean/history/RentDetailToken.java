package com.wlcxbj.bike.bean.history;

/**
 * Created by Administrator on 2017/2/14.
 */
public class RentDetailToken {
    //{"errcode":0,"errmsg":"OK","rentDetail":null}
    private int errcode;
    private String errmsg;
    private RentBikeBean rentDetail;


    @Override
    public String toString() {
        return "RentDetailToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", rentDetail='" + rentDetail + '\'' +
                '}';
    }

    public String getErrmsg() {
        return errmsg;
    }

    public RentBikeBean getRentDetail() {
        return rentDetail;
    }

    public int getErrcode() {
        return errcode;
    }
}
