package com.wlcxbj.bike.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.adapter.TicketsAdapter;
import com.wlcxbj.bike.bean.account.AccountInfo;
import com.wlcxbj.bike.bean.account.AccountToken;
import com.wlcxbj.bike.bean.account.BalanceInfoToken;
import com.wlcxbj.bike.bean.other.CouponsToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.global.Error;
import com.wlcxbj.bike.net.beanutil.HttpAccountBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpAccountOtherBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.ToastUtil;
import com.wlcxbj.bike.util.cache.CacheUtil;

/**
 * Created by bain on 16-11-28.
 */
public class WalletActivity extends BaseActivity {
    private static final int REQUEST_CHARGE_RESULT = 12345;
    private static final int REQUEST_FUND_BACK = 1086;
    private static final int REQUEST_REFUND_BACK = 1;
    private static final int REQUEST_RECHARGE = 2;
    @Bind(R.id.ll_bar)
    LinearLayout llBar;
    @Bind(R.id.ib_back)
    FrameLayout titleBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.btn_recharge)
    Button btnRecharge;
    @Bind(R.id.deposit)
    TextView deposit;
    @Bind(R.id.deposit_detail)
    TextView depositDetail;
    @Bind(R.id.bikeTickets_count)
    TextView bikeTickets;
    @Bind(R.id.ll_bike_tickets)
    RelativeLayout rLBikeTickets;
    @Bind(R.id.ll_deposit)
    RelativeLayout rLDeposit;
    @Bind(R.id.wallet_count)
    TextView tvWalletCount;

    private static final String TAG = "WalletActivity";
    private HttpAccountBeanUtil httpAccountBeanUtil;
    private AccountInfo account;
    private boolean hasDeposit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        httpAccountBeanUtil = new HttpAccountBeanUtil(this);
        //获取帐户余额和押金等信息
        getBalanceInfo();
    }

    private void getBalanceInfo() {
        httpAccountBeanUtil.getAccountBalanceInfo(mAuthNativeToken.getAuthToken().getAccess_token
                (), new HttpCallbackHandler<BalanceInfoToken>() {


            @Override
            public void onSuccess(BalanceInfoToken balanceInfoToken) {
                if (balanceInfoToken.getErrcode() == Error.OK) {
                    account = balanceInfoToken.getAccount();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                        }
                    });
                } else {
                    ToastUtil.showUIThread(WalletActivity.this, getString(R.string.tip_180));
                }
            }

            @Override
            public void onFailure(Exception error, String msg) {

            }
        });
    }

    private void initData() {
        if (account == null) return;
        tvWalletCount.setText(account.getBalance());
        double depositCount = Double.parseDouble(account.getGuaranteeDeposit());
        if (depositCount != 0d) {
            hasDeposit = true;
            depositDetail.setText(account.getGuaranteeDeposit() + getResources().getString(R
                    .string.tip_181));
        } else {
            hasDeposit = false;
            depositDetail.setText(getResources().getString(R.string.tip_82));
        }
        HttpAccountOtherBeanUtil httpAccountOtherBeanUtil = new HttpAccountOtherBeanUtil(this);
        httpAccountOtherBeanUtil.getCouponsList(mAuthNativeToken.getAuthToken()
                .getAccess_token(), -1, -1, new HttpCallbackHandler<CouponsToken>() {
            @Override
            public void onSuccess(final CouponsToken couponsToken) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (couponsToken.getErrcode() == Error.OK && couponsToken.getCoupons() !=
                                null)
                            bikeTickets.setText(couponsToken.getCoupons().size() + "张");
                    }
                });
            }

            @Override
            public void onFailure(Exception error, String msg) {

            }
        });
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_wallet);
    }

    @OnClick({R.id.ib_back, R.id.tv_right, R.id.btn_recharge, R.id.bikeTickets_count,
            R.id.ll_bike_tickets, R.id.ll_deposit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_right:
                Intent walletDetail = new Intent(this, WalletDetailActivity.class);
                walletDetail.putExtras(getAuthBundle());
                startActivityForResult(walletDetail, REQUEST_RECHARGE);
                break;
            case R.id.btn_recharge:
                startActivityForResult(new Intent(this, RechargeActivity.class),
                        REQUEST_CHARGE_RESULT);
                break;
            case R.id.ll_deposit:
                if (hasDeposit) {
                    showRefundDialog();
                } else {
                    Intent depositBack = new Intent(this, PayRefundActivity.class);
                    startActivityForResult(depositBack, REQUEST_REFUND_BACK);
                }
                break;
            case R.id.ll_bike_tickets:
                Intent tickets = new Intent(this, MyTicketsActivity.class);
                tickets.putExtras(getAuthBundle());
                startActivity(tickets);
                break;
        }
    }


    /**
     * 获取带有AuthToken的bundle
     */
    private Bundle getAuthBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MapActivity.AUTH_NATIVE_TOKEN, mAuthNativeToken);
        return bundle;
    }

    /**
     * 获取带有AuthToken,AccountToken的bundle
     */
    private Bundle getAuthAccountBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MapActivity.AUTH_NATIVE_TOKEN, mAuthNativeToken);
        bundle.putSerializable(MapActivity.ACCOUNT_TOKEN, mAccountToken);
        return bundle;
    }

    private void showRefundDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_refund);
        dialog.setTitle(getResources().getString(R.string.tip_182));
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_refund).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "退还押金");
                startActivityForResult(new Intent(WalletActivity.this, FundBackActivity.class),
                        REQUEST_FUND_BACK);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_FUND_BACK:
                initData();
                break;
            case REQUEST_RECHARGE:
                refreshBalance();
                break;
        }
    }

    private void refreshBalance() {
         AccountToken accountToken = CacheUtil.getSerialToken(getApplicationContext(), Constants
                .ACCOUNT_TOKEN_CACHE_FILE_NAME);
        if (accountToken == null) return;
        AccountInfo account = accountToken.getAccount();
        if (account == null) return;
        tvWalletCount.setText(account.getBalance());
    }
}
