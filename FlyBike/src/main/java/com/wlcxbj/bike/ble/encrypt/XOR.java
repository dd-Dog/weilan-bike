package com.wlcxbj.bike.ble.encrypt;

/**
 * Created by Administrator on 2017/3/6.
 */
public class XOR {

    /**
     * 加密
     * @param plain
     * @param randomKey
     * @return
     */
    public static byte[] encryptWithRandom(byte[] plain, byte[] randomKey) {
        byte[] encrypted = new byte[plain.length];
        for (int i = 0; i < plain.length; i++) {
            encrypted[i] = (byte) (plain[i] ^ randomKey[i % 8]);
        }
        return encrypted;
    }

    /**
     * 解密
     * @param plain
     * @param randomKey
     * @return
     */
    public static byte[] decryptedWithRandom(byte[] plain, byte[] randomKey) {
        return encryptWithRandom(plain, randomKey);
    }
}
