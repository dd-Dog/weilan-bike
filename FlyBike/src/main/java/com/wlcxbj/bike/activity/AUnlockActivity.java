package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import butterknife.ButterKnife;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.bike.ReportProblemBean;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.net.beanutil.HttpAccountOtherBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.TimeUtil;
import com.wlcxbj.bike.util.image.ImageHelper;
import com.wlcxbj.bike.util.properties.PropertiesUtil;

/**
 * Created by bain on 16-11-30.
 */
public class AUnlockActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AUnlockActivity";
    private String fileName;
    private ImageHelper imageHelper;
    private String localPath;
    private EditText etContent;
    private LinearLayout lLContent;

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_aunlock);
    }

    private static final int REQUEST_BIKE_NUMBER = 23333;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 23344;
    EditText etTag;
    private static int checkCount = 0;
    private Button btnSubmmit;
    private ImageView ivReport;
    private TextView tvBikeNum;
    private boolean hasBikeNum;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        btnSubmmit = (Button) findViewById(R.id.btn_submmit);
        ivReport = (ImageView) findViewById(R.id.iv_report);
        tvBikeNum = (TextView) findViewById(R.id.tv_bikeNum);
        etContent = (EditText) findViewById(R.id.et_content);
        lLContent = (LinearLayout) findViewById(R.id.ll_content);
        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.ll_scan).setOnClickListener(this);
        btnSubmmit.setOnClickListener(this);
        btnSubmmit.setClickable(false);
        ivReport.setOnClickListener(this);
        imageHelper = new ImageHelper(this);

        String[] titles = getResources().getStringArray(R.array.aunlock_problem_title);
        String[] details = getResources().getStringArray(R.array.aunlock_problem_detail);
        for (int i=0; i<titles.length; i++) {
            View parent = View.inflate(this, R.layout.layout_title_detal, null);
            TextView title = (TextView) parent.findViewById(R.id.tv_title);
            TextView detail = (TextView) parent.findViewById(R.id.tv_detail);
            title.setText(titles[i]);
            detail.setText(details[i]);
            lLContent.addView(parent);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ll_scan:
                startActivityForResult(new Intent(this, CaptureActivity.class),
                        REQUEST_BIKE_NUMBER);
                break;
            case R.id.iv_report:
                Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
                break;
            case R.id.btn_submmit:
                report();
                break;
        }
    }

    private void report() {
        HttpAccountOtherBeanUtil httpAccountOtherBeanUtil = new HttpAccountOtherBeanUtil(this);
        ReportProblemBean reportProblemBean = new ReportProblemBean();
        reportProblemBean.setTid(tvBikeNum.getText().toString());
        reportProblemBean.setIssueTypeSpid(Constants.ISSUE_TYPE_III);
        reportProblemBean.setLat(getIntent().getStringExtra("lat"));
        reportProblemBean.setLat(getIntent().getStringExtra("lng"));
        reportProblemBean.setTitle(getResources().getString(R.string.text_bike_broken));
        reportProblemBean.setNote(etContent.getText().toString());
        String bucket = PropertiesUtil.getProperties(this, PropertiesUtil.OSS_BUCKET);
        if (TextUtils.isEmpty(bucket)) return;
        String imgUrl = Constants.ENDPOINT + "/" + bucket
                + reportProblemBean.getTid() + "/"
                + reportProblemBean.getIssueTypeSpid();
        reportProblemBean.setImg1(imgUrl);
        httpAccountOtherBeanUtil.reportProblem(mAuthNativeToken.getAuthToken().getAccess_token(),
                reportProblemBean, new HttpCallbackHandler() {

                    @Override
                    public void onSuccess(Object o) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "提交成功", Toast
                                        .LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "提交失败", Toast
                                        .LENGTH_SHORT).show();
                            }
                        });
                    }
                });

        Log.e(TAG, "localPath=" + localPath + ",imgUrl=" + imgUrl);
        if (!TextUtils.isEmpty(localPath) && !TextUtils.isEmpty(imgUrl)) {
            Log.e(TAG, "上传问题图片");
            if (mAccountToken != null) {
                String objectName = Constants.OSS_FEEDBACK_FILE_PATH +
                        mAccountToken.getAccount().getEnduserId()
                        + Constants.USER_ICON_PREFIX + TimeUtil.getTimeStr(System.currentTimeMillis());
                imageHelper.uploadImageToOss(getCacheDir() + localPath, objectName);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_BIKE_NUMBER:
                    String result = data.getStringExtra("result");
                    LogUtil.d(TAG,"车编号"+result);
                    tvBikeNum.setText(result.substring(result.indexOf("=")+1));
                    hasBikeNum = true;
                    toggleButton();
                    break;
                case REQUEST_CODE_CAPTURE_CAMEIA:
                    Uri uri = data.getData();
                    if (uri == null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                            //保存文件到本地
                            fileName = "/report" + "/camera_" + System.currentTimeMillis();
                            localPath = imageHelper.writeBitmapToLocal(photo, fileName);
                            ivReport.setImageBitmap(photo);
                        } else {
                            Toast.makeText(getApplicationContext(), "err", Toast.LENGTH_LONG)
                                    .show();
                            return;
                        }
                    } else {
                        ivReport.setImageURI(uri);
                    }
                    break;
            }
        }
    }


    private void toggleButton() {
        if (hasBikeNum) {
            btnSubmmit.setClickable(true);
            btnSubmmit.setBackgroundResource(R.drawable.wallet_selector);
        } else {
            btnSubmmit.setClickable(false);
            btnSubmmit.setBackgroundResource(R.drawable.recharge_button_bg_gray);
        }
    }
}
