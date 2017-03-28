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
public class CmdData implements Serializable {
    private static final String TAG = "CmdData";
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public CmdData() {
    }

    public CmdData(byte[] data) {
        this.data = data;
    }

    /**
     * 获取明文字节数组
     *
     * @return
     */
    public byte[] getBytes() {
        return data;
    }

    /**
     * 获取加密后的字节数组
     *
     * @param type
     * @return
     */
    public byte[] getEncryptedBytes(int type) {
        Log.e(TAG, "Data加密方式为：" + type);
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
            byte[] encryptedBytes = SQRT98.encrypt(bytes);
            return encryptedBytes;
        } else {
            return AES.encrypt(bytes, AES.getKey());
        }
    }

    @Override
    public String toString() {
        return "CmdData{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}
