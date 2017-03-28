package com.wlcxbj.bike.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.util.ToastUtil;

/**
 * Created by bain on 16-12-1.
 */
public class ChangePhoneNumberActivity extends BaseActivity {


    @Bind(R.id.ll_bar)
    LinearLayout llBar;
    @Bind(R.id.et_fullname)
    TextView etFullname;
    @Bind(R.id.et_identity_code)
    EditText etIdentityCode;
    @Bind(R.id.et_new_phone)
    EditText etNewPhone;
    @Bind(R.id.et_checkNumber)
    EditText etCheckNumber;
    @Bind(R.id.get_checkNumber)
    TextView getCheckNumber;
    @Bind(R.id.btn_confirm)
    Button btnConfirm;
    @Bind(R.id.voice_checkNum)
    TextView voiceCheckNum;
    @Bind(R.id.identity_verify)
    TextView identityVerify;
    @Bind(R.id.phone_verify)
    TextView phoneVerify;


    private static boolean identityRight = false;
    private static boolean newPhoneRight = false;
    private static final String TAG = "ChangePhoneNumber";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        etIdentityCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    identityVerify.setText("请输入正确的身份证号");
                    identityVerify.setTextColor(Color.GRAY);
                    identityRight = false;
                } else if (s.toString().matches("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$")) {
                    identityRight = true;
                    identityVerify.setText("身份证号格式正确");
                    identityVerify.setTextColor(Color.GREEN);
                    if (clickable()) {
                        btnConfirm.setClickable(true);
                        btnConfirm.setBackgroundResource(R.drawable.wallet_selector);
                    } else {
                        btnConfirm.setClickable(false);
                        btnConfirm.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                } else {
                    identityRight = false;
                    identityVerify.setText("身份证号格式错误");
                    identityVerify.setTextColor(Color.RED);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etNewPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "S=" + s);

                if (TextUtils.isEmpty(s.toString())) {
                    phoneVerify.setText("请输入正确的手机号");
                    phoneVerify.setTextColor(Color.GRAY);
                    newPhoneRight = false;
                    setCheckButtonEnable(false);
                }else if (s.toString().matches("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")) {
                    newPhoneRight = true;
                    phoneVerify.setText("手机号证格式正确");
                    phoneVerify.setTextColor(Color.GREEN);
                    setCheckButtonEnable(true);
                    if (clickable()) {
                        btnConfirm.setClickable(true);
                        btnConfirm.setBackgroundResource(R.drawable.wallet_selector);
                    } else {
                        btnConfirm.setClickable(false);
                        btnConfirm.setBackgroundColor(getResources().getColor(R.color.gray));
                    }
                } else {
                    newPhoneRight = false;
                    phoneVerify.setText("手机号格式错误");
                    phoneVerify.setTextColor(Color.RED);
                    setCheckButtonEnable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setCheckButtonEnable(boolean enabled) {
        getCheckNumber.setClickable(enabled);
        getCheckNumber.setBackgroundResource(enabled? R.color.orange : R.color.gray);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_change_phone_number);
    }

    @OnClick({R.id.ib_back, R.id.et_fullname, R.id.get_checkNumber, R.id.btn_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_checkNumber:
                ToastUtil.show(this, getResources().getString(R.string.tip_131));
                break;
            case R.id.btn_confirm:
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }

    public boolean clickable() {
        boolean checkNumberRight = false;
        return identityRight && newPhoneRight && checkNumberRight;
    }
}
