package com.wlcxbj.bike.bean.bike;

/**
 * Created by Administrator on 2017/2/14.
 */
public class BikeBean {
    private String tid;        //锁的编号通过扫码二维码获取	与plateno二者必填其一
    private String plateno;    //车辆编号通过输入			与tid二者必填其一
    private String  bikelng;   //车辆所在位置经度
    private String bikelat;    //车辆所在位置维度
    private String userlng;    //用户所在位置经度
    private String userlat;    //用户所在位置维度

    public BikeBean(String tid, String plateno, String bikelng, String bikelat, String userlng,
                    String userlat) {
        this.tid = tid;
        this.plateno = plateno;
        this.bikelng = bikelng;
        this.bikelat = bikelat;
        this.userlng = userlng;
        this.userlat = userlat;
    }

    @Override
    public String toString() {
        return "BikeBean{" +
                "tid='" + tid + '\'' +
                ", plateno='" + plateno + '\'' +
                ", bikelng='" + bikelng + '\'' +
                ", bikelat='" + bikelat + '\'' +
                ", userlng='" + userlng + '\'' +
                ", userlat='" + userlat + '\'' +
                '}';
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPlateno() {
        return plateno;
    }

    public void setPlateno(String plateno) {
        this.plateno = plateno;
    }

    public String getBikelng() {
        return bikelng;
    }

    public void setBikelng(String bikelng) {
        this.bikelng = bikelng;
    }

    public String getBikelat() {
        return bikelat;
    }

    public void setBikelat(String bikelat) {
        this.bikelat = bikelat;
    }

    public String getUserlng() {
        return userlng;
    }

    public void setUserlng(String userlng) {
        this.userlng = userlng;
    }

    public String getUserlat() {
        return userlat;
    }

    public void setUserlat(String userlat) {
        this.userlat = userlat;
    }
}
