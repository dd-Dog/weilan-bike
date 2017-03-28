package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.commonaddress.CommonAddressBean;
import com.wlcxbj.bike.bean.commonaddress.SaveCommonAddressToken;
import com.wlcxbj.bike.global.Error;
import com.wlcxbj.bike.net.beanutil.HttpAccountOtherBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.ToastUtil;

/**
 * Created by bain on 17-1-18.
 */
public class EditAddressActivity extends BaseActivity implements View.OnClickListener, View
        .OnTouchListener {

    private static final String TAG = "EditAddressActivity";
    private EditText etAddress;
    private long toBeEditedAddressid;

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_edit_address);
        getSupportActionBar().hide();
//        initToken();
        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.tv_right).setOnClickListener(this);
        etAddress = (EditText) findViewById(R.id.et_address);
        etAddress.setOnTouchListener(this);
        etAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        toBeEditedAddressid = getIntent().getLongExtra("addressid", -1);
        LogUtil.e(TAG, "toBeEditedAddressid=" + toBeEditedAddressid);
        etAddress.performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_right:
                String newAddress = etAddress.getText().toString();
                if (TextUtils.isEmpty(newAddress)) {
                    ToastUtil.show(this, getResources().getString(R.string.tip_132));
                    break;
                }
                saveCommonAddress(newAddress);
                break;
        }
    }

    //保存常用地址
    private void saveCommonAddress(final String newAddress) {
        HttpAccountOtherBeanUtil httpAccountOtherBeanUtil = new HttpAccountOtherBeanUtil(this);
        long addressid = -1;
        int requestcode = getIntent().getIntExtra("requestcode", -1);
        if (requestcode == SearchActivity.REQUEST_ADD_ADDRESS) {
            addressid = -1;
        }else if(requestcode == SearchActivity.REQUEST_EDIT_ADDRESS) {
            addressid = toBeEditedAddressid;
            LogUtil.e(TAG, "addressId=" + addressid);
        }
        final Intent data = new Intent();
        data.putExtra("address", newAddress);
        data.putExtra("addressid", addressid);
        CommonAddressBean commonAddressBean = new CommonAddressBean(addressid, "", "", newAddress);
        httpAccountOtherBeanUtil.saveCommonAddress(mAuthNativeToken.getAuthToken()
                .getAccess_token(), commonAddressBean, new HttpCallbackHandler<SaveCommonAddressToken>() {
            @Override
            public void onSuccess(final SaveCommonAddressToken saveCommonAddressToken) {
                if (saveCommonAddressToken.getErrcode() == Error.OK) {
                    setResult(RESULT_OK, data);
                    data.putExtra("addressid", saveCommonAddressToken.getData().getId());
                }else {
                    setResult(RESULT_CANCELED, data);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(getApplicationContext(), saveCommonAddressToken.getErrmsg());
                    }
                });
                finish();
            }

            @Override
            public void onFailure(Exception error, String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.show(getApplicationContext(), "");
                    }
                });
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!(view.getId() == R.id.et_address)) return false;
        Drawable drawable = etAddress.getCompoundDrawables()[2];
        if (drawable == null) return false;
        //如果不是按下事件，不再处理
        if (motionEvent.getAction() != MotionEvent.ACTION_UP)
            return false;
        //getIntrinsicHeight返回的是以dp为单位的
        if (motionEvent.getX() > etAddress.getWidth()
                - etAddress.getPaddingRight()
                - drawable.getIntrinsicWidth() &&
                motionEvent.getY() < etAddress.getHeight()) {
            etAddress.setText("");
        }
        return false;
    }
}
