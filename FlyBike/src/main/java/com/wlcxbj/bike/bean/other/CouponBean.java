package com.wlcxbj.bike.bean.other;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/2/13.
 */
public class CouponBean {

    //
    // *
    /*
    {"id":9,
    "no":"00000001",
    "enduserId":3,
    "amount":100.50,
    "typeSpid":1,
    "statusSpid":1,
    "expiredTime":1490010648000,
    "note":null,
    "createdTime":1487591448000,
    "assignedTime":null,
    "exchangedTime":null}
     */

    private int id;
    private long no;
    private  int enduserId;
    private BigDecimal amount;
    private int typeSpid;
    private int statusSpid;
    private long expiredTime;
    private String note;
    private long createdTime;
    private long assignedTime;
    private long exchangedTime;


    @Override
    public String toString() {
        return "CouponBean{" +
                "id=" + id +
                ", no=" + no +
                ", enduserId=" + enduserId +
                ", amount=" + amount +
                ", typeSpid=" + typeSpid +
                ", statusSpid=" + statusSpid +
                ", expiredTime=" + expiredTime +
                ", note='" + note + '\'' +
                ", createdTime=" + createdTime +
                ", assignedTime=" + assignedTime +
                ", exchangedTime=" + exchangedTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public long getNo() {
        return no;
    }

    public int getEnduserId() {
        return enduserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getTypeSpid() {
        return typeSpid;
    }

    public int getStatusSpid() {
        return statusSpid;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public String getNote() {
        return note;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public long getAssignedTime() {
        return assignedTime;
    }

    public long getExchangedTime() {
        return exchangedTime;
    }
}
