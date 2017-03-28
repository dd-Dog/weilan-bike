package com.wlcxbj.bike.bean.account;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/6.
 */
public class AccountToken implements Serializable {

    private int errcode;
    private String errmsg;
    private AccountInfo account;
    private BasicInfo basicInfo;
    private RealInfo realInfo;

    public void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public BasicInfo getBasicInfo() {
        return basicInfo;
    }

    @Override
    public String toString() {
        return "AccountToken{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", account=" + account +
                ", basicInfo=" + basicInfo +
                ", realInfo=" + realInfo +
                '}';
    }

    public AccountInfo getAccount() {
        return account;
    }

    public void setAccount(AccountInfo account) {
        this.account = account;
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

    public RealInfo getRealInfo() {
        return realInfo;
    }

    public void setRealInfo(RealInfo realInfo) {
        this.realInfo = realInfo;
    }

}
