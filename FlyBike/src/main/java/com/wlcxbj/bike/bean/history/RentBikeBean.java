package com.wlcxbj.bike.bean.history;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/3/2.
 */
public class RentBikeBean implements Serializable{
    /*
        {"id":205,
     *          "tid":"1600190010",
     *          "amount":1.50,
     *          "statusSpid":4,
     *          "payStatusSpid":1,
     *          "startTime":1487591448000,
     *          "endTime":1487595048000,
     *          "paidTime":null,
     *          "userStartLat":"40.037304",
     *          "userStartLng":"116.301750",
     *          "bikeStartLat":"40.037305",
     *          "bikeStartLng":"116.301754",
     *          "userEndLat":"40.047304",
     *          "userEndLng":"116.305750",
     *          "bikeEndLat":"40.047305",
     *          "bikeEndLng":"116.305754",
     *          "intoAccountTime":null,
     *          "payTypeSpid":1,
     *          "payId":"100010001"},
     */

    private Long id;
    private String tid;
    private String amount;
    private int statusSpid;
    private int payStatusSpid;
    private String startTime;
    private String endTime;
    private String paidTime;
    private String userStartLat;
    private String userStartLng;
    private String bikeStartLat;
    private String bikeStartLng;
    private String userEndLat;
    private String userEndLng;
    private String bikeEndLat;
    private String bikeEndLng;
    private String intoAccountTime;
    private int payTypeSpid;
    private String payId;

    @Override
    public String toString() {
        return "RentBikeBean{" +
                "id=" + id +
                ", tid='" + tid + '\'' +
                ", amount=" + amount +
                ", statusSpid=" + statusSpid +
                ", payStatusSpid=" + payStatusSpid +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", paidTime=" + paidTime +
                ", userStartLat=" + userStartLat +
                ", userStartLng=" + userStartLng +
                ", bikeStartLat=" + bikeStartLat +
                ", bikeStartLng=" + bikeStartLng +
                ", userEndLat=" + userEndLat +
                ", userEndLng=" + userEndLng +
                ", bikeEndLat=" + bikeEndLat +
                ", bikeEndLng=" + bikeEndLng +
                ", intoAccountTime=" + intoAccountTime +
                ", payTypeSpid=" + payTypeSpid +
                ", payId='" + payId + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getTid() {
        return tid;
    }

    public String getAmount() {
        return amount;
    }

    public int getStatusSpid() {
        return statusSpid;
    }

    public int getPayStatusSpid() {
        return payStatusSpid;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPaidTime() {
        return paidTime;
    }

    public String getUserStartLat() {
        return userStartLat;
    }

    public String getUserStartLng() {
        return userStartLng;
    }

    public String getBikeStartLat() {
        return bikeStartLat;
    }

    public String getBikeStartLng() {
        return bikeStartLng;
    }

    public String getUserEndLat() {
        return userEndLat;
    }

    public String getUserEndLng() {
        return userEndLng;
    }

    public String getBikeEndLat() {
        return bikeEndLat;
    }

    public String getBikeEndLng() {
        return bikeEndLng;
    }

    public String getIntoAccountTime() {
        return intoAccountTime;
    }

    public int getPayTypeSpid() {
        return payTypeSpid;
    }

    public String getPayId() {
        return payId;
    }
}
