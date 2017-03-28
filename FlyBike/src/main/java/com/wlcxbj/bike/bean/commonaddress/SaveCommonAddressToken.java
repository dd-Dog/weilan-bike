package com.wlcxbj.bike.bean.commonaddress;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/14.
 */
public class SaveCommonAddressToken implements Serializable{
    /*
    {"errcode":0,"errmsg":"OK","data":[
    {"id":7,"addr":"北京欢迎你","lng":"","lat":"","createdTime":1489546081000,"updatedTime":1489546081000},
    {"id":8,"addr":"北京欢迎你","lng":"","lat":"","createdTime":1489546109000,"updatedTime":1489546109000},
    {"id":9,"addr":"北京欢迎你","lng":"","lat":"","createdTime":1489546142000,"updatedTime":1489546142000},
    {"id":10,"addr":"北京欢迎你","lng":"","lat":"","createdTime":1489546167000,"updatedTime":1489546167000}]}
     */
    private int errcode;
    private String errmsg;
    private CommonAddressBean data;

    @Override
    public String toString() {
        return "SaveCommonAddressToken{" +
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

    public CommonAddressBean getData() {
        return data;
    }
}
