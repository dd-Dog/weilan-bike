package com.wlcxbj.bike.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/8.
 */
public class UserIconAddressBean implements Serializable{
    /*
    {
    "errcode": 0,
    "errmsg": "OK",
    "basePath": null,
    "relativePath": null
    }
     */
    private int errcode;
    private String errmsg;
    private String basePath;
    private String relativePath;

    @Override
    public String toString() {
        return "UserIconAddressBean{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", basePath='" + basePath + '\'' +
                ", relativePath='" + relativePath + '\'' +
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

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
}
