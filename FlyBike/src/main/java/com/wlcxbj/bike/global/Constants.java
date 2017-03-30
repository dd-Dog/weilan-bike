package com.wlcxbj.bike.global;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2016/11/9.
 */
public class Constants {

    public static final String WECHAT_PAY_APP_ID = "wx5abf010f3a6deb84";

    public static final boolean ON_LINE_MODE = true;
    public static final String BLUETOOTH_PAIR_SUCCESS = "bluetooth_pair_success";
    public static final String UNBLOCK_BLE_ENABLED = "unblock_ble_enabled";
    public static final String GRANT_TYPE = "password";
    public static final String AUTH_BASE_VALUE = "appClient:secret";
    public static final String AUTH_BASE_KEY = "Authorization";
    public static final String CHANNEL = "ANDROID";
    public static final String API_SERVER_TOKEN_TYPE = "Bearer";
    public static final String CURRENT_RESERVATION_TOKEN_FILE = "current_reservation_token_file";
    public static final String STS_ACCESS_FILE_READ = "sts_access_file_read";
    public static final String STS_ACCESS_FILE_WRITE = "sts_access_file_write";

    /**
     * URL地址
     */
    public static final String BASE_URL2 = "http://" + ShareBikeApplication.ip + ":" +
            ShareBikeApplication.port;
    //    public static final String BASE_URL2 = "http://59.110.160.218:8081";
    public static final String AUTH_SERVER = BASE_URL2 + "/oauth";
    public static final String API_SERVER = BASE_URL2 + "/api";
    public static final String SMS_CODE = API_SERVER + "/pub/smscode";
    public static final String LOGIN_SERVER = AUTH_SERVER + "/token";
    public static final String URL_ACCOUNT_INFO = API_SERVER + "/user/account";
    public static final String REAL_USER_INFO_AUTH = API_SERVER + "/user/realinfo/auth";
    public static final String ICON_URL_AT_OSS = API_SERVER + "/user/update/avatar";
    public static final String WRITE_ACCESS_AT_OSS = API_SERVER + "/biz/aliyun/oss/sts";
    public static final String WECHAT_ORDER = API_SERVER + "/pay/wx/placeorder/";
    public static final String ALI_PAY = API_SERVER + "/pay/zfb/placeorder";
    public static final String UPDATE_USER_INFO_URL = API_SERVER + "/user/update/basicinfo";
    public static final String URL_BASIC_ACCOUNT_INFO = API_SERVER + "/user/update/basicinfo/";
    public static final String ACCOUNT_BALANCE_URL = API_SERVER + "/user/account/account";
    public static final String ACCOUNT_REAL_INFO_URL = API_SERVER + "/user/account/realinfo";
    public static final String REQUEST_INVICECODE_URL = API_SERVER + "/user/query/invitecode";
    public static final String MODIFY_INVICECODE_URL = API_SERVER + "/user/modify/invitenote";
    public static final String COUPONS_LIST = API_SERVER + "/biz/coupons";
    public static final String EXCHANGE_COUPON = API_SERVER + "/biz/coupon/exchange";
    public static final String REQUEST_BIKE_PSW = API_SERVER + "/biz/query/unlockcode";
    public static final String RESERVE_BIKE = API_SERVER + "/biz/reserve";
    public static final String TRIP_START_TIMING = API_SERVER + "/biz/trip/start";
    public static final String TRIP_STOP_TIMING = API_SERVER + "/biz/trip/end";
    public static final String RENT_HISTORY_LIST = API_SERVER + "/biz/rent/list";
    public static final String RENT_HISTORY_DETAIL = API_SERVER + "/biz/rent/detail";
    public static final String RECHARGE_HISOTRY_LIST = API_SERVER + "/user/pay/recharge/list";
    public static final String DEPOSIT_HISTORY_LIST = API_SERVER + "/user/pay/yj/list";
    public static final String COMMON_ADDRESS_LIST = API_SERVER + "/user/list/freq/addr";
    public static final String SAVE_COMMON_ADDRESS = API_SERVER + "/user/save/freq/addr";
    public static final String CANCLE_RESERVATION = API_SERVER + "/biz/cancel/reservation";
    public static final String REPORT_PROBLEM = API_SERVER + "/user/report/issue";
    public static final String TRANSACTION_LIST = API_SERVER + "/biz/transaction/list";
    public static final String ACCOUNT_BUSINESS_PARAMS_URL = API_SERVER + "/biz/bizparam";
    public static final String DELETE_COMMON_ADDRESS = API_SERVER + "/user/delete/freq/addr";
    public static final String REFUND_YJ_BACK = API_SERVER + "/pay/refund/yj";
    public static final String BIKE_PSW2 = API_SERVER + "/biz/unlock/query";

    /**
     * 问题报告类型
     */
    public static final int ISSUE_TYPE_I = 1;
    public static final int ISSUE_TYPE_II = 2;
    public static final int ISSUE_TYPE_III = 3;


    /**
     * oss
     */
    public static final String ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";
    public static final String ENDPOINT_ONLY = "oss-cn-beijing.aliyuncs.com";
    //    public static final String BUCKET = "commonataayun";
    public static final String BUCKET = "vbike";
    public static final String USER_ICON_PREFIX = "_";
    public static final int READ = 1;
    public static final int WRITE = 2;


    public static final String PREFERENCES_NAME = "config";
    public static final String SAVEPWD_ENABLED = "savepwd_enabled";
    public static final String SAVED_PASSWD = "saved_passwd";
    public static final String SAVED_USERNAME = "saved_username";
    public static final String AUTOLOGIN_ENABLED = "autologin_enabled";
    public static final String UID = "uid";
    public static final String USER_NICK_NAME = "user_nick_name";
    public static final String USER_FUND_BACK = "user_fund_back";

    public static final String MY_BIKE_UNDERUSED = "my_bike_underused";

    public static final int CONNECT_TIME_OUT = 20;
    public static final String SEARCH_HISTORY = "search_history";
    public static final CharSequence ALI_PAY_SUCCESS = "9000";
    public static final String OSS_AVATAR_PATH = "avatar/";
    public static final String OSS_FEEDBACK_FILE_PATH = "feedback/";
    public static final String BUSINESS_PARAMS_FILE = "business_params_file";
    public static String AUTH_CACHE_FILE_NAME = "auth_cache_file";
    public static String ACCOUNT_TOKEN_CACHE_FILE_NAME = "account_token_cache_file_name";

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context
                .TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }

    public static int getVerionCode(Context context) {
        int version = 0;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                    .versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
