package com.wlcxbj.bike.bean.account;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/17.
 */

public class BussinessParams implements Serializable{
    private String name;
    private String value;
    private String note;

    @Override
    public String toString() {
        return "BussinessParams{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", note='" + note + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    /*
    {
      "name": "BALANCE_LOWER_BOUND",
      "value": "0.0",
      "note": "账户余额下限，低于该值，则无法租车"
    }
     */
}
