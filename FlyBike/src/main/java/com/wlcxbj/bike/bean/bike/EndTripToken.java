package com.wlcxbj.bike.bean.bike;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/3.
 */
public class EndTripToken implements Serializable{
    /**
     * 骑行结束{"durationTime":22069,
     * "amount":"0.5",
     * "lng":"116.301628",
     * "balance":"1479.00",
     * "bizTypeSpid":0,
     * "enduserId":3,
     * "lockedTime":1488526450069,
     * "lat":"40.037403",
     *  "pushMsgSpid"："1" // 1:开始骑行 2：结束骑行
     * "tid":"1600190006"}
     */
    private String durationTime;
    private String amount;
    private String lng;
    private String balance;
    private String bizTypeSpid;
    private String enduserId;
    private String lockedTime;
    private String lat;
    private String tid;
    private String pushMsgSpid;

    public String getDurationTime() {
        return durationTime;
    }

    public String getAmount() {
        return amount;
    }

    public String getLng() {
        return lng;
    }

    public String getBalance() {
        return balance;
    }

    public String getBizTypeSpid() {
        return bizTypeSpid;
    }

    public String getEnduserId() {
        return enduserId;
    }

    public String getLockedTime() {
        return lockedTime;
    }

    public String getLat() {
        return lat;
    }

    public String getTid() {
        return tid;
    }

    public String getPushMsgSpid() { return  pushMsgSpid;};

    @Override
    public String toString() {
        return "EndTripToken{" +
                "durationTime='" + durationTime + '\'' +
                ", amount='" + amount + '\'' +
                ", lng='" + lng + '\'' +
                ", balance='" + balance + '\'' +
                ", bizTypeSpid='" + bizTypeSpid + '\'' +
                ", enduserId='" + enduserId + '\'' +
                ", lockedTime='" + lockedTime + '\'' +
                ", lat='" + lat + '\'' +
                ", tid='" + tid + '\'' +
                '}';
    }
}
