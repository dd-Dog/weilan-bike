package com.wlcxbj.bike.activity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jungly.gridpasswordview.GridPasswordView;
import com.wlcxbj.bike.R;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bain on 16-11-22.
 */
public class ManualInputSerialActivity extends BaseActivity implements View.OnClickListener,
        GridPasswordView.OnPasswordChangedListener {

    private GridPasswordView pswView;
    private Button btn_confirm;
    private ImageView ivFlash;
    private Camera camera;
    private Camera.Parameters parameters;
    private static int FLASH_OFF = 1;
    private static int FLASH_STATE = FLASH_OFF;
    private static final String TAG = "ManualInputSerialActivity";
    private int bikeNoLen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pswView = (GridPasswordView) findViewById(R.id.pswView);
        ivFlash = (ImageView) findViewById(R.id.iv_flash);
        findViewById(R.id.ib_back).setOnClickListener(this);
        ivFlash.setOnClickListener(this);
        pswView.togglePasswordVisibility();
        pswView.setOnPasswordChangedListener(this);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        btn_confirm.setClickable(false);
        btn_confirm.setBackgroundResource(R.drawable.recharge_button_bg_gray);
        //自动弹出软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pswView.performClick();
                        Log.e("inputmethod", "弹出软键盘");
                    }
                });
            }
        }, 500);
        String lenStr = getResources().getString(R.string.bike_no_length);
        bikeNoLen = Integer.parseInt(lenStr);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_back) {
            Intent data = new Intent();
            data.putExtra("serial_number", "");
            setResult(RESULT_CANCELED, data);
            finish();
        } else if (view.getId() == R.id.pswView) {

        } else if (view.getId() == R.id.btn_confirm) {
            String text = pswView.getPassWord();
            //此处还需要添加序列号的检查
            if (TextUtils.isEmpty(text)) {
                Toast.makeText(this, "序列号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (text.length() != bikeNoLen) {
                Toast.makeText(this, "请输入正确的序列号", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent data = new Intent();
            data.putExtra("serial_number", text);
            setResult(RESULT_OK, data);
            finish();
        } else if (view.getId() == R.id.iv_flash) {
            if (FLASH_STATE == FLASH_OFF) {
                //直接开启
                camera = Camera.open();
                if (camera == null) return;
                parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                camera.setParameters(parameters);
                camera.startPreview();
                int FLASH_ON = 0;
                FLASH_STATE = FLASH_ON;
                Log.e("flash", "打开闪光灯");
            } else if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();
                camera.release();
                camera = null;
                FLASH_STATE = FLASH_OFF;
                Log.e("flash", "关闭闪光灯");
            }
        }
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_manual_input);
    }

    @Override
    public void onTextChanged(String psw) {
        //检查输入是否正确
        if (psw.length() < bikeNoLen) {
            btn_confirm.setClickable(false);
            btn_confirm.setBackgroundResource(R.drawable.recharge_button_bg_gray);
        } else if (psw.length() == bikeNoLen) {
            btn_confirm.setClickable(true);
            btn_confirm.setBackgroundResource(R.drawable.recharge_button_bg_green);
        }
    }

    @Override
    public void onInputFinish(String psw) {
    }
}
