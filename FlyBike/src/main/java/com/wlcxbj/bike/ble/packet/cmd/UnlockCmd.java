package com.wlcxbj.bike.ble.packet.cmd;

import android.util.Log;

import com.wlcxbj.bike.ble.packet.part.CmdBody;

/**
 * Created by Administrator on 2017/3/4.
 */
public class UnlockCmd extends Command {

    private static final String TAG = "UnlockCmd";


    public UnlockCmd(byte[] cmd) {
        super(cmd);
    }

    public UnlockCmd() {
    }

    /**
     * 设置报体
     */
    @Override
    public void setCmdBody() {
        Log.e(TAG, "设置CmdBody");
        //AES检测位
        this.cmdBody = new CmdBody(CMD_ID_UNLOCK, CMD_TYPE_USER, USER_TYPE_NORMAL,
                TRANS_TYPE_DOWN);
    }

    @Override
    public String toString() {
        return "UnlockCmd{" +
                "cmdHead=" + cmdHead +
                ", cmdBody=" + cmdBody +
                ", cmdData=" + cmdData +
                ", cmdTail=" + cmdTail +
                '}';
    }

}
