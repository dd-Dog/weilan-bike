package com.wlcxbj.bike.bean.pay;

/**
 * Created by Administrator on 2017/2/14.
 */
public class WechatPayToken {
    /**
     {
     "errcode": 0,
     "errmsg": "OK",
     "appId": "wx1d475f8d0e170c00",
     "partnerId": "1395514402",
     "prepayId": "wx20170214101049a218c4ea900034613893",
     "timeStamp": "1487038250",
     "nonceStr": "y0b5c3p3lxqul69q",
     "pkg": "Sign=WXPay",
     "paySign": "024AB050452F75F203C51E0829266499"
     }
     */
    private int  errcode;
    private String errmsg;
    private String appId;
    private String partnerId;
    private String prepayId;
    private String timeStamp;
    private String nonceStr;
    private String pkg;
    private String paySign;

    @Override
    public String toString() {
        return "WechatPayToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", appId='" + appId + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", pkg='" + pkg + '\'' +
                ", paySign='" + paySign + '\'' +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getAppId() {
        return appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public String getPkg() {
        return pkg;
    }

    public String getPaySign() {
        return paySign;
    }
}
