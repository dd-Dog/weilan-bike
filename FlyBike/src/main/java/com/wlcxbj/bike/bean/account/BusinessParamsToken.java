package com.wlcxbj.bike.bean.account;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/17.
 */

public class BusinessParamsToken implements Serializable{
    private int errcode;
    private String errmsg;
    private ArrayList<BussinessParams> bizparamModels;

    @Override
    public String toString() {
        return "BusinessParamsToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", bizparamModels=" + bizparamModels +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public ArrayList<BussinessParams> getBizparamModels() {
        return bizparamModels;
    }

    public void setBizparamModels(ArrayList<BussinessParams> bizparamModels) {
        this.bizparamModels = bizparamModels;
    }
    /*
    {
  "errcode": 0,
  "errmsg": "OK",
  "bizparamModels": [
    ,
    {
      "name": "GUARANTEE_DEPOSIT_AMOUNT",
      "value": "199.00",
      "note": "押金金额，单位：元"
    },
    {
      "name": "MAX_FREQ_ADDR",
      "value": "4",
      "note": "每人最多保存的常用地址数量"
    },
    {
      "name": "RENT_PRICE",
      "value": "0.5",
      "note": "骑行价格 单位： 元/半小时"
    },
    {
      "name": "RESERVATION_KEEP_TIME",
      "value": "15",
      "note": "预约超时时间，单位：分钟"
    }
  ]
}
     */
}
