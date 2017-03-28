package com.wlcxbj.bike.bean.account;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/13.
 */
public class BasicUserInfoToken implements Serializable{
    //{"errcode":0,"errmsg":"OK","basicInfo":{"avatarUrl":null,"nickName":"sssssshnn"}}

    private int errcode;
    private String errmsg;
    private BasicInfo basicInfo;


    @Override
    public String toString() {
        return "BasicUserInfoToken{" +
                "errcode=" + errcode +
                ", errmsg=" + errmsg +
                ", basicInfo=" + basicInfo +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public BasicInfo getBasicInfo() {
        return basicInfo;
    }
}
