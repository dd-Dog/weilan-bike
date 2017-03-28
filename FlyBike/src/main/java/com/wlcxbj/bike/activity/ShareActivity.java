package com.wlcxbj.bike.activity;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.other.InviteCodeToken;
import com.wlcxbj.bike.net.beanutil.HttpAccountOtherBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.view.CustomDialog;

/**
 * Created by bain on 16-11-28.
 */
public class ShareActivity extends BaseActivity {
    @Bind(R.id.ib_back)
    FrameLayout titleBack;
    @Bind(R.id.invite_now)
    Button inviteNow;
    @Bind(R.id.tv_inviteCode)
    TextView tvInviteCode;
    @Bind(R.id.copy_code)
    TextView copyCode;


    private Dialog dialog;
    private static final String TAG = "ShareActivity";
    String[] plateforms = {
            "Wechat",   //微信
            "QQ",       //QQ
            "WechatMoments",//微信朋友圈
            "SinaWeibo",//新浪微博
    };
    private HttpAccountOtherBeanUtil httpAccountOtherBeanUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        httpAccountOtherBeanUtil = new HttpAccountOtherBeanUtil(this);
//        setInviceCode();
        Log.e("Share", getResources().getDisplayMetrics().density + "");

        //测试
        httpAccountOtherBeanUtil.modifyInviceCode(mAuthNativeToken.getAuthToken().getAccess_token
                (), "xxxx", new HttpCallbackHandler() {


            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onFailure(Exception error, String msg) {

            }
        });
    }

    //设置邀请码
    private void setInviceCode() {
        httpAccountOtherBeanUtil.getInviteCode(mAuthNativeToken.getAuthToken().getAccess_token(),
                new HttpCallbackHandler<InviteCodeToken>() {
                    @Override
                    public void onSuccess(final InviteCodeToken inviteCodeToken) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvInviteCode.setText(inviteCodeToken.getInviteCode());
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        LogUtil.e(TAG, "获取查询邀请码信息失败:" + msg);
                    }
                });
    }

    @OnClick({R.id.ib_back, R.id.copy_code, R.id.invite_now})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.copy_code:
                onClickCopy();
                break;
            case R.id.invite_now:
//                showShare(this, null, true);
                showShareDialog();
                break;
        }
    }

    private void showShareDialog() {
        final CustomDialog dialog = new CustomDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_share);
        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ll_wechat:
                        showShare(getApplicationContext(), plateforms[0], true);
                        break;
                    case R.id.ll_qq:
                        showShare(getApplicationContext(), plateforms[1], true);
                        break;
                    case R.id.ll_wechat_circle:
                        showShare(getApplicationContext(), plateforms[2], true);
                        break;
                    case R.id.ll_sina:
                        showShare(getApplicationContext(), plateforms[3], true);
                        break;
                    case R.id.tv_cancel:
                        dialog.dismiss();
                        break;
                }
            }
        };
        dialog.findViewById(R.id.ll_wechat).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.ll_qq).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.ll_wechat_circle).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.ll_sina).setOnClickListener(onClickListener);
        dialog.findViewById(R.id.tv_cancel).setOnClickListener(onClickListener);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(attributes);

    }


    public void onClickCopy() {
        // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(tvInviteCode.getText());
        Toast.makeText(this, getResources().getString(R.string.tip_177), Toast.LENGTH_LONG).show();
    }

    /**
     * 演示调用ShareSDK执行分享
     *
     * @param context
     * @param platformToShare 指定直接分享平台名称（一旦设置了平台名称，则九宫格将不会显示）
     * @param showContentEdit 是否显示编辑页
     */
    public void showShare(Context context, String platformToShare, boolean showContentEdit) {
        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(!showContentEdit);
        if (platformToShare != null) {
            oks.setPlatform(platformToShare);
        }
        //ShareSDK快捷分享提供两个界面第一个是九宫格 CLASSIC  第二个是SKYBLUE
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        // 在自动授权时可以禁用SSO方式
        oks.disableSSOWhenAuthorize();
        //oks.setAddress("12345678901"); //分享短信的号码和邮件的地址

        oks.setTitle(getResources().getString(R.string.share_title));
        oks.setTitleUrl("http://mob.com");
        oks.setText(getResources().getString(R.string.share_text));
        //oks.setImagePath("/sdcard/test-pic.jpg");  //分享sdcard目录下的图片
        oks.setImageUrl("http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg");
        oks.setUrl("http://www.mob.com"); //微信不绕过审核分享链接
        //oks.setFilePath("/sdcard/test-pic.jpg");
        // filePath是待分享应用程序的本地路劲，仅在微信（易信）好友和Dropbox中使用，否则可以不提供
        oks.setComment("分享"); //我对这条分享的评论，仅在人人网和QQ空间使用，否则可以不提供
        oks.setSite("ShareSDK");  //QZone分享完之后返回应用时提示框上显示的名称
        oks.setSiteUrl("http://mob.com");//QZone分享参数
        oks.setVenueName("ShareSDK");
        oks.setVenueDescription("This is a beautiful place!");
        // 将快捷分享的操作结果将通过OneKeyShareCallback回调
        //oks.setCallback(new OneKeyShareCallback());
        // 去自定义不同平台的字段内容
        //oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
        // 在九宫格设置自定义的图标
        Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
        String label = "ShareSDK";
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {

            }
        };
        oks.setCustomerLogo(logo, label, listener);

        // 为EditPage设置一个背景的View
        //oks.setEditPageBackground(getPage());
        // 隐藏九宫格中的新浪微博
        // oks.addHiddenPlatform(SinaWeibo.NAME);
        // String[] AVATARS = {
        // 		"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
        // 		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
        // 		"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
        // 		"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
        // 		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
        // 		"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg" };
        // oks.setImageArray(AVATARS);              //腾讯微博和twitter用此方法分享多张图片，其他平台不可以

        // 启动分享
        oks.show(context);
    }


    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_share);
    }

    private void showModifyDialog() {
        dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_modify_invitecode);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "点击了确定，修改邀请码");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
