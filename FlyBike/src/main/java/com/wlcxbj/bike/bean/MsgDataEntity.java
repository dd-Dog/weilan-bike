package com.wlcxbj.bike.bean;

/**
 * Created by bain on 17-1-17.
 */
public class MsgDataEntity {

    public boolean isRead;
    public String msgTitle;
    public String msgContent;

    public MsgDataEntity(boolean isRead, String msgTitle, String msgContent) {
        this.isRead = isRead;
        this.msgTitle = msgTitle;
        this.msgContent = msgContent;
    }
}
