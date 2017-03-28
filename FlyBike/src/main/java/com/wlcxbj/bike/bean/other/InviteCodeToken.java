package com.wlcxbj.bike.bean.other;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/13.
 */
public class InviteCodeToken implements Serializable {
    //{"errcode":0,"errmsg":"OK","inviteCode":"148619260995226643736","inviteNote":null}
    private int errcode;
    private String errmsg;
    private String inviteCode;
    private String inviteNote;

    @Override
    public String toString() {
        return "InviteCodeToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", inviteNote='" + inviteNote + '\'' +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public String getInviteNote() {
        return inviteNote;
    }
}
