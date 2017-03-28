package com.wlcxbj.bike.bean.commonaddress;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/14.
 */
public class CommonAddressListToken implements Serializable {
    private int errcode;
    private String errmsg;
    private ArrayList<CommonAddressBean> data;

    @Override
    public String toString() {
        return "CommonAddressListToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public ArrayList<CommonAddressBean> getData() {
        return data;
    }
}
