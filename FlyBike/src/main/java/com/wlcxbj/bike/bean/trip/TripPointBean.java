package com.wlcxbj.bike.bean.trip;

/**
 * Created by Administrator on 2017/2/14.
 */
public class TripPointBean {
//    tid			string	锁的编号通过扫码二维码获取或后台返回
//    bikelng		string	车辆所在位置经度		保留字段，暂时不填
//    bikelat		string 	车辆所在位置维度		保留字段，暂时不填
//    userlng		string	用户所在位置经度
//    userlat		string 	用户所在位置维度

    private String tid;
    private String bikelng;
    private String bikelat;
    private String userlng;
    private String userlat;

    public TripPointBean(String tid, String bikelng, String bikelat, String userlng, String
            userlat) {
        this.tid = tid;
        this.bikelng = bikelng;
        this.bikelat = bikelat;
        this.userlng = userlng;
        this.userlat = userlat;
    }

    @Override
    public String toString() {
        return "TripPointBean{" +
                "tid='" + tid + '\'' +
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
