package com.wlcxbj.bike.global;

import android.util.Log;

/**
 * Created by Administrator on 2017/3/8.
 */
public class BLEError {
    private static final String TAG = "BLEError";
    public static final int PACKAGE_NULL = 1001;
    public static final int PACKAGE_HEAD_NOT_FOUND = 1002;
    public static final int ENCRYPT_TYPE_ERROR = 1003;
    public static final int PKG_SORT_ERROR = 1004;
    public static final int DATA_LENGTH_ERROR = 1005;
    public static final int RES_AES_ENC_DATA_ERROR = 1006;
    public static final int AES_DECDATA_PADDING_ERROR = 1007;
    public static final int AES_DEC_ERROR = 1008;
    public static final int ERROR_WHEN_PARSE_PKG = 1009;
    public static final int PKG_LOST = 1010;
    public static final int CHECK_SUM_ERROR = 1011;


    //ERROR CODE
    public static final int ERROR_NONE = 0;
    public static final int ERROR_INVAL_AES = -1;
    public static final int ERROR_CMD_CHECK_FAILUR = -2;
    public static final int ERROR_MALLOC_FAILURE = -3;
    public static final int ERROR_SND_DATA_FAILURE = -4;
    public static final int ERROR_CMD_TAIL_FAILURE = -5;
    public static final int ERROR_CMD_CHEECKSUM_FAILURE = -6;
    public static final int ERROR_CMD_TRANS_TYPE_FAILURE = -7;
    public static final int ERROR_INVAL_AES_ACK = -8;
    public static final int ERROR_INVAL_PWD = -9;
    public static final int ERROR_INVAL_ENCRYPT_TYPE = -10;
    public static final int ERROR_CMD_LEN = -11;
    public static final int STATE_UNLOCK = 1;
    public static final int STATE_LOCK = 2;

    public static void error(int errCode) {
        Log.e(TAG, "errcode=" + errCode);
    }
}
