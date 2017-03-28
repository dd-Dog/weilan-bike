package com.dtr.zbar.scan;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.wlcxbj.bike.R;
import com.dtr.zbar.build.ZBarDecoder;
import com.wlcxbj.bike.activity.BaseActivity;
import com.wlcxbj.bike.activity.ManualInputSerialActivity;

import java.io.IOException;
import java.lang.reflect.Field;

public class CaptureActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "CaptureActivity";
    private static Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private CameraManager mCameraManager;
    private static final int REQUEST_SERRIAL_NUMBER = 1990;
    private TextView scanResult;
    private FrameLayout scanPreview;
    private Button scanRestart;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;

    private Rect mCropRect = null;
    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private SoundPool soundPool;
    private int success; // 测试提交

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewById();
        addEvents();
        initViews();
        String ggg = android.os.Build.CPU_ABI;
        System.out.println(ggg);
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        success = soundPool.load(this, R.raw.success, 1);
    }

    private void findViewById() {
        scanPreview = (FrameLayout) findViewById(R.id.capture_preview);
        scanResult = (TextView) findViewById(R.id.capture_scan_result);
        scanRestart = (Button) findViewById(R.id.capture_restart_scan);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.btn_manual).setOnClickListener(this);
        findViewById(R.id.button_openorcloseClick).setOnClickListener(this);
    }

    private void addEvents() {
        scanRestart.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                /*if (barcodeScanned) {
                    barcodeScanned = false;
					scanResult.setText("Scanning...");
					mCamera.setPreviewCallback(previewCb);
					mCamera.startPreview();
					previewing = true;
					mCamera.autoFocus(autoFocusCB);
				}*/
                Toast.makeText(getApplicationContext(), "dianjile", Toast.LENGTH_SHORT).show();
                soundPool.play(success, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        });
    }

    private void initViews() {
        if (mCamera != null) return;
        autoFocusHandler = new Handler();
        mCameraManager = new CameraManager(this);
        try {
            mCameraManager.openDriver();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera = mCameraManager.getCamera();
        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        scanPreview.addView(mPreview);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(3000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.REVERSE);
        scanLine.startAnimation(animation);
    }

    public void onPause() {
        super.onPause();
//        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
//            mCameraManager.closeDriver();
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Size size = camera.getParameters().getPreviewSize();

            // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < size.height; y++) {
                for (int x = 0; x < size.width; x++)
                    rotatedData[x * size.height + size.height - y - 1] = data[x + y * size.width];
            }

            // 宽高也要调整
            int tmp = size.width;
            size.width = size.height;
            size.height = tmp;

            initCrop();
            ZBarDecoder zBarDecoder = new ZBarDecoder();
            String result = zBarDecoder.decodeCrop(rotatedData, size.width, size.height,
                    mCropRect.left, mCropRect.top, mCropRect.width(), mCropRect.height());

            if (!TextUtils.isEmpty(result)) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                barcodeScanned = true;

                Intent intent = new Intent();
                intent.putExtra("result", result);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    public static Camera getCamera() {
        return mCamera;
    }
    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = mCameraManager.getCameraResolution().y;
        int cameraHeight = mCameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

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
    public void setContentViewID() {
        setContentView(R.layout.activity_capture);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        releaseCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initViews();
//        soundPool.release();
    }

    private Camera.Parameters parameters;
    private static int FLASH_OFF = 1;
    private static int FLASH_STATE = FLASH_OFF;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.btn_manual:
                //启动手动输入
                Intent intent = new Intent(CaptureActivity.this, ManualInputSerialActivity.class);
                startActivityForResult(intent, REQUEST_SERRIAL_NUMBER);
                break;
            case R.id.button_openorcloseClick:
                if (FLASH_STATE == FLASH_OFF) {
                    //直接开启
//                    camera = Camera.open();
                    parameters = mCamera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                    mCamera.setParameters(parameters);
                    mCamera.startPreview();
                    int FLASH_ON = 0;
                    FLASH_STATE = FLASH_ON;
                    Log.e("flash", "打开闪光灯");
                } else if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(parameters);
                    //不能停止preview，也不能翻译，因为相机还在使用当中
//                    mCamera.stopPreview();
//                    mCamera.release();
//                    mCamera = null;
                    FLASH_STATE = FLASH_OFF;
                    Log.e("flash", "关闭闪光灯");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SERRIAL_NUMBER) {
            if (resultCode == RESULT_OK) {
                String serial_number = data.getStringExtra("serial_number");
                Log.e(TAG, "serial_number=" + serial_number);
                Intent intent = new Intent();
                intent.putExtra("result", serial_number);
                setResult(RESULT_OK, intent);
                finish();
            }else if(resultCode == RESULT_CANCELED) {
            }
        }
    }
}
