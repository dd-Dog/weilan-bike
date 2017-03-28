package com.wlcxbj.bike.activity;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.pay.PayResult;
import com.wlcxbj.bike.bean.account.AuthNativeToken;
import com.wlcxbj.bike.bean.pay.ALiPayToken;
import com.wlcxbj.bike.bean.pay.OrderBean;
import com.wlcxbj.bike.bean.pay.WechatPayToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.net.OkhttpHelper;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.net.beanutil.HttpPayBeanUtil;
import com.wlcxbj.bike.thread.AliPayThread;
import com.wlcxbj.bike.util.cache.CacheUtil;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.ToastUtil;

/**
 * Created by bain on 17-1-20.
 */
public class PayRefundActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PayRefundActivity";
    private static final int RECHARGE_METHOD_ALIPAY = 100;
    private static final int RECHARGE_METHOD_WECHAT = 200;
    private int currentMethod = RECHARGE_METHOD_ALIPAY;
    private ImageView ivAlipay;
    private ImageView ivWeChat;


    private IWXAPI api;
    private AuthNativeToken authNativeTotken;
    private HttpPayBeanUtil httpPayBeanUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化微信支付API
        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_PAY_APP_ID);
        OkhttpHelper okhttpHelper = OkhttpHelper.getInstance();
        initData();
        initView();
    }

    private void initData() {
        httpPayBeanUtil = new HttpPayBeanUtil(this);
        authNativeTotken = CacheUtil.getSerialToken(this, Constants
                .AUTH_CACHE_FILE_NAME);
    }

    private void initView() {
        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.rl_wechat).setOnClickListener(this);
        findViewById(R.id.rl_alipay).setOnClickListener(this);
        findViewById(R.id.btn_recharge).setOnClickListener(this);
        ivWeChat = (ImageView) findViewById(R.id.iv_wechat);
        ivAlipay = (ImageView) findViewById(R.id.iv_alipay);
        toggleMethod(RECHARGE_METHOD_WECHAT);

    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_pay_refund);
        getSupportActionBar().hide();
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
            case R.id.btn_recharge:
                if (currentMethod == RECHARGE_METHOD_ALIPAY) {
                    payByAli();
                } else {
                    payByWechat();
                }
                break;
        }
    }

    private void payByWechat() {

        OrderBean orderBean = new OrderBean(OrderBean.ORDER_TYPE_DEPOSIT, "100", "0.01", "");
        httpPayBeanUtil.getWechatPayToken(authNativeTotken.getAuthToken().getAccess_token(),
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
        boolean register = api.registerApp(Constants.WECHAT_PAY_APP_ID);
        Log.e(TAG, "register=" + register);
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
            ToastUtil.showUIThread(PayRefundActivity.this, getResources().getString(R.string
                    .tip_163));
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            boolean b = api.sendReq(req);
            LogUtil.e(TAG, "支付RESULT=" + b);
        } else {
            Log.d("PAY_GET", "返回错误");
            Toast.makeText(this, "返回错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 支付宝支付业务
     */
    public void payByAli() {

        OrderBean orderBean = new OrderBean(OrderBean.ORDER_TYPE_DEPOSIT, "100", "0.01", "");
        httpPayBeanUtil.getAlipayToken(authNativeTotken.getAuthToken().getAccess_token(),
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
                final String resultStatus = payResult.getResultStatus();
                LogUtil.e(TAG, "阿里支付结果:" + resultInfo + ", resultStatus:" + resultStatus);
                // 判断resultStatus 为9000则代表支付成功
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.equals(resultStatus, Constants.ALI_PAY_SUCCESS)) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            Toast.makeText(PayRefundActivity.this, getResources().getString(R.string
                                    .tip_164), Toast.LENGTH_SHORT).show();
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            Toast.makeText(PayRefundActivity.this, getResources().getString(R.string
                                    .tip_165), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        aliPayThread.start();
    }
}
