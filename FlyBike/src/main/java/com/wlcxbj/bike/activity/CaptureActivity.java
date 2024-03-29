package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import com.wlcxbj.bike.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

public class CaptureActivity extends BaseActivity implements QRCodeView.Delegate, View
        .OnClickListener {
    private static final String TAG = CaptureActivity.class.getSimpleName();
    private static final int REQUEST_TNO = 12;
    public static final int RESULT_SERIAL_NUMBER = 100;
    private QRCodeView mQRCodeView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQRCodeView = (ZBarView) findViewById(R.id.zxingview);
        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.btn_manual).setOnClickListener(this);
        findViewById(R.id.button_openorcloseClick).setOnClickListener(this);
        mQRCodeView.setDelegate(this);
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_test_scan);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
        mQRCodeView.changeToScanQRCodeStyle();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
//        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate();
        mQRCodeView.stopSpot();
        Intent intent = new Intent();
        intent.putExtra("result", result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    private boolean flashLightOn = false;

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.btn_manual:
                startActivityForResult(new Intent(this, ManualInputSerialActivity.class), REQUEST_TNO);
                break;
            case R.id.button_openorcloseClick:
                if (flashLightOn) {
                    mQRCodeView.closeFlashlight();
                    flashLightOn = false;
                } else {
                    mQRCodeView.openFlashlight();
                    flashLightOn = true;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TNO:
                    Intent intent = new Intent();
                    intent.putExtra("serial_number", data.getStringExtra("serial_number"));
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    }
}