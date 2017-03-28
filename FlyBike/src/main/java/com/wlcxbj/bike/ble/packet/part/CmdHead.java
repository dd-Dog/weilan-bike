package com.wlcxbj.bike.ble.packet.part;

import com.wlcxbj.bike.ble.encrypt.SQRT98;
import com.wlcxbj.bike.ble.packet.cmd.Command;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/4.
 * <p>
 * 命令头字段定义类
 */
public class CmdHead implements Serializable {
    public static final int LENGTH = 13;
    private byte encrypt_type;
    private byte[] cmd_data_len;//2个字节长度
    private byte[] random_data;//8个字节长度
    private byte[] reserve_data = new byte[]{0, 0};//2个字节长度

    public CmdHead(byte[] headPlainBytes) {
        this.encrypt_type = headPlainBytes[0];
        cmd_data_len = new byte[2];
        random_data = new byte[8];
        System.arraycopy(headPlainBytes, 1, cmd_data_len, 0, 2);
        System.arraycopy(headPlainBytes, 3, random_data, 0, 8);
    }


    /**
     * 获取明文字节数组
     * @return
     */
    public byte[] getBytes() {
        byte[] bytes = new byte[LENGTH];
        bytes[0] = encrypt_type;
        bytes[1] = cmd_data_len[0];
        bytes[2] = cmd_data_len[1];
        for (int i = 0; i < random_data.length; i++) {
            bytes[3 + i] = random_data[i];
        }
        bytes[11] = reserve_data[0];
        bytes[12] = reserve_data[1];
        return bytes;
    }

    /**
     * 获取加密后的字节数组
     * 使用SQRT98异或加密
     *
     * @return
     */
    public byte[] getEncryptedBytes(int type) {
        byte[] bytes = getBytes();
        if (type == Command.ENRYPT_TYPE_SQRT98) {
            byte[] encryptedBytes = SQRT98.encrypt(bytes);
            return encryptedBytes;
        } else {
            byte[] encryptedBytes = SQRT98.encrypt(bytes);
            return encryptedBytes;
        }
    }


    /**
     * @param encrypt_type body,data,tail的加密类型
     * @param cmd_data_len data字段长度
     * @param random_data 随机数组8个字节长度
     * @param reserve_data 保留
     */
    public CmdHead(byte encrypt_type, byte[] cmd_data_len, byte[] random_data, byte[]
            reserve_data) {
        this.encrypt_type = encrypt_type;
        this.cmd_data_len = cmd_data_len;
        this.random_data = random_data;
        this.reserve_data = reserve_data;
    }

    @Override
    public String toString() {
        return "CmdHead{" +
                "encrypt_type=" + encrypt_type +
                ", cmd_data_len=" + Arrays.toString(cmd_data_len) +
                ", random_data=" + Arrays.toString(random_data) +
                ", reserve_data=" + Arrays.toString(reserve_data) +
                '}';
    }

    public static int getLENGTH() {
        return LENGTH;
    }

    public byte getEncrypt_type() {
        return encrypt_type;
    }

    public void setEncrypt_type(byte encrypt_type) {
        this.encrypt_type = encrypt_type;
    }

    public byte[] getCmd_data_len() {
        return cmd_data_len;
    }

    public void setCmd_data_len(byte[] cmd_data_len) {
        this.cmd_data_len = cmd_data_len;
    }

    public byte[] getRandom_data() {
        return random_data;
    }

    public void setRandom_data(byte[] random_data) {
        this.random_data = random_data;
    }

    public byte[] getReserve_data() {
        return reserve_data;
    }

    public void setReserve_data(byte[] reserve_data) {
        this.reserve_data = reserve_data;
    }
}
