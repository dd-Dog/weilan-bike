package com.wlcxbj.bike.bean.pay;

/**
 * Created by Administrator on 2017/2/14.
 */
public class ALiPayToken {
    /**
     * {
     "errcode": 0,
     "errmsg": "OK",
     "payStr": "app_id=2016073100134806&biz_content=%7B%22body%22%3A%22%E7%A7%9F%E8%BD%A6%E6%8A%BC%E9%87%91%22%2C%22subject%22%3A%22%E7%A7%9F%E8%BD%A6%E6%8A%BC%E9%87%91%22%2C%22out_trade_no%22%3A%2210000000106%22%2C%22total_amount%22%3A%220.01%22%2C%22passback_params%22%3A%22YJA%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F115.28.211.130%3A8081%2Fapi%2Fcb%2Fzfb%2Fasync%2Fcb&sign_type=RSA2&timestamp=2017-02-14+09%3A31%3A37&version=1.0&sign=dAbsgW8xCbuN28E7AqVDnza7bBDDU7upLbXsPYDxSy59DGVgGnuA%2Fg4K8BzcRU8w1WXfEJyQOmQ4Ck5R1vYOjp%2FA%2FU9kXGfYZ2cF9pjhjnDk92nIg8Tas1U9V8wakRSMleZZmRfkaF1f0YN4lPlILrQ1ypaFAA5j46Bur8kPGsGmstB95yxdRsaK0liYGGDfeaKlNF3aKfBcYffqb3%2Bn3pUJQ3Zw7LT692FnM6vjnZNk9b4j%2FDhm5T4GMJWG221whEZY%2FSJk6wqjDsk1VTBPuKLO1tlkVVztWRfbWMcz93Yol6sc3Wx1Knxsx7vFOsYcRfMvDMAq6wVQWJQ6M0ma4w%3D%3D"
     }
     */
    private int errcode;
    private String errmsg;
    private String payStr;


    @Override
    public String toString() {
        return "ALiPayToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", payStr='" + payStr + '\'' +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getPayStr() {
        return payStr;
    }
}
