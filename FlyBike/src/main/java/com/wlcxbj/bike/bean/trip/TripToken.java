package com.wlcxbj.bike.bean.trip;

/**
 * Created by Administrator on 2017/2/14.
 */
public class TripToken {
    //{"errcode":3001,"errmsg":"设备编号无效","tripId":null}

    private int errcode;
    private String errmsg;
    private String tripId;

    @Override
    public String toString() {
        return "TripToken{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", tripId='" + tripId + '\'' +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getTripId() {
        return tripId;
    }
}
