package com.wlcxbj.bike.bean.history;

/**
 * Created by Administrator on 2017/3/16.
 */

public class TransactionListBean {
    private String orderTypeSpids;	//	订单类型，必填，可指定多种类型，类型之间用英文逗号分隔，至少指定一种
    //取值范围参考系统参数 groupId=COMMON, codeType=ORDER_TYPE
    private String startTime;	//	开始日期 格式 yyyyMMddHHmmSS 选填
    private String endTime;  //结束日期 格式 yyyyMMddHHmmSS选填

    public String getOrderTypeSpids() {
        return orderTypeSpids;
    }

    public void setOrderTypeSpids(String orderTypeSpids) {
        this.orderTypeSpids = orderTypeSpids;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
