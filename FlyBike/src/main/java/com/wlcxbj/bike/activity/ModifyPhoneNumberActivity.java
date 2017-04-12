package com.wlcxbj.bike.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.util.PreferenceUtil;

/**
 * Created by bain on 16-11-30.
 */
public class ModifyPhoneNumberActivity extends BaseActivity {

    @Bind(R.id.ll_bar)
    LinearLayout llBar;
    @Bind(R.id.btn_changePhone)
    Button btnChangePhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_phone_number);
    }

    @OnClick({R.id.btn_changePhone, R.id.ib_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.btn_changePhone:
                Boolean back = PreferenceUtil.getBoolean(this, Constants.USER_FUND_BACK, true);
                if (back) {
                    startActivity(new Intent(this, ChangePhoneNumberActivity.class));
                } else {
                    showRefundDialog();
                }
                break;
        }
    }

    private void showRefundDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_modify_phone_number);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
