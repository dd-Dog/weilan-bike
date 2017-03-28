package com.wlcxbj.bike.bean;

/**
 * Created by Administrator on 2017/2/8.
 */
public class WriteAccessAtOssBean {
    private int errcode;
    private String errmsg;
    private StsCredentialsModel stsCredentialsModel;

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

    public StsCredentialsModel getStsCredentialsModel() {
        return stsCredentialsModel;
    }

    public void setStsCredentialsModel(StsCredentialsModel stsCredentialsModel) {
        this.stsCredentialsModel = stsCredentialsModel;
    }

    @Override
    public String toString() {
        return "WriteAccessAtOssBean{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", stsCredentialsModel=" + stsCredentialsModel +
                '}';
    }

    public class StsCredentialsModel {
        private String expiration;
        private String securityToken;

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getSecurityToken() {
            return securityToken;
        }

        public void setSecurityToken(String securityToken) {
            this.securityToken = securityToken;
        }

        @Override
        public String toString() {
            return "StsCredentialsModel{" +
                    "expiration='" + expiration + '\'' +
                    ", securityToken='" + securityToken + '\'' +
                    '}';
        }
    }
}
