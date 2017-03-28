package com.libs.zxing;

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

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bain on 16-11-22.
 */
public class ManualInputSerialActivity extends Activity implements View.OnClickListener,
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
        setContentView(R.layout.activity_manual_input);
        initState();
        FrameLayout ivBack = (FrameLayout) findViewById(R.id.iv_back);
        pswView = (GridPasswordView) findViewById(R.id.pswView);
        ivFlash = (ImageView) findViewById(R.id.iv_flash);

        ivFlash.setOnClickListener(this);
        pswView.togglePasswordVisibility();
        pswView.setOnPasswordChangedListener(this);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        ivBack.setOnClickListener(this);
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
        if (view.getId() == R.id.iv_back) {
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
                parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                camera.setParameters(parameters);
                camera.startPreview();
                int FLASH_ON = 0;
                FLASH_STATE = FLASH_ON;
                ivFlash.setBackgroundResource(R.mipmap.scan_qrcode_flash_light_on);
                Log.e("flash", "打开闪光灯");
            } else if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();
                camera.release();
                camera = null;
                FLASH_STATE = FLASH_OFF;
                ivFlash.setBackgroundResource(R.mipmap.scan_qrcode_flash_light_off);
                Log.e("flash", "关闭闪光灯");
            }
        }
    }


    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     */
    private void initState() {
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
            linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = getStatusBarHeight();
            //动态的设置隐藏布局的高度
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear_bar
                    .getLayoutParams();
            params.height = statusHeight;
            linear_bar.setLayoutParams(params);
        }
    }

    /**
     * 通过反射的方式获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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
