package com.wlcxbj.bike.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.meg7.widget.CircleImageView;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.account.BasicInfo;
import com.wlcxbj.bike.bean.account.BasicUserInfoToken;
import com.wlcxbj.bike.bean.oss.AvatarURL2OSSParamsUtil;
import com.wlcxbj.bike.bean.oss.OSSFileParams;
import com.wlcxbj.bike.bean.UserIconAddressBean;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.net.beanutil.HttpAccountBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.util.account.AccountUtil;
import com.wlcxbj.bike.util.image.ImageHelper;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.image.ImageHelperCallbackHandler;
import com.wlcxbj.bike.view.ActionSheetDialog;


/**
 * Created by Administrator on 2016/11/16.
 */
public class UserInfoActivity extends BaseActivity {
    private static final int REQUEST_IMAGE_CAPTURE_CAMEIA = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;
    private static final int REQUEST_CROP_IMAGE = 3;
    private static final int REQUEST_CHANGE_NICKNAME = 4;
    @Bind(R.id.iv_headIcon)
    CircleImageView ivHeadIcon;
    @Bind(R.id.rl_head_icon)
    RelativeLayout rlHeadIcon;
    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.rl_nick_name)
    RelativeLayout rlNickName;
    @Bind(R.id.tv_fullname)
    TextView tvFullname;
    @Bind(R.id.rl_fullname)
    RelativeLayout rlFullname;
    @Bind(R.id.rl_identify)
    RelativeLayout rlIdentify;
    @Bind(R.id.tv_phoneNumber)
    TextView tvPhoneNumber;
    @Bind(R.id.rl_phone_number)
    RelativeLayout rlPhoneNumber;
    @Bind(R.id.tv_school)
    TextView tvSchool;
    @Bind(R.id.rl_school)
    RelativeLayout rlSchool;
    private Dialog dialog;
    private static final String TAG = "UserInfoActivity";
    private ImageView userIdentityPic;
    private TextView tvIdentityState;
    private UserIconAddressBean mUserIconAddressBean;
    private ImageHelper imageHelper;
    private Uri photoUri;
    private Bitmap mUserIconBitmap;
    private OSSFileParams ossFileParams;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        //图片工具类
        imageHelper = new ImageHelper(this);
        initView();
        initData();
    }

    private void initView() {
        userIdentityPic = (ImageView) findViewById(R.id.user_identity_pic);
        tvIdentityState = (TextView) findViewById(R.id.identify_state);
    }

    private void initData() {
        if (mAccountToken == null) return;
        if (!AccountUtil.isLogin(this)) return;
        if (mAccountToken.getRealInfo().getVerifySpid() == 1) {
            tvIdentityState.setText(R.string.under_check);
            tvIdentityState.setTextColor(getResources().getColor(R.color.green));
            userIdentityPic.setImageResource(R.drawable.id_number_green);
        } else if (mAccountToken.getRealInfo().getVerifySpid() == 2) {
            tvIdentityState.setText(R.string.check_done);
            tvIdentityState.setTextColor(getResources().getColor(R.color.green));
            userIdentityPic.setImageResource(R.drawable.id_number_green);
        } else {
            tvIdentityState.setText(R.string.not_identify);
            tvIdentityState.setTextColor(getResources().getColor(R.color.user_text_gray));
            userIdentityPic.setImageResource(R.drawable.id_number_grey);
        }
        String mobile = mAuthNativeToken.getAuthToken().getMobile();
        tvPhoneNumber.setText(mobile);
        String nickName = mAccountToken.getBasicInfo().getNickName();
        tvNickname.setText(TextUtils.isEmpty(nickName) ? mobile : nickName);
        String realName = mAccountToken.getRealInfo().getRealName();
        tvFullname.setText(TextUtils.isEmpty(realName) ? getResources().getString(R.string
                .tip_179) : realName);
        //从OSS服务器获取头像
        getUserIcon();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_userinfo);
    }

    @OnClick({R.id.ib_back, R.id.rl_head_icon, R.id.rl_nick_name, R.id.rl_phone_number, R.id
            .rl_identify, R.id.rl_school})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.rl_head_icon:
                showGetPictureDialog2();
                break;
            case R.id.rl_nick_name:
                Intent nickNameIntent = new Intent(this, ModifyNickNameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(MapActivity.AUTH_NATIVE_TOKEN, mAuthNativeToken);
                bundle.putSerializable(MapActivity.ACCOUNT_TOKEN, mAccountToken);
                nickNameIntent.putExtras(bundle);
                nickNameIntent.putExtra("nickname", tvNickname.getText().toString());
                startActivityForResult(nickNameIntent, REQUEST_CHANGE_NICKNAME);
                break;
            case R.id.rl_phone_number:
                Intent phoneNum = new Intent(this, ModifyPhoneNumberActivity.class);
                startActivity(phoneNum);
                break;
            case R.id.rl_school:
                startActivity(new Intent(this, SchoolIdentifyActivity.class));
                break;
            case R.id.rl_identify:
                if (mAccountToken.getRealInfo().getVerifySpid() == 2) {
                    return;
                }
                startActivity(new Intent(this, IdentityActivity.class));
                break;
        }
    }

    /**
     * 调用系统拍照
     */
    public Uri takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "avatar_" +
                String.valueOf(System.currentTimeMillis()) + ".png"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_CAMEIA);
        return photoUri;
    }

    private void showGetPictureDialog2() {
        new ActionSheetDialog(this)
                .builder()
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("拍照",
                        ActionSheetDialog.SheetItemColor.GREEN,
                        new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                takePhoto();
                            }
                        })
                .addSheetItem("从相册选一张",
                        ActionSheetDialog.SheetItemColor.GREEN,
                        new ActionSheetDialog.OnSheetItemClickListener() {

                            @Override
                            public void onClick(int which) {
                                //选择图片
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media
                                        .EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY);
                            }
                        }).show();
    }

    //TODO 获取图片应该先裁剪再进行显示和上传,否则图片体积太大,而且不适合做头像显示
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_FROM_GALLERY:
                    photoUri = data.getData();
                    if (photoUri != null) {
                        LogUtil.e(TAG, "从相册获取图片,data type=URI");
                        //启动裁剪
                        startPhotoZoom(photoUri);
                    } else {
                        LogUtil.e(TAG, "从相册获取图片,data type=null");
                    }
                    break;
                case REQUEST_IMAGE_CAPTURE_CAMEIA:
                    if (photoUri != null) {
                        startPhotoZoom(photoUri);
                    }
                    break;
                case REQUEST_CROP_IMAGE:
                    setCropImg(data);
                    break;
                case REQUEST_CHANGE_NICKNAME:
                    String newNickname = data.getStringExtra(ModifyNickNameActivity.NEW_NICKNAME);
                    tvNickname.setText(newNickname);
                    break;
            }
        }
    }

    /**
     * 设置并上传头像
     *
     * @param picdata
     */
    private void setCropImg(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (null != bundle) {
            LogUtil.e(TAG, "裁剪成功");
            mUserIconBitmap = bundle.getParcelable("data");
            ivHeadIcon.setImageBitmap(mUserIconBitmap);
//            String cropImageUri = Environment.getExternalStorageDirectory() + "/crop_" + System
//                    .currentTimeMillis() + ".png";
            if (mAccountToken == null) return;
            String idno = mAccountToken.getAccount().getEnduserId();
            //保存文件到本地
            String localFileName = imageHelper.writeBitmapToLocal(mUserIconBitmap, Constants
                    .USER_ICON_PREFIX + idno);
            //上传用户头像
            uploadUserIcon(getCacheDir() + "/" + localFileName, Constants.OSS_AVATAR_PATH +
                    Constants.USER_ICON_PREFIX + idno);
            cacheBitmapTolocal(mUserIconBitmap);
        }
    }

    /**
     * 启动系统裁剪
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {

        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(uri, "image/*");
        // 设置裁剪
        cropIntent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        cropIntent.putExtra("outputX", 320);
        cropIntent.putExtra("outputY", 320);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, REQUEST_CROP_IMAGE);
    }


    /**
     * 上传用户头像
     */
    private void uploadUserIcon(String srcPath, String objectName) {
        LogUtil.e(TAG, "开始上传用户头像");
        imageHelper.uploadImageToOss(srcPath, objectName);
    }

    /**
     * 获取用户头像
     */
    public void getUserIcon() {
        imageHelper.setCallbackHandler(imageHelperCallbackHandler);
        ossFileParams = (OSSFileParams) getIntent().getSerializableExtra
                (MapActivity.USER_ICON_BEAN);
        if (ossFileParams != null) {
            imageHelper.display(ivHeadIcon, ossFileParams);
        } else {
            HttpAccountBeanUtil httpAccountBeanUtil = new HttpAccountBeanUtil(this);
            httpAccountBeanUtil.getBasicUserInfo(mAuthNativeToken.getAuthToken().getAccess_token(),
                    new HttpCallbackHandler<BasicUserInfoToken>() {
                        @Override
                        public void onSuccess(BasicUserInfoToken basicUserInfoToken) {
                            BasicInfo basicInfo = basicUserInfoToken.getBasicInfo();
                            String avatarUrl = basicInfo.getAvatarUrl();
                            if (avatarUrl != null) {
                                ossFileParams = new AvatarURL2OSSParamsUtil(avatarUrl)
                                        .getOSSParams();
                                LogUtil.e(TAG, "userIconUrl from server:" + ossFileParams);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LogUtil.e(TAG, "refresh usericon:" + ossFileParams
                                                .getFileName());
                                        imageHelper.clear();
                                        imageHelper.display(ivHeadIcon, ossFileParams);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Exception error, String msg) {

                        }
                    });
        }
    }

    ImageHelperCallbackHandler imageHelperCallbackHandler = new ImageHelperCallbackHandler() {

        @Override
        public void onUploadSuccess(PutObjectRequest request, PutObjectResult result) {
            //向服务器上传头像地址
            String bucketName = request.getBucketName();
            String objectKey = request.getObjectKey();
            //http://commonataayun.oss-cn-beijing.aliyuncs.com/userIcon
            String iconUrl = "http://" + bucketName + "." + Constants.ENDPOINT_ONLY + "/" +
                    objectKey;
            LogUtil.e(TAG, "上传的iconUrl=" + iconUrl);
            HttpAccountBeanUtil httpAccountBeanUtil = new HttpAccountBeanUtil
                    (UserInfoActivity.this);
            httpAccountBeanUtil.updateUserInfo(mAuthNativeToken.getAuthToken().getAccess_token(),
                    "", iconUrl, new HttpCallbackHandler() {
                        @Override
                        public void onSuccess(Object o) {
                            LogUtil.e(TAG, "update avatarurl to my server success");
                        }

                        @Override
                        public void onFailure(Exception error, String msg) {
                            LogUtil.e(TAG, "update avatarurl to my server fail");
                        }
                    });
            //保存到本地
            cacheBitmapTolocal(mUserIconBitmap);
        }

        @Override
        public void onUploadFailure(PutObjectRequest request, ClientException clientExcepion,
                                    ServiceException serviceException) {

        }
    };

    private void cacheBitmapTolocal(Bitmap bitmap) {
        if (bitmap == null) {
            LogUtil.e(TAG, "保存头像到本地失败");
            return;
        }
        //缓存头像到本地
        imageHelper.writeBitmapToLocal(bitmap, Constants.USER_ICON_PREFIX);
        imageHelper.writeToMem(Constants.USER_ICON_PREFIX, bitmap);
    }
}
