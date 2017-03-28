package com.wlcxbj.bike.bean.pay;

/**
 * Created by Administrator on 2017/2/14.
 */
public class OrderBean {
    public static final int ORDER_TYPE_DEPOSIT = 1;
    public static final int ORDER_TYPE_RECHARGE = 2;

    private int orderTypeSpid;//订单类型:取值范围参考系统参数 groupId=COMMON, codeType= ORDER_PAY_STATUS
    private String orderAmount;  //订单金额	单位：元
    private String realpayAmount;//实际应支付金额，单位：元
    private String cuid;         //运营商编号

    public OrderBean(int orderTypeSpid, String orderAmount, String realpayAmount, String cuid) {
        this.orderTypeSpid = orderTypeSpid;
        this.orderAmount = orderAmount;
        this.realpayAmount = realpayAmount;
        this.cuid = cuid;
    }

    public int getOrderTypeSpid() {
        return orderTypeSpid;
    }

    public void setOrderTypeSpid(int orderTypeSpid) {
        this.orderTypeSpid = orderTypeSpid;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getRealpayAmount() {
        return realpayAmount;
    }

    public void setRealpayAmount(String realpayAmount) {
        this.realpayAmount = realpayAmount;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }
}
