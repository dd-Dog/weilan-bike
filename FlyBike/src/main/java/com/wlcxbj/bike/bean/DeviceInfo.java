package com.wlcxbj.bike.bean;

/**
 * Created by Administrator on 2016/11/10.
 */

/**
 * 设备字段信息
 */
public class DeviceInfo {

    private String brand;
    private String color;
    private String ctime;
    private String groupid;
    private String grouptype;
    private String id;
    private String imeino;
    private String imsino;
    private String install_time;
    private String installer;
    private String is_online;
    private String owner;
    private String owner_mobile;
    private String plateno;
    private String realsimno;
    private String selfno;
    private String seller;
    private String serialno;
    private String service_stop_time;
    private String simno;
    private String terminal_type;
    private String utime;
    private String vender;

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getCtime() {
        return ctime;
    }

    public String getGroupid() {
        return groupid;
    }

    public String getGrouptype() {
        return grouptype;
    }

    public String getId() {
        return id;
    }

    public String getImeino() {
        return imeino;
    }

    public String getImsino() {
        return imsino;
    }

    public String getInstall_time() {
        return install_time;
    }

    public String getInstaller() {
        return installer;
    }

    public String getIs_online() {
        return is_online;
    }

    public String getOwner() {
        return owner;
    }

    public String getOwner_mobile() {
        return owner_mobile;
    }

    public String getPlateno() {
        return plateno;
    }

    public String getRealsimno() {
        return realsimno;
    }

    public String getSelfno() {
        return selfno;
    }

    public String getSeller() {
        return seller;
    }

    public String getSerialno() {
        return serialno;
    }

    public String getService_stop_time() {
        return service_stop_time;
    }

    public String getSimno() {
        return simno;
    }

    public String getTerminal_type() {
        return terminal_type;
    }

    public String getUtime() {
        return utime;
    }

    public String getVender() {
        return vender;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                ", ctime='" + ctime + '\'' +
                ", groupid=" + groupid +
                ", grouptype='" + grouptype + '\'' +
                ", id=" + id +
                ", imeino='" + imeino + '\'' +
                ", imsino='" + imsino + '\'' +
                ", install_time='" + install_time + '\'' +
                ", installer='" + installer + '\'' +
                ", is_online=" + is_online +
                ", owner='" + owner + '\'' +
                ", owner_mobile='" + owner_mobile + '\'' +
                ", plateno=" + plateno +
                ", realsimno='" + realsimno + '\'' +
                ", selfno=" + selfno +
                ", seller='" + seller + '\'' +
                ", serialno='" + serialno + '\'' +
                ", service_stop_time='" + service_stop_time + '\'' +
                ", simno='" + simno + '\'' +
                ", terminal_type=" + terminal_type +
                ", utime='" + utime + '\'' +
                ", vender='" + vender + '\'' +
                '}';
    }
}
