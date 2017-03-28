package com.wlcxbj.bike.bean.commonaddress;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/14.
 */
public class CommonAddressBean implements Serializable {

    private long id	;       //  地址ID，可以为空，如果为空，则后台创建一条新记录,如果不为空，则更新id对应的地址信息
    private String lng;     //  地址经度，例如： 116.123456
    private String lat;     //  地址维度，例如： 40.123456
    private String addr;    //  地址

    public CommonAddressBean(long id, String lng, String lat, String addr) {
        this.id = id;
        this.lng = lng;
        this.lat = lat;
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "CommonAddressBean{" +
                "id=" + id +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
