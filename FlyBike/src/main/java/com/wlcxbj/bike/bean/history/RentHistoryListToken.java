package com.wlcxbj.bike.bean.history;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/2/14.
 */
public class RentHistoryListToken {

    /**
     * {"errcode":0,
     * "errmsg":"OK",
     * "totalPages":2,
     * "pageNo":0,
     * "pageSize":0,
     * "rents":[
     *
     */
    private int errcode;
    private String errmsg;
    private int totalPages;
    private int pageNo;
    private int pageSize;
    private ArrayList<RentBikeBean> rents;

    @Override
    public String toString() {
        return "RentHistoryListToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", totalPages=" + totalPages +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", rents=" + rents +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public ArrayList<RentBikeBean> getRents() {
        return rents;
    }
}
