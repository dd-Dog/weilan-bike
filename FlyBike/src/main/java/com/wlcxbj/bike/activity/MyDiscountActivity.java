package com.wlcxbj.bike.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.net.beanutil.HttpAccountOtherBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.util.ToastUtil;

/**
 * Created by bain on 17-1-17.
 */
public class MyDiscountActivity extends BaseActivity implements View.OnClickListener {

    private EditText ticketNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //自动弹出软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        ticketNum.requestFocus();
//                        ticketNum.performClick();
                        InputMethodManager inputManager =
                                (InputMethodManager)ticketNum.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(ticketNum, 0);
                        Log.e("inputmethod", "弹出软键盘");
                    }
                });
            }
        }, 300);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.actvity_mydiscount);
        getSupportActionBar().hide();
        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.btn_traval).setOnClickListener(this);
        findViewById(R.id.tv_right).setOnClickListener(this);
        ticketNum = (EditText) findViewById(R.id.et_tickets_number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.btn_traval:
                String num = ticketNum.getText().toString().trim();
                if (TextUtils.isEmpty(num)) {
                    ToastUtil.showUIThread(this, getResources().getString(R.string.tip_160));
                    return;
                }
                transferTickets(Integer.parseInt(num));
                break;
            case R.id.tv_right:
                startActivity(new Intent(this, TicketsGuideActivity.class));
                break;
        }
    }

    private void transferTickets(int num) {
        HttpAccountOtherBeanUtil httpAccountOtherBeanUtil = new HttpAccountOtherBeanUtil(this);
        httpAccountOtherBeanUtil.exchangeCoupon(mAuthNativeToken.getAuthToken().getAccess_token()
                , num, new HttpCallbackHandler() {
                    @Override
                    public void onSuccess(Object o) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showTransferResult(true);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showTransferResult(false);
                            }
                        });
                    }
                });
    }

    private void showTransferResult(boolean b) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(b ? getResources().getString(R.string.tip_187) : getResources().getString(R.string.tip_188))
                .setPositiveButton(getResources().getString(R.string.confirm), null)
                .create();
        alertDialog.show();
    }
}
