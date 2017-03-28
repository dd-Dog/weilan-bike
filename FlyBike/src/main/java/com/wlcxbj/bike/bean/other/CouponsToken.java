package com.wlcxbj.bike.bean.other;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/13.
 */
public class CouponsToken {
    public static final int COUPON_TYPE_I = 1;
    public static final int COUPON_TYPE_II = 1;
    public static final int COUPON_STATUS_NOT_RELEASED = 1;
    public static final int COUPON_TYPE_RELEASED = 2;
    public static final int COUPON_TYPE_NOT_USED = 3;
    public static final int COUPON_TYPE_ALREADY_USED = 4;
//    {"errcode":0,"errmsg":"OK","coupons":[]}

    private int errcode;
    private String errmsg;
    private ArrayList<CouponBean> coupons;

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public ArrayList<CouponBean> getCoupons() {
        return coupons;
    }

    @Override
    public String toString() {
        return "CouponsToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", coupons=" + coupons +
                '}';
    }
}
