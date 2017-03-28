package com.wlcxbj.bike.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.account.AccountInfo;
import com.wlcxbj.bike.bean.account.BalanceInfoToken;
import com.wlcxbj.bike.bean.pay.ALiPayToken;
import com.wlcxbj.bike.bean.pay.OrderBean;
import com.wlcxbj.bike.bean.pay.PayResult;
import com.wlcxbj.bike.bean.pay.WechatPayToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.global.Error;
import com.wlcxbj.bike.net.beanutil.HttpAccountBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.net.beanutil.HttpPayBeanUtil;
import com.wlcxbj.bike.thread.AliPayThread;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.ToastUtil;
import com.wlcxbj.bike.util.cache.CacheUtil;


/**
 * Created by bain on 16-11-28.
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RechargeActivity";
    private static final int RECHARGE_COUNT_100 = 100;
    private static final int RECHARGE_COUNT_50 = 50;
    private static final int RECHARGE_COUNT_10 = 10;
    private static final int RECHARGE_METHOD_ALIPAY = 100;
    private static final int RECHARGE_METHOD_WECHAT = 200;
    private int currentMethod = RECHARGE_METHOD_ALIPAY;
    private ImageView ivAlipay;
    private ImageView ivWeChat;
    private TextView tv100;
    private TextView tv50;
    private TextView tv10;
    private HttpPayBeanUtil httpPayBeanUtil;
    private IWXAPI api;
    private PayHandler payHandler;
    private TextView walletCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        httpPayBeanUtil = new HttpPayBeanUtil(this);
        //初始化微信支付API
        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_PAY_APP_ID);
        initView();
        payHandler = new PayHandler(this);
    }

    private void initView() {
        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.rl_wechat).setOnClickListener(this);
        findViewById(R.id.rl_alipay).setOnClickListener(this);
        findViewById(R.id.btn_recharge).setOnClickListener(this);
        ivWeChat = (ImageView) findViewById(R.id.iv_wechat);
        walletCount = (TextView) findViewById(R.id.wallet_count);
        ivAlipay = (ImageView) findViewById(R.id.iv_alipay);
        tv100 = (TextView) findViewById(R.id.tv_100);
        tv50 = (TextView) findViewById(R.id.tv_50);
        tv10 = (TextView) findViewById(R.id.tv_10);
        tv10.setOnClickListener(this);
        tv50.setOnClickListener(this);
        tv100.setOnClickListener(this);
        toggleCount(RECHARGE_COUNT_100);
        toggleMethod(RECHARGE_METHOD_WECHAT);
        if (mAccountToken != null) {
            walletCount.setText(mAccountToken.getAccount().getBalance());
        }
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_recharge);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.rl_alipay:
                toggleMethod(RECHARGE_METHOD_ALIPAY);
                break;
            case R.id.rl_wechat:
                toggleMethod(RECHARGE_METHOD_WECHAT);
                break;
            case R.id.tv_10:
                toggleCount(RECHARGE_COUNT_10);
                break;
            case R.id.tv_50:
                toggleCount(RECHARGE_COUNT_50);
                break;
            case R.id.tv_100:
                toggleCount(RECHARGE_COUNT_100);
                break;
            case R.id.btn_recharge:
                if (currentMethod == RECHARGE_METHOD_ALIPAY) {
                    payByAli();
                } else if (currentMethod == RECHARGE_METHOD_WECHAT) {
                    payByWechat();
                }
                break;
        }
    }

    private void toggleCount(int count) {
        int currentCount = RECHARGE_COUNT_100;
        if (count == RECHARGE_COUNT_100) {
            currentCount = RECHARGE_COUNT_100;
            tv100.setTextColor(getResources().getColor(R.color.green));
            tv50.setTextColor(getResources().getColor(R.color.text_gray2));
            tv10.setTextColor(getResources().getColor(R.color.text_gray2));
            tv100.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_rec_border_green));
            tv50.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_rec_border));
            tv10.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_rec_border));
        } else if (count == RECHARGE_COUNT_50) {
            currentCount = RECHARGE_COUNT_50;
            tv50.setTextColor(getResources().getColor(R.color.green));
            tv100.setTextColor(getResources().getColor(R.color.text_gray2));
            tv10.setTextColor(getResources().getColor(R.color.text_gray2));
            tv100.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_rec_border));
            tv50.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_rec_border_green));
            tv10.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_rec_border));
        } else if (count == RECHARGE_COUNT_10) {
            currentCount = RECHARGE_COUNT_10;
            tv10.setTextColor(getResources().getColor(R.color.green));
            tv50.setTextColor(getResources().getColor(R.color.text_gray2));
            tv100.setTextColor(getResources().getColor(R.color.text_gray2));
            tv100.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_rec_border));
            tv50.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_rec_border));
            tv10.setBackgroundDrawable(getResources().getDrawable(R.drawable.tv_rec_border_green));
        }
    }

    private void toggleMethod(int method) {
        Log.e("RechargeActivity", "currentMethod" + currentMethod);
        if (method == RECHARGE_METHOD_ALIPAY) {
            currentMethod = RECHARGE_METHOD_ALIPAY;
            ivAlipay.setImageResource(R.drawable.selecteddeposit);
            ivWeChat.setImageResource(R.drawable.noselecteddeposit);
        } else if (method == RECHARGE_METHOD_WECHAT) {
            currentMethod = RECHARGE_METHOD_WECHAT;
            ivWeChat.setImageResource(R.drawable.selecteddeposit);
            ivAlipay.setImageResource(R.drawable.noselecteddeposit);
        }
    }

    /**
     * 支付宝支付业务
     */
    public void payByAli() {

        OrderBean orderBean = new OrderBean(OrderBean.ORDER_TYPE_RECHARGE, "100", "0.01", "");
        httpPayBeanUtil.getAlipayToken(mAuthNativeToken.getAuthToken().getAccess_token(),
                orderBean, new HttpCallbackHandler<ALiPayToken>() {
                    @Override
                    public void onSuccess(ALiPayToken payToken) {
                        payRequest(payToken.getPayStr());
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {

                    }
                });

    }

    /**
     * 开启阿里支付线程
     *
     * @param orderInfo
     */
    private void payRequest(String orderInfo) {
        LogUtil.e(TAG, "开启阿里支付线程");
        AliPayThread aliPayThread = new AliPayThread(this, orderInfo);
        aliPayThread.setAliPaySyncCallbackHandler(new AliPayThread.AliPaySyncCallbackHandler() {
            @Override
            public void payBack(Map<String, String> result) {
                PayResult payResult = new PayResult((Map<String, String>) result);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                LogUtil.e(TAG, "阿里支付结果:" + resultInfo + ", resultStatus:" + resultStatus);
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, Constants.ALI_PAY_SUCCESS)) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    ToastUtil.showUIThread(RechargeActivity.this, getResources().getString(R
                            .string.tip_164));
                    //查询后台
                    payHandler.sendEmptyMessageDelayed(CONFIRM_BALANCE, CONFIRM_BALANCE_DELAYED);
                    confirmCount = 3;

                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    ToastUtil.showUIThread(RechargeActivity.this, getResources().getString(R
                            .string.tip_165));
                }
            }
        });
        aliPayThread.start();
    }

    private static final int CONFIRM_BALANCE_DELAYED = 1000;
    private static final int CONFIRM_BALANCE = 1;
    private int confirmCount = 3;

    private static class PayHandler extends Handler {
        private RechargeActivity activity;

        public PayHandler(RechargeActivity activity) {
            this.activity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CONFIRM_BALANCE:
                    if (activity.confirmCount > 0) {
                        LogUtil.e(TAG, "请求后台确认支付结果：confirmCount＝" + activity.confirmCount);
                        activity.confirmPaySuccess();
                        activity.confirmCount--;
                    }
                    break;
            }
        }
    }

    ;

    /**
     * 查询后台支付完成
     */
    private void confirmPaySuccess() {
        HttpAccountBeanUtil httpAccountBeanUtil = new HttpAccountBeanUtil(this);
        httpAccountBeanUtil.getAccountBalanceInfo(mAuthNativeToken.getAuthToken().getAccess_token
                (), new HttpCallbackHandler<BalanceInfoToken>() {

            @Override
            public void onSuccess(BalanceInfoToken balanceInfoToken) {
                if (balanceInfoToken.getErrcode() == Error.OK) {
                    AccountInfo account = balanceInfoToken.getAccount();
                    final String newBalanceStr = account.getBalance();
                    final double newBalance = Double.parseDouble(newBalanceStr);
                    final String oldBalanceStr = mAccountToken.getAccount().getBalance();
                    double oldBalance = Double.parseDouble(oldBalanceStr);
                    if (newBalance > oldBalance) {
                        //支付成功
                        ToastUtil.showUIThread(RechargeActivity.this, balanceInfoToken.getErrmsg());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                walletCount.setText(newBalanceStr);
                                //更新内存
                                mAccountToken.getAccount().setBalance(newBalanceStr);
                                CacheUtil.clearCache(getApplicationContext(), Constants.ACCOUNT_TOKEN_CACHE_FILE_NAME);
                                CacheUtil.cacheSerialToken(getApplicationContext(), Constants.ACCOUNT_TOKEN_CACHE_FILE_NAME, mAccountToken);
                            }
                        });
                    }else {
                        //再一秒再查询
                        payHandler.sendEmptyMessageDelayed(CONFIRM_BALANCE, CONFIRM_BALANCE_DELAYED);
                    }
                } else {
                    ToastUtil.showUIThread(RechargeActivity.this, balanceInfoToken.getErrmsg());
                }
            }

            @Override
            public void onFailure(Exception error, String msg) {

            }
        });
    }


    /**
     * 通过微信支付
     */
    private void payByWechat() {

        OrderBean orderBean = new OrderBean(OrderBean.ORDER_TYPE_RECHARGE, "100", "0.01", "");
        httpPayBeanUtil.getWechatPayToken(mAuthNativeToken.getAuthToken().getAccess_token(),
                orderBean, new HttpCallbackHandler<WechatPayToken>() {
                    @Override
                    public void onSuccess(WechatPayToken wechatPayToken) {
                        weChatPay(wechatPayToken);
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                    }
                });
    }

    /**
     * 调用微信客户端进入支付
     *
     * @param wechatPayToken
     */
    private void weChatPay(WechatPayToken wechatPayToken) {
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            Toast.makeText(this, getResources().getString(R.string.tip_162), Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        // 将该app注册到微信
        api.registerApp(Constants.WECHAT_PAY_APP_ID);
        if (null != wechatPayToken) {
            PayReq req = new PayReq();
//				req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
            req.appId = wechatPayToken.getAppId();
            req.partnerId = wechatPayToken.getPartnerId();
            req.prepayId = wechatPayToken.getPrepayId();
            req.nonceStr = wechatPayToken.getNonceStr();
            req.timeStamp = wechatPayToken.getTimeStamp();
            req.packageValue = wechatPayToken.getPkg();
            req.sign = wechatPayToken.getPaySign();
            System.out.println("packageValue = " + req.packageValue);
//            ToastUtil.showUIThread(RechargeActivity.this, "正常调起支付");
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            boolean b = api.sendReq(req);
            if (b) {
                //查询后台
                payHandler.sendEmptyMessageDelayed(CONFIRM_BALANCE, CONFIRM_BALANCE_DELAYED);
                confirmCount = 3;
            }
            LogUtil.e(TAG, "支付RESULT=" + b);
        } else {
            Log.d("PAY_GET", "返回错误");
            Toast.makeText(this, getResources().getString(R.string.tip_166), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
