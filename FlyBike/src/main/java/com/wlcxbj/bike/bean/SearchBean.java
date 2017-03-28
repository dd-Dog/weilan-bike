package com.wlcxbj.bike.bean;

import java.io.Serializable;

/**
 * Created by bain on 16-11-25.
 */
public class SearchBean extends BaseBean implements Serializable{
    public int iconId;
    public String building;
    public String streets;

    public SearchBean() {
    }

    public SearchBean(int iconId, String building, String streets) {
        this.iconId = iconId;
        this.building = building;
        this.streets = streets;
    }
}
