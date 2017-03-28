package com.wlcxbj.bike.bean.history;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/16.
 */

public class TransactionListToken {
    /*
     * {"errcode":0,"errmsg":"OK","transactions":
     * [{"id":10000000038,"orderTypeSpid":1,"amount":0.01,"intoAccountTime":1489576170000,"payTypeSpid":1},
     * {"id":10000000039,"orderTypeSpid":1,"amount":0.01,"intoAccountTime":1489576209000,"payTypeSpid":2},
     * {"id":10000000040,"orderTypeSpid":1,"amount":1.00,"intoAccountTime":1489576431000,"payTypeSpid":2}]}
     */
    private int errcode;
    private String errmsg;
    private ArrayList<TransactionBean> transactions;

    @Override
    public String toString() {
        return "TransactionListToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", transactions=" + transactions +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public ArrayList<TransactionBean> getTransactions() {
        return transactions;
    }
}
