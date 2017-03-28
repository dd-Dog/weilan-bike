package com.wlcxbj.bike.ble.packet.part;

import android.util.Log;

import com.wlcxbj.bike.ble.encrypt.AES;
import com.wlcxbj.bike.ble.encrypt.SQRT98;
import com.wlcxbj.bike.ble.packet.cmd.Command;

import java.io.Serializable;
import java.util.Arrays;


/**
 * Created by Administrator on 2017/3/4.
 */
public class CmdBody implements Serializable {
    private static final String TAG = "CmdBody";
    public static final int LENGTH = 8;
    private byte cmd_id;
    private byte cmd_type;
    private byte user_type;
    private byte trans_type;
    private byte[] cmd_check = new byte[]{0x12, 0x34};   //2个字节
    private byte[] reserve_data = new byte[]{0, 0};

    public CmdBody(byte[] decryptedBodyBytes) {
        this.cmd_id = decryptedBodyBytes[0];
        this.cmd_type = decryptedBodyBytes[1];
        this.user_type = decryptedBodyBytes[2];
        this.trans_type = decryptedBodyBytes[3];
        cmd_check = new byte[2];
        System.arraycopy(decryptedBodyBytes, 4, cmd_check, 0, 2);
    }


    /**
     * 获取明文字节数组
     *
     * @return
     */
    public byte[] getBytes() {
        byte[] bytes = new byte[LENGTH];
        bytes[0] = cmd_id;
        bytes[1] = cmd_type;
        bytes[2] = user_type;
        bytes[3] = trans_type;
        bytes[4] = cmd_check[0];
        bytes[5] = cmd_check[1];
        bytes[6] = reserve_data[0];
        bytes[7] = reserve_data[1];
        Log.e(TAG, "CMD_BODY=" + Arrays.toString(bytes));
        return bytes;
    }

    /**
     * 获取加密后的字节数组
     *
     * @return
     */
    public byte[] getEncryptedBytes(int type) {
        Log.e(TAG, "BODY加密方式为：" + type);
        byte[] bytes = getBytes();
        if (type == Command.ENRYPT_TYPE_AES) {
            return AES.encrypt(bytes, AES.getKey());
        } else if (type == Command.ENRYPT_TYPE_XOR) {
            //TODO-添加RSA加密算法
            return null;
        } else if (type == Command.ENRYPT_TYPE_SQRT98) {
            byte[] encryptedBytes = SQRT98.encrypt(bytes);
            return encryptedBytes;
        } else {
            return AES.encrypt(bytes, AES.getKey());
        }
    }

    /**
     * 获取解密后的字节数组
     *
     * @return
     */
    public byte[] getDecryptedBytes(int type) {
        Log.e(TAG, "BODY加密方式为：" + type);
        byte[] bytes = getBytes();
        if (type == Command.ENRYPT_TYPE_AES) {
            return AES.decrypt(bytes, AES.getKey());
        } else if (type == Command.ENRYPT_TYPE_XOR) {
            //TODO-添加RSA加密算法
            return null;
        } else if (type == Command.ENRYPT_TYPE_SQRT98) {
            byte[] encryptedBytes = SQRT98.decrypt(bytes);
            return encryptedBytes;
        } else {
            return AES.encrypt(bytes, AES.getKey());
        }
    }


    public CmdBody(byte cmd_id, byte cmd_type, byte user_type, byte trans_type) {
        this.cmd_id = cmd_id;
        this.cmd_type = cmd_type;
        this.user_type = user_type;
        this.trans_type = trans_type;
    }

    @Override
    public String toString() {
        return "CmdBody{" +
                "cmd_id=" + cmd_id +
                ", cmd_type=" + cmd_type +
                ", user_type=" + user_type +
                ", trans_type=" + trans_type +
                ", cmd_check=" + Arrays.toString(cmd_check) +
                ", reserve_data=" + Arrays.toString(reserve_data) +
                '}';
    }

    public static String getTAG() {
        return TAG;
    }

    public static int getLENGTH() {
        return LENGTH;
    }

    public byte getCmd_id() {
        return cmd_id;
    }

    public void setCmd_id(byte cmd_id) {
        this.cmd_id = cmd_id;
    }

    public byte getCmd_type() {
        return cmd_type;
    }

    public void setCmd_type(byte cmd_type) {
        this.cmd_type = cmd_type;
    }

    public byte getUser_type() {
        return user_type;
    }

    public void setUser_type(byte user_type) {
        this.user_type = user_type;
    }

    public byte getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(byte trans_type) {
        this.trans_type = trans_type;
    }

    public byte[] getCmd_check() {
        return cmd_check;
    }

    public void setCmd_check(byte[] cmd_check) {
        this.cmd_check = cmd_check;
    }

    public byte[] getReserve_data() {
        return reserve_data;
    }

    public void setReserve_data(byte[] reserve_data) {
        this.reserve_data = reserve_data;
    }
}
