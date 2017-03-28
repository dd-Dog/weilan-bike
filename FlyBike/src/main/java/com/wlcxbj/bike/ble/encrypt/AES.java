package com.wlcxbj.bike.ble.encrypt;

/**
 * Created by Administrator on 2017/3/4.
 * 128-ecb
 */

import android.util.Log;
import com.wlcxbj.bike.NativeUtils;
import javax.crypto.*;
import javax.crypto.spec.*;

public class AES {

    private static final String TAG = "AES";
    private static final byte[] KEY_BYTES = {0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77,
            (byte) 0x88, (byte) 0x99, (byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd, (byte)
            0xee, (byte) 0xff};
//    private static final String AES_KEY_STR = "M$NmHcUvJ0$y#3*M";

//    public static native String getAES_key();
    public static byte[] getKey() {
//        return getAES_key().getBytes();
        return NativeUtils.getAES_key().getBytes();
    }
    //需要加密的字串
    /**
     * 解密字符串
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey) throws Exception {
        try {
            //判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            //判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            //解密类型的cipher
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = hex2byte(sSrc);
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    /**
     * 解密字节数组
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] sSrc, byte[] sKey) {
        try {
            //判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            //判断Key是否为16位
            if (sKey.length != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey;
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            //解密类型的cipher
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            try {
                byte[] original = cipher.doFinal(sSrc);
                return original;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    /**
     * 加密字节数组,会自动补齐16字节
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] sSrc, byte[] sKey) {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        //判断Key是否为16位
        if (sKey.length != 16) {
            System.out.print("Key长度不是16位");//
            return null;
        }
        try {
            byte[] raw = sKey;
            Log.e(TAG, "加密字节数据的长度＝" + raw.length);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            //加密类型的cipher
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            //doFinal进行加密或解密，根据对象的初始化类型
            byte[] encrypted = cipher.doFinal(sSrc);
            return encrypted;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密字符串
     *
     * @param sSrc 明文字符串
     * @param sKey 密钥字符串
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        //判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");//
            return null;
        }
        byte[] raw = sKey.getBytes("ASCII");
        Log.e(TAG, "加密字节数据的长度＝" + raw.length);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //加密类型的cipher
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        //doFinal进行加密或解密，根据对象的初始化类型
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());
//        return byte2hex(encrypted).toLowerCase();
        return byte2hex(encrypted);
    }

    /**
     * 十六进制字符串转数组
     *
     * @param strhex
     * @return
     */
    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int len = strhex.length();
        if (len % 2 == 1) {
            return null;
        }
        byte[] b = new byte[len / 2];
        for (int i = 0; i != len / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    /**
     * 字节转十六进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            hs += " ";
        }
//        Log.e(TAG, hs);
        return hs.toUpperCase();
    }

    /**
     * 字节转十六进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2hexNoSeprator(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
//        Log.e(TAG, hs);
        return hs.toUpperCase();
    }
}
