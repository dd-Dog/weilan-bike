package com.wlcxbj.bike.bean;

/**
 * Created by itsdon on 17/4/17.
 */

public class VersionBean {
//    {
//        "errcode": 0,
//            "errmsg": "OK",
//            "version": "v1.1.0",
//            "updateUrl": "http://www.wlcx.com",
//            "appDesc": "init",
//            "appType": 1,
//            "force": "yes",
//            "latest": "no"
//    }
    private  int errcode;
    private  String errmsg;
    private  String version;
    private  String updateUrl;
    private  String appDesc;
    private  String appType;
    private  String force;
    private  String latest;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getLatest() {
        return latest;
    }

    public void setLatest(String latest) {
        this.latest = latest;
    }

    @Override
    public String toString() {
        return "VersionBean{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", version='" + version + '\'' +
                ", updateUrl='" + updateUrl + '\'' +
                ", appDesc='" + appDesc + '\'' +
                ", appType='" + appType + '\'' +
                ", force=" + force +
                ", latest=" + latest +
                '}';
    }
}
