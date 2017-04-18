package com.wlcxbj.bike.util;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.view.SwipeLayout;

import java.security.PrivilegedAction;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by itsdon on 17/4/13.
 */

public class DialogUtil {



    public static void showDoubleButtonDialog(Context context, String title, final DoubleButtonListener listener){
      showDoubleButtonDialog(context,title,"","","","",listener);
    }

    public static void showDoubleButtonDialog(Context context,String title,String info1,final  DoubleButtonListener listener){
       showDoubleButtonDialog(context,title,info1,"","","",listener);
    }

    public static void showDoubleButtonDialog(Context context,String title,String info1,String info2,final DoubleButtonListener listener){
        showDoubleButtonDialog(context,title,info1,info2,"","",listener);
    }

    public static void showDoubleButtonDialog(Context context,String title,int leftBtnTxt,int rightBtnTxt,final DoubleButtonListener listener){
       showDoubleButtonDialog(context,title,"","",context.getString(leftBtnTxt),context.getString(rightBtnTxt),listener);
    }



    public static void showDoubleButtonDialog(Context context, String title, String info1, String info2, String leftBtnTxt, String rightBtnTxt
            , final DoubleButtonListener listener){
        final Dialog  dialog = new Dialog(context, R.style.CustomDialogStyle);
        dialog.setContentView(R.layout.dialog_double_btn);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams p =  dialogWindow.getAttributes();
        p.width = CommonUtil.getScreenWidth(context) - 2 * DpPxUtil.dip2px(context,30);

        TextView title_tv = (TextView) dialog.findViewById(R.id.tv_title);
        TextView info1_tv = (TextView) dialog.findViewById(R.id.tv_info1);
        TextView info2_tv = (TextView) dialog.findViewById(R.id.tv_info2);
        Button left_btn = (Button) dialog.findViewById(R.id.btn_left);
        Button right_btn = (Button) dialog.findViewById(R.id.btn_right);

        if(!TextUtils.isEmpty(title)){
            title_tv.setText(title);
        }

         if(TextUtils.isEmpty(info1)){
             info1_tv.setVisibility(View.GONE);
         }else{
             info1_tv.setText(title);
         }

        if(TextUtils.isEmpty(info2)){
            info2_tv.setVisibility(View.GONE);
        }else{
            info2_tv.setText(info2);
        }

        if(!TextUtils.isEmpty(leftBtnTxt)){
            left_btn.setText(leftBtnTxt);
        }

        if(!TextUtils.isEmpty(rightBtnTxt)){
            right_btn.setText(leftBtnTxt);
        }

        if(listener != null){
            left_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    listener.onLeftBtnClick();
                }
            });
            right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRightBtnClick();
                    dialog.dismiss();
                }
            });
        }
        dialog.show();

    }


    /**
     *  显示正常版本升级提示框
     */
    public static void showVersionUpdateDialog(Context context, String updateInfo, final DoubleButtonListener listener){
     final Dialog  dialog = new Dialog(context, R.style.CustomDialogStyle);
        dialog.setContentView(R.layout.dialog_version_update);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams p =  dialogWindow.getAttributes();
        p.width = CommonUtil.getScreenWidth(context) - 2 * DpPxUtil.dip2px(context,30);

        TextView updateInfo_tv = (TextView) dialog.findViewById(R.id.tv_updateInfo);
        Button cancleDownload_btn = (Button) dialog.findViewById(R.id.btn_cancelDownload);
        Button downloadNow_btn = (Button) dialog.findViewById(R.id.btn_downloadNow);
        GifView gifView = (GifView) dialog.findViewById(R.id.gif1);
        gifView.play();
        if(!TextUtils.isEmpty(updateInfo)){
            updateInfo_tv.setText(updateInfo);
        }
        if(listener != null){
            cancleDownload_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onLeftBtnClick();
                    dialog.dismiss();
                }
            });
            downloadNow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRightBtnClick();
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }

    /**
     * 显示强制版本升级提示框
     */
    public static void showVersionUpdateForceDialog(Context context, String updateInfo, final View.OnClickListener listener){
        final Dialog  dialog = new Dialog(context, R.style.CustomDialogStyle);
        dialog.setContentView(R.layout.dialog_version_update_force);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams p =  dialogWindow.getAttributes();
        p.width = CommonUtil.getScreenWidth(context) - 2 * DpPxUtil.dip2px(context,30);

        TextView updateInfo_tv = (TextView) dialog.findViewById(R.id.tv_updateInfo);
        TextView downloadNow_tv = (TextView) dialog.findViewById(R.id.tv_downloadNow);
        GifView gifView = (GifView) dialog.findViewById(R.id.gif1);
        gifView.play();
        if(!TextUtils.isEmpty(updateInfo)){
            updateInfo_tv.setText(updateInfo);
        }
        downloadNow_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onClick(view);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();

    }

    /**
     *  显示修改邀请码输入框
     * @param context
     * @param listener
     */
    public static void showModifyInvitecodeDialog(final Context context, final ModifyInviteCodeButtonListener listener){
        final Dialog  dialog = new Dialog(context, R.style.CustomDialogStyle);
        dialog.setContentView(R.layout.dialog_modify_invitecode);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams p =  dialogWindow.getAttributes();
        p.width = CommonUtil.getScreenWidth(context) - 2 * DpPxUtil.dip2px(context,30);

        final EditText newCode_et = (EditText) dialog.findViewById(R.id.et_newInviteCode);
        Button left_btn = (Button) dialog.findViewById(R.id.btn_left_modifyInviteCode);
        Button right_btn = (Button) dialog.findViewById(R.id.btn_right_modifyInviteCode);



        if(listener != null){
            left_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            right_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String newCode = newCode_et.getText().toString().trim();
                    if(TextUtils.isEmpty(newCode)){
                        ToastUtil.show(context,"邀请码不能为空");
                        return;
                    }
                    listener.onRightBtnClick(newCode);
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }


   public interface DoubleButtonListener{
        void onLeftBtnClick();
        void onRightBtnClick();
    }

    public interface ModifyInviteCodeButtonListener{
        void onRightBtnClick(String newCode);
    }

}
