package com.wlcxbj.bike.bean;

/**
 * Created by Administrator on 2016/11/10.
 */
public class LoginInfo {
    private int uid;
    private String user_name;
    private String user_role;

    public int getUid() {
        return uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_role() {
        return user_role;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "uid=" + uid +
                ", user_name='" + user_name + '\'' +
                ", user_role='" + user_role + '\'' +
                '}';
    }
}
