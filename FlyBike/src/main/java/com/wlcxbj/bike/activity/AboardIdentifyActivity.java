package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wlcxbj.bike.R;

/**
 * Created by bain on 17-1-21.
 */
public class AboardIdentifyActivity extends BaseActivity implements View.OnClickListener {


    private static final int REQUEST_HEAD_ICON_IMAGE_LEFT = 1000;
    private static final int REQUEST_HEAD_ICON_IMAGE_RIGHT = 2000;
    private Button btnSubmmit;
    private boolean hasFullName = false;
    private boolean hasIdenCode = false;
    private boolean hasCountry = false;
    private ImageView ivRight;
    private ImageView ivLeft;
    private boolean hasPicLet = false;
    private boolean hasPicRight = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_aboard_identify);
        LinearLayout picLeft = (LinearLayout) findViewById(R.id.pic_left);
        LinearLayout picRight = (LinearLayout) findViewById(R.id.pic_right);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivRight = (ImageView) findViewById(R.id.iv_right);
        btnSubmmit = (Button) findViewById(R.id.btn_submmit);
        btnSubmmit.setOnClickListener(this);
        EditText etFullName = (EditText) findViewById(R.id.et_fullname);
        EditText etIdenCode = (EditText) findViewById(R.id.et_identity_code);
        EditText etCountry = (EditText) findViewById(R.id.et_country);
        findViewById(R.id.ib_back).setOnClickListener(this);
        picLeft.setOnClickListener(this);
        picRight.setOnClickListener(this);
        btnSubmmit.setClickable(false);

        etFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    hasFullName = true;
                } else {
                    hasFullName = false;
                }
                toggleButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etIdenCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    hasIdenCode = true;
                } else {
                    hasIdenCode = false;
                }
                toggleButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    hasCountry = true;
                } else {
                    hasCountry = false;
                }
                toggleButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.pic_left:
                //选择图片
                getPicture(REQUEST_HEAD_ICON_IMAGE_LEFT);
                break;
            case R.id.pic_right:
                getPicture(REQUEST_HEAD_ICON_IMAGE_RIGHT);
                break;
            case R.id.btn_submmit:

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_HEAD_ICON_IMAGE_LEFT) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri == null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap bm = (Bitmap) extras.get("data");
                        ivLeft.setImageBitmap(bm);
                        hasPicLet = true;
                    }
                } else {
                    ivLeft.setImageURI(uri);
                    hasPicLet = true;
                }
            }
        }else if(requestCode == REQUEST_HEAD_ICON_IMAGE_RIGHT) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri == null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                        ivRight.setImageBitmap(photo);
                        hasPicRight = true;
                    } else {
                        Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                } else {
                    ivRight.setImageURI(uri);
                    hasPicRight = true;
                }
            }
        }
    }

    private void getPicture(int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, requestCode);
    }


    private void toggleButton() {

        if (hasFullName && hasIdenCode && hasCountry && hasPicLet && hasPicRight) {
            btnSubmmit.setClickable(true);
            btnSubmmit.setBackgroundResource(R.drawable.wallet_selector);
        } else {
            btnSubmmit.setClickable(false);
            btnSubmmit.setBackgroundResource(R.drawable.recharge_button_bg_gray);
        }
    }
}
