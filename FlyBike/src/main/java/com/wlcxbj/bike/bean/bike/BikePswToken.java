package com.wlcxbj.bike.bean.bike;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/13.
 */
public class BikePswToken implements Serializable{
    //{"errcode":0,"errmsg":"OK","tno":"019000123","tid":"1699954321","mac":"E8:EB:11:09:61:38","unlockCode":"123"}
    private int errcode;
    private String errmsg;
    private String tno;
    private String tid;
    private String mac;
    private String unlockCode;


    @Override
    public String toString() {
        return "BikePswToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", tno='" + tno + '\'' +
                ", tid='" + tid + '\'' +
                ", mac='" + mac + '\'' +
                ", unlockCode='" + unlockCode + '\'' +
                '}';
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getTno() {
        return tno;
    }

    public void setTno(String tno) {
        this.tno = tno;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setUnlockCode(String unlockCode) {
        this.unlockCode = unlockCode;
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getTid() {
        return tid;
    }

    public String getUnlockCode() {
        return unlockCode;
    }
}
