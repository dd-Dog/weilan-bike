package com.wlcxbj.bike.bean.oss;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/20.
 */
public class StsCredentialsModelBean implements Serializable {
    private String expiration;
    private String securityToken;
    private String accessKeyId;
    private String accessKeySecret;
    private String roleSessionName;

    @Override
    public String toString() {
        return "StsCredentialsModelBean{" +
                "expiration='" + expiration + '\'' +
                ", securityToken='" + securityToken + '\'' +
                ", accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='" + accessKeySecret + '\'' +
                ", roleSessionName='" + roleSessionName + '\'' +
                '}';
    }

    public String getExpiration() {
        return expiration;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public String getRoleSessionName() {
        return roleSessionName;
    }
}
