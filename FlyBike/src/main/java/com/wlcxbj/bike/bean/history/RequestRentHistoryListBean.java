package com.wlcxbj.bike.bean.history;

/**
 * Created by Administrator on 2017/2/14.
 */
public class RequestRentHistoryListBean {
    private String startTime;//开始日期 格式 yyyyMMddHHmmSS 选填
    private String endTime;//结束日期 格式 yyyyMMddHHmmSS选填
    private int pageNo;//第几页	选填
    private int pageSize;//每页大小   选填
    public RequestRentHistoryListBean() {
    }

    public RequestRentHistoryListBean(String startTime) {
        this.startTime = startTime;
    }

    public RequestRentHistoryListBean(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public RequestRentHistoryListBean(String startTime, String endTime, int pageNo, int pageSize) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "RequestRentHistoryListBean{" +
                "startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", pageNo='" + pageNo + '\'' +
                ", pageSize='" + pageSize + '\'' +
                '}';
    }
}
