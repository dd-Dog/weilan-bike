package com.wlcxbj.bike.bean.bike;

/**
 * Created by Administrator on 2017/2/17.
 */
public class BikePswBean {
    private String tno;
    private String tid;     //	锁的编号通过扫码二维码获取	与plateno二者必填其一
    private String plateno; //	车辆编号通过输入			与tid二者必填其一
    private String userlng; //	用户所在位置经度
    private String userlat; //	用户所在位置维度

    public BikePswBean(String tno, String plateno, String userlng, String userlat) {
        this.tno = tno;
        this.plateno = plateno;
        this.userlng = userlng;
        this.userlat = userlat;
    }

    @Override
    public String toString() {
        return "BikePswBean{" +
                "tno='" + tno + '\'' +
                ", tid='" + tid + '\'' +
                ", plateno='" + plateno + '\'' +
                ", userlng='" + userlng + '\'' +
                ", userlat='" + userlat + '\'' +
                '}';
    }

    public String getTno() {
        return tno;
    }

    public void setTno(String tno) {
        this.tno = tno;
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
