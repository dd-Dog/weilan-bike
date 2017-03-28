package com.wlcxbj.bike.bean;

import java.io.Serializable;

/**
 * Created by bain on 16-11-28.
 */
public class RouteBean extends BaseBean implements Serializable{
    public RouteBean(long startTime, long endTime, long time, String bikeID, int usingTime, float money) {
        this.time = time;
        this.bikeID = bikeID;
        this.usingTime = usingTime;
        this.money = money;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long time;
    public String bikeID;
    public int  usingTime;
    public float money;
    public long startTime;
    public long endTime;
}
