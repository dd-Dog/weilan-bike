package com.wlcxbj.bike.bean.history;

/**
 * Created by Administrator on 2017/2/14.
 */
public class RechargeListBean {

    private int payTypeSpid;  //支付方式，选填，保留字段，暂时未用,取值范围参考系统参数 groupId=COMMON, codeType=PAY_TYPE
    private int pageNo;     //第几页	选填
    private int pageSize;	//每页大小   选填


    public RechargeListBean() {
    }

    public RechargeListBean(int payTypeSpid, int pageNo, int pageSize) {
        this.payTypeSpid = payTypeSpid;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public int getPayTypeSpid() {
        return payTypeSpid;
    }

    public void setPayTypeSpid(int payTypeSpid) {
        this.payTypeSpid = payTypeSpid;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
