package com.wlcxbj.bike.bean;

/**
 * Created by Administrator on 2016/11/10.
 * 位置信息类
 */
public class DeviceStateInfo {


    private String celladdress;
    private String cellc;
    private String cstatus;
    private String ctime;
    private String direct;
    private String electricqua;
    private String gpstime;
    private String id;
    private String isvalid;
    private String lac;
    private String lat;
    private String latstr;
    private String lng;
    private String lngstr;
    private String mcc;
    private String mnc;
    private String plateno;
    private String precision;
    private String signalstren;
    private String simno;
    private String speed;
    private String starnum;
    private String state;
    private String updatetime;

    public String getCelladdress() {
        return celladdress;
    }

    public String getCellc() {
        return cellc;
    }

    public String getCstatus() {
        return cstatus;
    }

    public String getCtime() {
        return ctime;
    }

    public String getDirect() {
        return direct;
    }

    public String getElectricqua() {
        return electricqua;
    }

    public String getGpstime() {
        return gpstime;
    }

    public String getId() {
        return id;
    }

    public String getIsvalid() {
        return isvalid;
    }

    public String getLac() {
        return lac;
    }

    public String getLat() {
        return lat;
    }

    public String getLatstr() {
        return latstr;
    }

    public String getLng() {
        return lng;
    }

    public String getLngstr() {
        return lngstr;
    }

    public String getMcc() {
        return mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public String getPlateno() {
        return plateno;
    }

    public String getPrecision() {
        return precision;
    }

    public String getSignalstren() {
        return signalstren;
    }

    public String getSimno() {
        return simno;
    }

    public String getSpeed() {
        return speed;
    }

    public String getStarnum() {
        return starnum;
    }

    public String getState() {
        return state;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    @Override
    public String toString() {
        return "DeviceStateInfo{" +
                "celladdress='" + celladdress + '\'' +
                ", cellc='" + cellc + '\'' +
                ", cstatus='" + cstatus + '\'' +
                ", ctime='" + ctime + '\'' +
                ", direct='" + direct + '\'' +
                ", electricqua='" + electricqua + '\'' +
                ", gpstime='" + gpstime + '\'' +
                ", id='" + id + '\'' +
                ", isvalid='" + isvalid + '\'' +
                ", lac='" + lac + '\'' +
                ", lat='" + lat + '\'' +
                ", latstr='" + latstr + '\'' +
                ", lng='" + lng + '\'' +
                ", lngstr='" + lngstr + '\'' +
                ", mcc='" + mcc + '\'' +
                ", mnc='" + mnc + '\'' +
                ", plateno='" + plateno + '\'' +
                ", precision='" + precision + '\'' +
                ", signalstren='" + signalstren + '\'' +
                ", simno='" + simno + '\'' +
                ", speed='" + speed + '\'' +
                ", starnum='" + starnum + '\'' +
                ", state='" + state + '\'' +
                ", updatetime='" + updatetime + '\'' +
                '}';
    }
}
