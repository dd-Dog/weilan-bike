package com.wlcxbj.bike.util.bike;

/**
 * Created by Administrator on 2017/2/17.
 */
public class BikePwdUtil {

    public static byte[] decoce(int code) {
        byte[] decodeArr = new byte[4];
        int codeValue = code * 1234;
        for (int i = 0; i < 4; i++) {
            int c = codeValue % 4 + 1;
            switch (c) {
                case 1:
                    decodeArr[i] = 0x01;
                    break;
                case 2:
                    decodeArr[i] = 0x02;
                    break;
                case 3:
                    decodeArr[i] = 0x03;
                    break;
                case 4:
                    decodeArr[i] = 0x04;
                    break;
            }
            codeValue /= 4;
        }
        return decodeArr;
    }
}
