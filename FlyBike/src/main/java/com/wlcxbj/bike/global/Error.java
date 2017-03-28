package com.wlcxbj.bike.global;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/16.
 */
public class Error {

    public static final int OK = 0;
    /**
     * 手机号码格式错误
     */
    public static final int ERRCODE_MALFORMAT_MOBILE = 1000;

    /**
     * 短信验证码错误
     */
    public static final int ERRCODE_INVALID_SMSCODE = 1001;

    /**
     * 短信验证码发送失败
     */
    public static final int ERRCODE_FAILED_TO_SMSCODE = 1002;

    /**
     * 用户基本更新失败
     */
    public static final int ERRCODE_FAILED_TO_UPDATE_USER_BASIC_INFO = 1003;

    /**
     * 用户邀请码只能修改一次
     */
    public static final int ERRCODE_INVITE_NOTE_ALREADY_MODIFIED = 1004;

    /**
     * 用户邀请码重复
     */
    public static final int ERRCODE_INVITE_NOTE_IS_USED = 1005;

    /**
     * 设备编号无效
     */
    public static final int ERRCODE_INVALID_TERMINAL_ID = 3001;

    /**
     * 优惠券兑换失败
     */
    public static final int ERRCODE_FAILED_TO_EXCHANGE_COUPON = 3002;

    /**
     * 订单创建失败
     */
    public static final int ERRCODE_FAILED_TO_CREATE_ORDER = 3003;

    /**
     * 车辆被预定
     */
    public static final int ERRCODE_BIKE_WAS_RESERVED = 3004;

    /**
     * 余额不足
     */
    public static final int ERRCODE_INSUFFICIENT_BALANCE = 3005;

    /**
     * 获取阿里云 OSS STS TOKEN 失败
     */
    public static final int ERRCODE_FAILED_TO_GET_ALI_OSS_STS = 3006;

    /**
     * 订单类型错误
     */
    public static final int ERRCODE_UNKNOWN_ORDER_TYPE = 3007;

    /**
     * 支付宝签名失败
     */
    public static final int ERRCODE_ALIPAY_FAILED_TO_SIGN = 3008;

    /**
     * 常用地址ID无效
     */
    public static final int ERRCODE_INVALIDE_FREQ_ADDR_ID = 3009;

    /**
     * 常用地址数量达到上限
     */
    public static final int ERRCODE_FREQ_ADDR_REACH_MAX = 3010;

    /**
     * bike in use
     */
    public static final int ERRCODE_FREQ_BIKE_IN_USE = 3031;
    private static final String UNRECOGNIZED_ERROR = "出现了未知错误~";


    public static HashMap<Integer, String> errorList = new HashMap<>();
    static {
        errorList.put(1000, "手机号码格式错误");
        errorList.put(1001, "短信验证码错误");
        errorList.put(1002, "短信验证码发送失败");
        errorList.put(1003, "用户基本信息更新失败");
        errorList.put(1004, "车辆被预定");
        errorList.put(1005, "用户邀请码重复");
        errorList.put(3000, "用户邀请码重复");
        errorList.put(3001, "设备编号无效");
        errorList.put(3002, "优惠券兑换失败");
        errorList.put(3003, "订单创建失败");
        errorList.put(3004, "车辆被预定");
        errorList.put(3005, "余额不足");
        errorList.put(3006, "获取阿里云 OSS STS TOKEN 失败");
        errorList.put(3007, "订单类型错误");
        errorList.put(3008, "支付宝签名失败");
        errorList.put(3009, "常用地址ID无效");
        errorList.put(3010, "常用地址数量达到上限");
        errorList.put(3031, "车辆正在使用当中");
        errorList.put(3032, "余额不足");

    }
    public static String getErrorString(int errCode) {
        String errMsg = errorList.get(errCode);
        if (TextUtils.isEmpty(errMsg)) {
            return UNRECOGNIZED_ERROR;
        }
        return errMsg;
    }

}
