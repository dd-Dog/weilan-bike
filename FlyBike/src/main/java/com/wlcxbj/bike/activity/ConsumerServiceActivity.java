package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wlcxbj.bike.R;

/**
 * Created by bain on 16-11-30.
 */
public class ConsumerServiceActivity extends BaseActivity implements TextWatcher {
    private static final String TAG = "ConsumerServiceActivity";
    private static final int REQUEST_IMAGE = 10088;
    @Bind(R.id.alipay)
    Button alipay;
    @Bind(R.id.wechat)
    Button wechat;
    @Bind(R.id.iv_screenshot)
    ImageView ivScreenshot;
    @Bind(R.id.transaction_code)
    EditText transactionCode;
    @Bind(R.id.btn_missing)
    Button btnMissing;
    @Bind(R.id.ib_back)
    ImageButton ibBack;


    private static final int ALIPAY = 1000;
    private static final int WECHATPAY = 2000;
    private static int pay_method = ALIPAY;
    private static boolean imageSetted = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        transactionCode.addTextChangedListener(this);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_consumer_service);
    }

    @OnClick({R.id.tv_get_transCode, R.id.ib_back, R.id.alipay, R.id.wechat, R.id.iv_screenshot, R.id.transaction_code, R.id.btn_missing})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alipay:
                if (!(pay_method == ALIPAY)) {
                    clear();
                    pay_method = ALIPAY;
                    alipay.setBackgroundColor(getResources().getColor(R.color.wallet_orange));
                    wechat.setBackgroundColor(getResources().getColor(R.color.gray));
                    imageSetted = false;
                    setButtonEnable(false);
                }
                break;
            case R.id.wechat:
                if (!(pay_method == WECHATPAY)) {
                    clear();
                    pay_method = WECHATPAY;
                    wechat.setBackgroundColor(getResources().getColor(R.color.wallet_orange));
                    alipay.setBackgroundColor(getResources().getColor(R.color.gray));
                    imageSetted = false;
                    setButtonEnable(false);
                }
                break;
            case R.id.iv_screenshot:
                openGallery();
                break;
            case R.id.tv_get_transCode:
                startActivity(new Intent(this, GetTranCodeMethodActivity.class));
                break;
            case R.id.btn_missing:
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult");
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Log.e(TAG, "Uri=" + uri);
                if (uri == null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap bm = (Bitmap) extras.get("data");
                        ivScreenshot.setImageBitmap(bm);
                        imageSetted = true;
                    }
                }else {
                    ivScreenshot.setImageURI(uri);
                    imageSetted = true;
                }
                if (isOk()) {
                    setButtonEnable(true);
                }
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_IMAGE);
//        Intent intent = new Intent();
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        //根据版本号不同使用不同的Action
//        if (Build.VERSION.SDK_INT <19) {
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//        }else {
//            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        }
//        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void clear() {
        ivScreenshot.setImageResource(R.mipmap.report_violation_default);
        transactionCode.setText("");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isOk()) {
            setButtonEnable(true);
        }else {
            setButtonEnable(false);
        }
    }

    private void setButtonEnable(boolean enabled) {
        btnMissing.setClickable(enabled);
        btnMissing.setBackgroundResource(enabled? R.drawable.wallet_selector : R.drawable.gray_bg);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean isOk() {
        return (!TextUtils.isEmpty(transactionCode.getText().toString())) && imageSetted;
    }
}
