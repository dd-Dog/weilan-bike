package com.wlcxbj.bike.ble.encrypt;

import android.util.Log;


/**
 * Created by Administrator on 2017/3/4.
 */
public class SQRT98 {
    private static final String TAG = "SQRT98";
    //8个字节
    private static final String KEY_HEX_STR = "DBD01D1CB406BC06";
    private static byte[] key_hex_byte = new byte[]{(byte) 0xDB, (byte) 0xD0, 0X1D, 0X1C, (byte)
            0xB4, 0x06, (byte) 0xBC, 0x06};


    /**
     * 异或加密
     * @param plain
     * @return
     */
    public static byte[] encrypt(byte[] plain) {
        byte[] endcryptedBytes = new byte[plain.length];
        for (int i = 0; i < plain.length; i++) {
            endcryptedBytes[i] = (byte) (plain[i] ^ key_hex_byte[i % 8]);
        }
        return endcryptedBytes;
    }

    public static void print() {
        for (int i = 0; i < key_hex_byte.length; i++) {
            Log.e(TAG, AES.byte2hex(key_hex_byte) + "");
        }
    }

    /**
     * 异或解密
     * @param decrypt
     */
    public static byte[] decrypt(byte[] decrypt) {
        byte[] plain = new byte[decrypt.length];
        for (int i = 0; i < plain.length; i++) {
            plain[i] = (byte) (decrypt[i] ^ key_hex_byte[i % 8]);
        }
        return plain;

    }
}
