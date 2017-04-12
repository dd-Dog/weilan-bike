package com.wlcxbj.bike.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.amap.api.maps.MapView;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.history.RentBikeBean;
import com.wlcxbj.bike.bean.history.RentDetailToken;
import com.wlcxbj.bike.global.Error;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.net.beanutil.HttpHistoryBeanUtil;
import com.wlcxbj.bike.util.TimeUtil;
import com.wlcxbj.bike.util.ToastUtil;
import com.wlcxbj.bike.util.map.AmapUtil;


/**
 * Created by bain on 16-11-29.
 */
public class RouteDetailActivity extends BaseActivity implements View.OnClickListener {


    private MapView mapView;
    String[] plateforms = {
            "Wechat",   //微信
            "QQ",       //QQ
            "WechatMoments",//微信朋友圈
            "SinaWeibo",//新浪微博
    };
    private TextView endTime;
    private TextView startTime;
    private TextView startPoint;
    private TextView endPoint;
    private TextView tid;
    private TextView money;
    private RentBikeBean routebean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        routebean = (RentBikeBean) getIntent().getSerializableExtra("routebean");
//        getRentHistoryDetail(routebean.getId());
        initView(savedInstanceState);
        update(routebean);
        LatLonPoint startPoint = AmapUtil.createLatLonPointFromStr(routebean
                .getBikeStartLat(), routebean.getBikeStartLng());
        LatLonPoint endPoint = AmapUtil.createLatLonPointFromStr(routebean
                .getBikeEndLat(), routebean.getBikeEndLng());
        if (startPoint != null && endPoint != null) {
            searchRouteResult(startPoint, endPoint);
        }
    }

    private void initView(@Nullable Bundle savedInstanceState) {
        findViewById(R.id.ib_back).setOnClickListener(this);
        findViewById(R.id.tv_right).setOnClickListener(this);
        startTime = (TextView) findViewById(R.id.start_time);
        endTime = (TextView) findViewById(R.id.end_time);
        startPoint = (TextView) findViewById(R.id.start_point);
        endPoint = (TextView) findViewById(R.id.end_point);
        tid = (TextView) findViewById(R.id.tid);
        money = (TextView) findViewById(R.id.money);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
    }

    /**
     * 获取单个租车记录详情
     */
    private void getRentHistoryDetail(final long rentId) {
        HttpHistoryBeanUtil httpHistoryBeanUtil = new HttpHistoryBeanUtil(this);
        httpHistoryBeanUtil.getRentDetial(mAuthNativeToken.getAuthToken().getAccess_token(),
                rentId, new HttpCallbackHandler<RentDetailToken>() {

                    @Override
                    public void onSuccess(final RentDetailToken rentDetailToken) {
                        if (rentDetailToken.getErrcode() == Error.OK) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    update(rentDetailToken.getRentDetail());
                                }
                            });
                        } else {
                            ToastUtil.showUIThread(RouteDetailActivity.this, rentDetailToken
                                    .getErrmsg());
                        }
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        ToastUtil.showUIThread(RouteDetailActivity.this, msg);
                    }
                });
    }

    private void update(RentBikeBean rentBikeBean) {
        if (rentBikeBean == null) return;
        startTime.setText(TimeUtil.getTimeStr(rentBikeBean.getStartTime()));
        endTime.setText(TimeUtil.getTimeStr(rentBikeBean.getEndTime()));
        if (TextUtils.isEmpty(rentBikeBean.getBikeStartLat()) || TextUtils.isEmpty(rentBikeBean
                .getBikeStartLng()) || TextUtils.isEmpty(rentBikeBean.getBikeEndLat()) || TextUtils
                .isEmpty(rentBikeBean.getBikeEndLng())) {
            return;
        }
        AmapUtil.getBuildingAddress(this, new LatLonPoint(Double.parseDouble(rentBikeBean
                .getBikeStartLat()), Double.parseDouble(rentBikeBean
                .getBikeStartLng())), startPoint);
        AmapUtil.getBuildingAddress(this, new LatLonPoint(Double.parseDouble(rentBikeBean
                .getBikeEndLat()), Double.parseDouble(rentBikeBean
                .getBikeEndLng())), endPoint);
        money.setText(getResources().getString(R.string.currency_symble) + rentBikeBean.getAmount
                ());
        tid.setText(rentBikeBean.getTid());
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_done_route_detail);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_right:
                showShareDialog();
                break;
        }
    }

    /**
     * 开始搜索路径规划方案
     */
    private RouteSearch mRouteSearch;

    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
        mRouteSearch = new RouteSearch(this);
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                startPoint, endPoint);
        // 步行路径规划
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch
                .WalkDefault);
        mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
    }

    private void showShareDialog() {
        final Dialog dialog = new Dialog(this);
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
        Window window = dialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(attributes);

        dialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
}
