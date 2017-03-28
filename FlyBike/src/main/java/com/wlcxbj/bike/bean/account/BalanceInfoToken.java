package com.wlcxbj.bike.bean.account;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/24.
 */
public class BalanceInfoToken implements Serializable {
    //{"errcode":0,"errmsg":"OK",
    // "account":{"enduserId":3,
    // "balance":984.00,
    // "point":-200,"
    // guaranteeDeposit":0.00,
    // "ridingMileage":0,
    // "savedCarbonEmission":0,
    // "sportAchievement":0}}

    private int errcode;
    private String errmsg;
    private AccountInfo account;

    @Override
    public String toString() {
        return "BalanceInfoToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", account=" + account +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public AccountInfo getAccount() {
        return account;
    }
}
