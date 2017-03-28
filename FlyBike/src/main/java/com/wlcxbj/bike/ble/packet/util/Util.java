package com.wlcxbj.bike.ble.packet.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2017/3/4.
 */
public class Util {
    private static final String TAG = "Util";

    /**
     * 根据系统时间生成8个字节的随机数，阻止重放攻击
     *
     * @return
     */
    public static byte[] createRandomData() {
        Random random = new Random();
        byte[] data = new byte[8];
        random.nextBytes(data);
        return data;
    }

    /**
     * 分解数据包成20字节的
     *
     * @param arr
     * @return
     */
    public static ArrayList<byte[]> splitToByteArr(byte[] arr) {
        ArrayList<byte[]> bytesArrList = new ArrayList<>();
        int len = arr.length;
        int packetCount = (len % 18 == 0) ? len / 18 : len / 18 + 1;
        for (int i = 0; i < packetCount; i++) {
            byte[] bytes = new byte[20];
            bytes[0] = (byte) packetCount;
            bytes[1] = (byte) i;
            if (i == (packetCount - 1)) {
                System.arraycopy(arr, i * 18, bytes, 2, len % 18);
                //不足18字节填充0
                for (int j = len % 18 + 2; j < 20; j++) {
                    bytes[j] = 0;
                }
            } else {
                System.arraycopy(arr, i * 18, bytes, 2, 18);
            }
            bytesArrList.add(bytes);
        }
        Log.e(TAG, "发送的数据包个数：" + bytesArrList.size());
        return bytesArrList;
    }

    /**
     * 字节数组拷贝
     *
     * @param src
     * @param srcPos
     * @param des
     * @param desPos
     * @param length
     */
    public static void arrayCopy(byte[] src, int srcPos, byte[] des, int desPos, int length) {
        if (length < 0) {
            LogUtil.e(Util.class, "拷贝长度应该大于0");
            return;
        }
        if (srcPos >= src.length) {
            LogUtil.e(Util.class, "拷贝目标数组的起始位置不能超过最大数组长度");
            return;
        }
        if (length > src.length - srcPos) {
            LogUtil.e(Util.class, "拷贝源数组的没有足够的数据长度");
            return;
        }
        if (desPos >= des.length) {
            LogUtil.e(Util.class, "拷贝目标数组的起始位置不能超过最大数组长度");
            return;
        }
        if (length > des.length - desPos) {
            LogUtil.e(Util.class, "拷贝目标数组的没有足够的空间");
            return;
        }
        System.arraycopy(src, srcPos, des, desPos, length);
    }

    public static int getPositiveValue(byte b) {
        int value = b & 0xff;
        return value;
    }

    /**
     * 计算字节数组所表示的数量
     * @param times
     * @return
     */
    public static long getNumberFromBytes(byte[] times) {
        Log.e(TAG, "要转成正数的字节数组长度＝" + times.length);
        long number = 0;
        for (int i = 0; i < times.length; i++) {
            number += getPositiveValue(times[i]) * Math.pow(256, i);
        }
        return number;
    }
}
