package com.wlcxbj.bike.bean.history;

/**
 * Created by bain on 16-11-30.
 */
public class TransactionBean {
    /*
     * {"id":10000000040,"orderTypeSpid":1,"amount":1.00,"intoAccountTime":1489576431000,
     * "payTypeSpid":2}
     */
    private static final String TYPE_I = "交押金";
    private static final String TYPE_II = "充值";
    private static final String TYPE_III = "支付租车费";
    private long id;
    private int orderTypeSpid;
    private int payTypeSpid;
    private double amount;
    private long intoAccountTime;

    @Override
    public String toString() {
        return "TransactionBean{" +
                "id=" + id +
                ", orderTypeSpid=" + orderTypeSpid +
                ", payTypeSpid=" + payTypeSpid +
                ", amount=" + amount +
                ", intoAccountTime=" + intoAccountTime +
                '}';
    }

    public long getId() {
        return id;
    }

    public int getOrderTypeSpid() {
        return orderTypeSpid;
    }

    public int getPayTypeSpid() {
        return payTypeSpid;
    }

    public double getAmount() {
        return amount;
    }

    public long getIntoAccountTime() {
        return intoAccountTime;
    }

    public String getOrderTypeStr(int type) {
        String str = null;
        switch (type) {
            case 1:
                str = TYPE_I;
                break;
            case 2:
                str = TYPE_II;
                break;
            case 3:
                str = TYPE_III;
                break;
        }
        return str;
    }
}
