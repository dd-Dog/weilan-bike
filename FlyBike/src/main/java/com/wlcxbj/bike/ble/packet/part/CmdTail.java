package com.wlcxbj.bike.ble.packet.part;

import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/4.
 */
public class CmdTail implements Serializable {
    private static final String TAG = "CmdTail";
    public static final int LENGTH = 4;
    /**
     * check_sum 2个字节。用于校验数据完整性，整体命令（不含check_sum本身）未加密之前的累加和校验，结果只用2个字节保存，溢出则舍弃
      cmd_tail 2个字节，固定为 0x86,0xEF
     */
    private byte[] check_sum;
    private byte[] cmd_tail;
    {
        cmd_tail = new byte[] {(byte) 0x86, (byte) 0xEF};
        Log.e(TAG, "cmd_tail=" + Arrays.toString(cmd_tail));
    }

    public CmdTail(byte[] decryptedTailBytes) {
        check_sum = new byte[2];
        cmd_tail = new byte[2];
        System.arraycopy(decryptedTailBytes, 0, check_sum, 0, 2);
        System.arraycopy(decryptedTailBytes, 2, cmd_tail, 0, 2);
    }

    /**
     * 获取明文字节数组
     * @return
     */
    public byte[] getBytes() {
        byte[] bytes = new byte[LENGTH];
        bytes[0] = check_sum[0];
        bytes[1] = check_sum[1];
        bytes[2] = cmd_tail[0];
        bytes[3] = cmd_tail[1];
        return bytes;
    }

    public CmdTail() {
    }

    public byte[] getCheck_sum() {
        return check_sum;
    }

    public void setCheck_sum(byte[] check_sum) {
        this.check_sum = check_sum;
        Log.e(TAG, "设置累加校验和");
    }

    public byte[] getCmd_tail() {
        return cmd_tail;
    }

    public void setCmd_tail(byte[] cmd_tail) {
        this.cmd_tail = cmd_tail;
    }

    @Override
    public String toString() {
        return "CmdTail{" +
                "check_sum=" + Arrays.toString(check_sum) +
                ", cmd_tail=" + Arrays.toString(cmd_tail) +
                '}';
    }
}
