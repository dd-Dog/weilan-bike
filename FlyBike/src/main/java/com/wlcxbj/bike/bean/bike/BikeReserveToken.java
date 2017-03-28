package com.wlcxbj.bike.bean.bike;

/**
 * Created by Administrator on 2017/2/16.
 */
public class BikeReserveToken {
    //{"errcode":0,"errmsg":"OK","tid":"1600190173","plateno":"","reservationId":null
    public static final int STATE_AVAILABLE = 0;
    public static final int STATE_PRESERVED = 3004;
    public static final int STATE_BALANCE_EAGER = 3005;


    private String errcode;
    private String errmsg;
    private String tid;
    private String plateno;
    private String reservationId;

    public String getReservationId() {
        return reservationId;
    }

    @Override
    public String toString() {
        return "BikeReserveToken{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", tid='" + tid + '\'' +
                ", plateno='" + plateno + '\'' +
                ", reservationId='" + reservationId + '\'' +
                '}';
    }

    public String getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getTid() {
        return tid;
    }

    public String getPlateno() {
        return plateno;
    }
}
