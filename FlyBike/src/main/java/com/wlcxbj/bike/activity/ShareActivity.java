package com.wlcxbj.bike.activity;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.amap.api.maps.model.Text;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.other.InviteCodeToken;
import com.wlcxbj.bike.net.beanutil.HttpAccountOtherBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.view.CustomDialog;

import com.wlcxbj.bike.util.ShareUtil;
import com.wlcxbj.bike.util.ToastUtil;
import com.wlcxbj.bike.util.image.ImageHelper;
import com.wlcxbj.bike.view.CustomDialog;

import java.io.IOException;
import java.io.InputStream;

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
    String[] plateforms = {"Wechat","QQ","WechatMoments","SinaWeibo"};
    private HttpAccountOtherBeanUtil httpAccountOtherBeanUtil;
    private boolean isInviteCodeModified = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        httpAccountOtherBeanUtil = new HttpAccountOtherBeanUtil(this);
        Log.e("Share", getResources().getDisplayMetrics().density + "");
        setInviceCode();
        Log.e("Share", getResources().getDisplayMetrics().density + "");

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
                                if(inviteCodeToken.getInviteNote() == null){
                                    tvInviteCode.setText(inviteCodeToken.getInviteCode());
                                }else{
                                    tvInviteCode.setText(inviteCodeToken.getInviteNote());
                                }
                                isInviteCodeModified = !(inviteCodeToken.getInviteNote() == null);
                                LogUtil.d(TAG,"inviteToken ="+inviteCodeToken.toString());
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        LogUtil.e(TAG, "获取查询邀请码信息失败:" + msg);
                    }
                });
    }

    public void modifyInviteCode(final String newInviteCode){
        httpAccountOtherBeanUtil.modifyInviceCode(mAuthNativeToken.getAuthToken().getAccess_token
                (), newInviteCode, new HttpCallbackHandler() {


            @Override
            public void onSuccess(Object o) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvInviteCode.setText(newInviteCode);
                        ToastUtil.show(ShareActivity.this,"邀请码修改成功");
                        isInviteCodeModified = true;
                    }
                });
            }

            @Override
            public void onFailure(Exception error, String msg) {

            }
        });
    }


    @OnClick({R.id.ib_back, R.id.copy_code, R.id.invite_now,R.id.rl_inviteCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.copy_code:
                onClickCopy();
                break;
            case R.id.invite_now:
                showShareDialog();
                break;
            case R.id.rl_inviteCode:
                if(!isInviteCodeModified){
                    showModifyDialog();
                }else{
                    ToastUtil.show(ShareActivity.this,"您的邀请码已修改过");
                }
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
                    case R.id.wechat:
                        ShareUtil.showShare(ShareActivity.this, plateforms[0], true);
                        break;
                    case R.id.ll_qq:
                        ShareUtil.showShare(ShareActivity.this,plateforms[1], true);
                        break;
                    case R.id.ll_wechat_circle:
                        ShareUtil.showShare(ShareActivity.this, plateforms[2], true);
                        break;
                    case R.id.ll_sina:
                        ShareUtil. showShare(ShareActivity.this, plateforms[3], true);
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

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_share);
    }

    private void showModifyDialog() {
        dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_modify_invitecode);
        final EditText newInviteCode_et = (EditText) dialog.findViewById(R.id.et_newInviteCode);
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
                if(TextUtils.isEmpty(newInviteCode_et.getText().toString())){
                    ToastUtil.show(ShareActivity.this,"新的邀请码不能为空");
                }else{
                    modifyInviteCode(newInviteCode_et.getText().toString());
                    dialog.dismiss();
                }
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
