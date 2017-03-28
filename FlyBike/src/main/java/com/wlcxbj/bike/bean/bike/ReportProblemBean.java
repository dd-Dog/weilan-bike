package com.wlcxbj.bike.bean.bike;

/**
 * Created by Administrator on 2017/3/13.
 */

public class ReportProblemBean {
    private int issueTypeSpid; // 问题类型，必填
    private String tid;		//锁的编号通过扫码二维码获取
    private String lng;		//string	报告者所在地址经度，例如： 116.123456
    private String lat;		 //报告者所在地址维度，例如： 40.123456pageSize
    private String title;	//string(64)	标题
    private String content;
    private String note;	//		string(256)	备注
    private String img1;	//		string(128)	相关图片的oss url
    private String img2;	//		string(128)	相关图片的oss url
    private String img3;	//		string(128)	相关图片的oss url

    @Override
    public String toString() {
        return "ReportProblemBean{" +
                "issueTypeSpid=" + issueTypeSpid +
                ", tid='" + tid + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", note='" + note + '\'' +
                ", img1='" + img1 + '\'' +
                ", img2='" + img2 + '\'' +
                ", img3='" + img3 + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIssueTypeSpid() {
        return issueTypeSpid;
    }

    public void setIssueTypeSpid(int issueTypeSpid) {
        this.issueTypeSpid = issueTypeSpid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }
}
