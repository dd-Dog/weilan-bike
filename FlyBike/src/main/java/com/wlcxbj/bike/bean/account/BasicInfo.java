package com.wlcxbj.bike.bean.account;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/13.
 */
public class BasicInfo implements Serializable {
    private String avatarUrl;
    private String nickName;

    @Override
    public String toString() {
        return "BasicInfo{" +
                "avatarUrl='" + avatarUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }
}