package com.wlcxbj.bike.ble.packet.cmd;

import android.util.Log;

import com.wlcxbj.bike.ble.packet.part.CmdBody;

/**
 * Created by Administrator on 2017/3/10.
 */
public class LocateCmd extends Command {

    private static final String TAG = "LocateCmd";

    public LocateCmd(byte[] cmd) {
        super(cmd);
    }

    @Override
    protected void setCmdBody() {
        Log.e(TAG, "设置CmdBody");
        //AES检测位
        this.cmdBody = new CmdBody(CMD_ID_LOCATE, CMD_TYPE_USER, USER_TYPE_NORMAL,
                TRANS_TYPE_DOWN);
    }

}
