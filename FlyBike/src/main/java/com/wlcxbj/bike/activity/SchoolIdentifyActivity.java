package com.wlcxbj.bike.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wlcxbj.bike.R;

/**
 * Created by bain on 17-1-21.
 */
public class SchoolIdentifyActivity extends BaseActivity implements View.OnClickListener {

    private Button btnConfirm;
    private boolean hasFullName = false;
    private boolean hasStuCode = false;
    private boolean hasSchool = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        EditText etFullName = (EditText) findViewById(R.id.et_fullname);
        btnConfirm = (Button) findViewById(R.id.btn_submmit);
        EditText etStudentCode = (EditText) findViewById(R.id.et_sutudent_code);
        EditText etSchool = (EditText) findViewById(R.id.et_identity_school);
        findViewById(R.id.ib_back).setOnClickListener(this);
        etSchool.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    hasSchool = true;
                } else {
                    hasSchool = false;
                }
                toggleButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etStudentCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    hasStuCode = true;
                } else {
                    hasStuCode = false;
                }
                toggleButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_school);
    }

    private void toggleButton() {
        if (hasFullName && hasStuCode && hasSchool) {
            btnConfirm.setClickable(true);
            btnConfirm.setBackgroundResource(R.drawable.wallet_selector);
        } else {
            btnConfirm.setClickable(false);
            btnConfirm.setBackgroundResource(R.drawable.recharge_button_bg_gray);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.btn_submmit:

                break;
        }
    }
}
