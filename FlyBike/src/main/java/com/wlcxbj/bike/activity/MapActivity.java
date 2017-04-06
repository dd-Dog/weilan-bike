package com.wlcxbj.bike.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.cloud.CloudItem;
import com.amap.api.services.cloud.CloudItemDetail;
import com.amap.api.services.cloud.CloudResult;
import com.amap.api.services.cloud.CloudSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.google.gson.Gson;
import com.inuker.bluetooth.library.search.SearchResult;
import com.meg7.widget.CircleImageView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.ScanResultToken;
import com.wlcxbj.bike.bean.account.AccountToken;
import com.wlcxbj.bike.bean.account.AuthNativeToken;
import com.wlcxbj.bike.bean.account.BasicInfo;
import com.wlcxbj.bike.bean.account.BasicUserInfoToken;
import com.wlcxbj.bike.bean.account.BusinessParamsToken;
import com.wlcxbj.bike.bean.account.BussinessParams;
import com.wlcxbj.bike.bean.bike.BikeBean;
import com.wlcxbj.bike.bean.bike.BikePswBean;
import com.wlcxbj.bike.bean.bike.BikePswToken;
import com.wlcxbj.bike.bean.bike.BikeReserveToken;
import com.wlcxbj.bike.bean.bike.CancelReserveToken;
import com.wlcxbj.bike.bean.bike.EndTripToken;
import com.wlcxbj.bike.bean.oss.AvatarURL2OSSParamsUtil;
import com.wlcxbj.bike.bean.oss.OSSFileParams;
import com.wlcxbj.bike.bean.trip.TripPointBean;
import com.wlcxbj.bike.bean.trip.TripToken;
import com.wlcxbj.bike.ble.BleDeviceBean;
import com.wlcxbj.bike.ble.encrypt.AES;
import com.wlcxbj.bike.ble.lock.LockCallbackHandler;
import com.wlcxbj.bike.ble.lock.LockManager;
import com.wlcxbj.bike.ble.packet.cmd.Command;
import com.wlcxbj.bike.global.Error;
import com.wlcxbj.bike.net.beanutil.HttpAccountBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpBikeBeanUtil;
import com.wlcxbj.bike.global.ShareBikeApplication;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.net.beanutil.HttpTripBeanUtil;
import com.wlcxbj.bike.receiver.AliMessageCallbackHandlerAdapter;
import com.wlcxbj.bike.util.StringUtil;
import com.wlcxbj.bike.receiver.AliMessageReceiver;
import com.wlcxbj.bike.util.map.AmapUtil;
import com.wlcxbj.bike.util.DpPxUtil;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.TimeUtil;
import com.wlcxbj.bike.util.ToastUtil;
import com.wlcxbj.bike.bean.DdeviceStateResult;
import com.wlcxbj.bike.bean.DeviceResult;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.net.OkhttpHelper;
import com.wlcxbj.bike.util.account.AccountUtil;
import com.wlcxbj.bike.util.bike.BikePwdUtil;
import com.wlcxbj.bike.util.cache.CacheUtil;
import com.wlcxbj.bike.util.image.ImageHelper;
import com.wlcxbj.bike.util.map.MarkerUtil;
import com.wlcxbj.bike.util.map.SensorEventHelper;
import com.wlcxbj.bike.util.properties.PropertiesUtil;


public class MapActivity extends BaseActivity implements View.OnClickListener, LocationSource {
    private static final int REQUEST_SEARCH_KEYWORD = 19980;
    public static final int LENGTH_BT_PB = 1;
    private static final long RETURN_MYLOC_TIME = 800;
    private static final float VELOCITY_X = 5;
    private static final float VELOCITY_Y = 5;
    public static final int REQUEST_LOGIN = 2222;
    private static final int MSG_UNLOCK_BLUETOOTH = 3333;
    private static final int MAX_BLUETOOTH_PB = 500;
    private static final long DELAY_BT_PB = 20;
    private static final int MSG_UNLOCK_BLE = 12311;
    public static final String ACCOUNT_TOKEN = "account_token";
    public static final String AUTH_NATIVE_TOKEN = "auth_token";
    public static final String USER_ICON_BEAN = "user_icon_bean";
    private static final int REFRESH_BIKES = 9;
    private static final int REFRESH_BIKES_DELAYED = 30 * 1000;
    private LatLonPoint mStartPoint = null;//起点，116.335891,39
    // .942295
    private LatLonPoint mEndPoint = null;//终点，116.481288,39.995576
    private static final int TWICE_BACK_QUIT = 1000;
    private String city;
    private PoiSearch.Query query;
    private PoiResult poiResult;
    private String myAddress;
    private WalkRouteResult mWalkRouteResult;
    private OkhttpHelper okhttpHelper;
    private static final int REQUEST_SCAN_RESULT = 1000;
    public static final String TAG = "MapActivity";
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private MapView mapView;
    private AMap aMap;
    private long start;
    private long end;
    //    private static Point point;
    private Marker currentMarker = null;
    private LinearLayout inUseWindow;
    private Marker markerNow;
    private ProgressDialog progDialog = null;// 搜索时进度条
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    String scanResult = null;//扫描结果
    private SensorManager mSensorManager;
    private float mDegree = 0f;
    private DrawerLayout drawerLayout;
    private Marker screenMarker;
    private GestureDetector gestureDetector;
    private TextView tvUserName;
    private CircleImageView userIcon;
    private TextView tvIdentityState;
    private ImageView ivIdentityPic;
    private TextView creditPoints;
    private Dialog btProgress;
    private ProgressBar pbUnlock;
    private static int msg_unlock_ble = 3;
    private AccountToken mAccountToken;
    private AuthNativeToken mAuthNativeToken;
    private ScanResultToken scanResultToken;
    private HttpBikeBeanUtil httpBikeBeanUtil;
    private HttpTripBeanUtil httpTripBeanUtil;
    private LinearLayout orderWindow;
    private LinearLayout inOrderWindow;
    boolean aMapLocationFlag = true;
    private boolean mFirstFix = false;
    public static final int UNDER_FREE = 61;
    public static final int UNDER_RESERVE = 62;
    public static final int UNDER_BICYCLING = 62;
    public int currentState = UNDER_FREE;
    private OnLocationChangedListener mOnLocationChangedListener;
    private TextView tvUseTime;
    private TextView tvDistance;
    private TextView tvCalory;
    private TextView tvOrderedNum;
    private TextView tvEndPoint2;
    private TextView tvEndPoint1;
    private TextView tvRemainTime;
    private TextView emulateDistance;
    private TextView emulateTime;
    private OSSFileParams mOssParams;
    private BusinessParamsToken mBusinessParamsToken;
    private BikePswToken mBikePswToken;
    private HttpAccountBeanUtil httpAccountBeanUtil;
    private ImageHelper imageHelper;
    private ArrayList<CloudItem> oldBikeList = new ArrayList<>();
    private int codeValue;
    private byte[] decoceArr;
    private CloudSearch mCloudSearch;
    private CloudSearch.Query mQuery;
    private String mTableID;
    private boolean connectToBle;
    private TextView tvBikeId;
    private AliMessageReceiver aliMessageReceiver;
    private SensorEventHelper mSensorHelper;
    private LockManager mLockManager;
    private RelativeLayout mBottomLayout, mHeadLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
       initUtils();
        initView();
        initBle();
        initGestureDetector();
        ShareBikeApplication.getInstance().push(this);
        //请求网络数据
        initMap(savedInstanceState);
        registerAliMessageReceiver();
    }

    /**
     * 把一些初始化操作放在onStart方法中，让activity尽快跳转
     */
    @Override
    protected void onStart() {
        super.onStart();
        initLoc();
        getPersimmions();
        initCloudSearch();
        //register receiver
        if (mLockManager != null && !mLockManager.isBluetoothOpened()) {
            mLockManager.openBluetooth();
        }
        mBusinessParamsToken = CacheUtil.getSerialToken(getApplicationContext(),
                Constants.BUSINESS_PARAMS_FILE);
        if (mBusinessParamsToken == null)
            if (mAuthNativeToken != null)
                if (mAuthNativeToken.getAuthToken() != null)
                    httpAccountBeanUtil.getBusinessParams(mAuthNativeToken.getAuthToken()
                                    .getAccess_token(),
                            new HttpCallbackHandler<BusinessParamsToken>() {

                                @Override
                                public void onSuccess(BusinessParamsToken businessParamsToken) {
                                    mBusinessParamsToken = businessParamsToken;
                                    boolean b = CacheUtil.cacheSerialToken(getApplicationContext(),
                                            Constants.BUSINESS_PARAMS_FILE, businessParamsToken);
                                    LogUtil.e(TAG, "缓存" + (b ? "成功" : "失败"));
                                }

                                @Override
                                public void onFailure(Exception error, String msg) {

                                }
                            });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mapView.onResume();
        //刷新数据显示
        invalidateViewByAccount();

        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        } else {
            mSensorHelper = new SensorEventHelper(this);
            if (mSensorHelper != null) {
                mSensorHelper.registerSensorListener();
                if (mSensorHelper.getCurrentMarker() == null && markerNow != null) {
                    mSensorHelper.setCurrentMarker(markerNow);
                }
            }
        }
        mFirstFix = false;
        LogUtil.e(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        mapView.onPause();
//        deactivate();
        mFirstFix = false;
        LogUtil.e(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        deactivate();
        //取消注册,否则会有内存泄露
//        mSensorManager.unregisterListener(mAmapSersorEventListener);
//        mAmapSersorEventListener = null;
        ShareBikeApplication.getInstance().pop(this);
        unregisterReceiver(aliMessageReceiver);
        //取消定时器
        stopCountDown();
        if (mLockManager != null) {
            mLockManager.destroy();
        }

    }

    //声明定位回调监听器
    //可以通过类implement方式实现AMapLocationListener接口，也可以通过创造接口类对象的方法实现
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    LatLng location = new LatLng(amapLocation.getLatitude(), amapLocation
                            .getLongitude());
//                    markerNow.setToTop();
                    if (!mFirstFix) {
                        mFirstFix = true;
                        addMarkerNow(location);
                        mSensorHelper.setCurrentMarker(markerNow);//定位图标旋转
                    } else {
                        markerNow.setPosition(location);
                    }
                    city = amapLocation.getCity();

                    myAddress = amapLocation.getAddress();
//                    Log.e(TAG, myAddress + "经纬度:" + amapLocation
//                            .getAltitude() + "," + amapLocation.getLongitude());
                    //定位成功后进行云图检索
                    if (aMapLocationFlag) {
                        cloudSearch(amapLocation);
                        aMapLocationFlag = false;
                    }
                    // 实时位置，计算骑行距离用
                    realTimeLocation = new LatLng(amapLocation.getLatitude(), amapLocation
                            .getLongitude());

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }


    };

    //Marker点击响应事件
    private OnMarkerClickListener mMarkerClicklistener = new OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (!AccountUtil.isLogin(MapActivity.this)) {
                ToastUtil.showUIThread(MapActivity.this, getResources().getString(R.string
                        .tip_137));
                return true;
            }

            if (currentState == UNDER_BICYCLING) {
                return true;
            }
            currentMarker = marker;
            //显示Infowindow
//            currentMarker.showInfoWindow();
            //显示路线规划
            LatLng end = currentMarker.getPosition();
            LatLng start = markerNow.getPosition();
            mStartPoint = new LatLonPoint(start.latitude, start.longitude);
            mEndPoint = new LatLonPoint(end.latitude, end.longitude);
            Log.e(TAG, "startPoint=" + mStartPoint.toString() + ",endPoint=" + mEndPoint.toString
                    ());
            searchRouteResult(mStartPoint, mEndPoint);
            //显示预约窗口
            showOrderWindow(currentMarker);
            AmapUtil.getAddress(MapActivity.this, mEndPoint, tvEndPoint2);
            return true;
        }
    };

    //地图点击监听事件
    private AMap.OnMapClickListener mMapClickListener = new AMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if (currentState != UNDER_FREE) {
                return;
            }
            hideAllWindows();
            //隐藏Infowidow
            currentMarker.hideInfoWindow();
            if (walkRouteOverlay != null) {
                walkRouteOverlay.removeFromMap();
                mBottomLayout.setVisibility(View.INVISIBLE);
            }
        }
    };


    AliMessageCallbackHandlerAdapter aliMessageCallbackHandlerAdapter = new
            AliMessageCallbackHandlerAdapter() {
                @Override
                public void onMessage(Context context, CPushMessage cPushMessage) {
                    super.onMessage(context, cPushMessage);
                    LogUtil.d(TAG, " 收到推送" + cPushMessage.getContent());
                    EndTripToken tempToken = new Gson().fromJson(cPushMessage.getContent(), EndTripToken.class);
                    if (tempToken.getPushMsgSpid() != null) {
                        if (tempToken.getPushMsgSpid().equals("1")) {
                            startTrip();
                        } else if (tempToken.getPushMsgSpid().equals("2")) {
                            endTrip(cPushMessage.getContent());
                        }
                    } else {
                        endTrip(cPushMessage.getContent());
                    }
                }
            };

    /**
     * 路线搜索规划监听器
     */
    private WalkRouteOverlay walkRouteOverlay;
    RouteSearch.OnRouteSearchListener mRouteSearchListener = new RouteSearch
            .OnRouteSearchListener() {

        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int errorCode) {
        }

        @Override
        public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {
        }

        @Override
        public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
            dissmissProgressDialog();
//            aMap.clear();// 清理地图上的所有覆盖物
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        LogUtil.e(TAG, "onWalkRouteSearched");
                        mWalkRouteResult = result;
                        final WalkPath walkPath = mWalkRouteResult.getPaths()
                                .get(0);
                        if (walkRouteOverlay != null) {
                            walkRouteOverlay.removeFromMap();
                        }
                        walkRouteOverlay = new WalkRouteOverlay(
                                MapActivity.this, aMap, walkPath,
                                mWalkRouteResult.getStartPos(),
                                mWalkRouteResult.getTargetPos());
                        //不显示小人
                        walkRouteOverlay.setNodeIconVisibility(false);
                        Log.e(TAG, "mWalkRouteResult_start=" + mWalkRouteResult.getStartPos()
                                .toString() + ",mWalkRouteResult_end=" + mWalkRouteResult
                                .getTargetPos().toString());
                        walkRouteOverlay.addToMap();
                        walkRouteOverlay.zoomToSpan();
//                        mBottomLayout.setVisibility(View.VISIBLE);
                        int dis = (int) walkPath.getDistance();
                        int dur = (int) walkPath.getDuration();
                        String des = AmapUtil.getFriendlyTime(dur) + "(" + AmapUtil
                                .getFriendlyLength(dis) + ")";
                        mRotueTimeDes.setText(des);
                        mRouteDetailDes.setVisibility(View.GONE);
                        mBottomLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MapActivity.this,
                                        WalkRouteDetailActivity.class);
                                intent.putExtra("walk_path", walkPath);
                                intent.putExtra("walk_result",
                                        mWalkRouteResult);
                                startActivity(intent);
                            }
                        });
                    } else if (result != null && result.getPaths() == null) {
                        ToastUtil.show(getApplicationContext(), getResources().getString(R.string
                                .tip_138));
                    }
                } else {
                    ToastUtil.show(getApplicationContext(), getResources().getString(R.string
                            .tip_138));
                }
            } else {
                ToastUtil.showerror(getApplicationContext(), errorCode);
            }
        }

        @Override
        public void onRideRouteSearched(RideRouteResult rideRouteResult, int errorCode) {

        }
    };




    /**
     * 添加定位图标
     *
     * @param location
     * @return
     */
    private boolean addMarkerNow(LatLng location) {
        if (markerNow != null) {
            return true;
        }
        MarkerOptions myLocMarkerOption = new MarkerOptions();
        myLocMarkerOption.position(location);
        myLocMarkerOption.anchor(0.5f, 0.5f);
        myLocMarkerOption.title("我的位置");
        myLocMarkerOption.visible(true);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap
                (BitmapFactory.decodeResource(getResources(), R.mipmap
                        .location_marker));
        myLocMarkerOption.icon(bitmapDescriptor);
        markerNow = aMap.addMarker(myLocMarkerOption);
        markerNow.setRotateAngle(mDegree);
        markerNow.setToTop();
        return false;
    }

    private boolean unlockSuccess = false;
    DecimalFormat df = new DecimalFormat("######0.00");
    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {

        private final MapActivity mapActivity;

        public MyHandler(MapActivity activity) {
            mapActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TWICE_BACK_QUIT:
                    mapActivity.start = 0;
                    mapActivity.end = 0;
                    break;
                case MSG_UNLOCK_BLUETOOTH:
                    mapActivity.pbUnlock.setProgress(mapActivity.pbUnlock.getProgress() +
                            LENGTH_BT_PB);
                    sendEmptyMessageDelayed(MSG_UNLOCK_BLUETOOTH, DELAY_BT_PB);
                    if (mapActivity.pbUnlock.getProgress() == MAX_BLUETOOTH_PB) {
                        mapActivity.dismissBTUnlockDialog();
                        if (!mapActivity.connectToBle) {
                            mapActivity.showManualUnlockHintDialog(mapActivity.getPswStr());
                        }
                    }
                    break;
                case MSG_UNLOCK_BLE:
//                    sendCount = 0;
//                    mapActivity.sendCmd(sendCount);
//                    msg_unlock_ble--;
//                    if (msg_unlock_ble > 0)
//                        sendEmptyMessageDelayed(MSG_UNLOCK_BLE, 1000);
                    break;
                case REFRESH_BIKES:
                    //刷新自行车
                    if (mapActivity.mLocationClient != null) {
//                        cloudSearch(mLocationClient.getLastKnownLocation());
                    }
                    sendEmptyMessageDelayed(REFRESH_BIKES, REFRESH_BIKES_DELAYED);
                    break;
                case COUNT_BIKING_TIME:
                    sendEmptyMessageDelayed(COUNT_BIKING_TIME, COUNT_BIKING_TIME_DELAYED);
                    mapActivity.refreshInUseWindow();
                    break;
                case COUNTING_RESERVE_LEFT_TIME:
                    if (mapActivity.remainReservetime != 0) {
                        mapActivity.remainReservetime--;
                        mapActivity.tvRemainTime.setText(TimeUtil.getMiniteSecondsStr(mapActivity
                                .remainReservetime));
                        sendEmptyMessageDelayed(COUNTING_RESERVE_LEFT_TIME,
                                COUNT_BIKING_TIME_DELAYED);
                    } else {
                        mapActivity.cancelReservation();
                        removeMessages(COUNTING_RESERVE_LEFT_TIME);
                    }
                    break;
                case COUNT_BIKING_DISTANCE:
                    sendEmptyMessageDelayed(COUNT_BIKING_DISTANCE, COUNT_BIKING_DISTANCE_DELAYED);
                    mapActivity.countRideDistance();
                    break;
            }
            super.handleMessage(msg);
        }
    }


    public String getPswStr() {
        if (!Constants.ON_LINE_MODE) {
            return "0x44";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < decoceArr.length; i++) {
            sb.append(decoceArr[i]);
        }
        return sb.toString();
    }


    /**
     * 开始行程
     */
    public void startTrip() {
        LogUtil.e(TAG, "调用开始行程方法");
        httpTripBeanUtil = new HttpTripBeanUtil(this);
        if (mBikePswToken == null) return;
        String tid = mBikePswToken.getTid();
        LogUtil.e(TAG, "startTrip:TID=" + tid);
        String userlng = "";
        String userlat = "";
        String bikelng = "";
        String bikelat = "";
        TripPointBean tripPointBean = new TripPointBean(tid, bikelng, bikelat, userlng, userlat);
        httpTripBeanUtil.startTrip(mAuthNativeToken.getAuthToken().getAccess_token(),
                tripPointBean, new HttpCallbackHandler<TripToken>() {
                    @Override
                    public void onSuccess(TripToken tripToken) {
                        LogUtil.e(TAG, "请求开始骑行成功：" + tripToken);
                        if (tripToken.getErrcode() == Error.OK) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showInUserWindow();
                                }
                            });
                        } else {
                            ToastUtil.showUIThread(MapActivity.this, tripToken.getErrmsg());
                        }
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                btProgress.dismiss();
                                if (unlockSuccess) {
                                    showInUserWindow();
                                } else {
                                    Toast.makeText(getApplicationContext(), getResources().getString
                                            (R.string.tip_157), Toast
                                            .LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });
                    }
                });
    }

    /**
     * 结束骑行
     */
    private void endTrip(String endMsg) {
        EndTripToken endTripToken = new Gson().fromJson(endMsg, EndTripToken.class);
        LogUtil.d(TAG, "行程结束=" + endMsg);
        hideAllWindows();
        handler.removeMessages(COUNT_BIKING_TIME);
        handler.removeMessages(COUNT_BIKING_DISTANCE);
        usingTimeSeconds = 0;
        Intent endtripIntent = new Intent(this, ConsumeResultActivity.class);
//        if (scanResultToken != null) {
        endtripIntent.putExtra("tid", endTripToken.getTid());
        endtripIntent.putExtra("coast", 0.5);
        Bundle bundle = new Bundle();
        bundle.putSerializable("endtriptoken", endTripToken);
        endtripIntent.putExtras(bundle);
        endtripIntent.putExtra("rideDistance", rideDistance);
        rideDistance = 0;
        startActivity(endtripIntent);
        setCurrentState(UNDER_FREE);
//        }
    }

    private void registerAliMessageReceiver() {
        aliMessageReceiver = new AliMessageReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        aliMessageReceiver.setAliCallbackHandler(aliMessageCallbackHandlerAdapter);
        intentFilter.addAction("com.alibaba.push2.action.NOTIFICATION_OPENED");
        intentFilter.addAction("com.alibaba.push2.action.NOTIFICATION_REMOVED");
        intentFilter.addAction("com.taobao.taobao.intent.action.COMMAND");
        intentFilter.addAction("com.taobao.accs.intent.action.COMMAND");
        intentFilter.addAction("com.alibaba.sdk.android.push.RECEIVE");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        registerReceiver(aliMessageReceiver, intentFilter);
    }


    private void initCloudSearch() {
        mTableID = PropertiesUtil.getProperties(this, PropertiesUtil.TABLEID);
        LogUtil.e(TAG, "mTableID=" + mTableID);
//        LogUtil.e(TAG, "开始云图检索");
        // 初始化查询类
        mCloudSearch = new CloudSearch(this);
        // 设置回调函数
        mCloudSearch.setOnCloudSearchListener(new CloudSearch.OnCloudSearchListener() {
            @Override
            public void onCloudSearched(CloudResult cloudResult, int i) {
//                LogUtil.e(TAG, "从云图检索成功,展示数据");
                markBikes(cloudResult);
                screenMarker.setToTop();
            }

            @Override
            public void onCloudItemDetailSearched(CloudItemDetail cloudItemDetail, int i) {

            }
        });
        // 设置中心点及检索范围
    }

    /**
     * 云图检索
     */
    public void cloudSearch(AMapLocation mCenterPoint) {

        CloudSearch.SearchBound bound = new CloudSearch.SearchBound(new LatLonPoint(
                mCenterPoint.getLatitude(), mCenterPoint.getLongitude()), 10000);
        //设置查询条件 mTableID是将数据存储到数据管理台后获得。
        try {
            mQuery = new CloudSearch.Query(mTableID, "", bound);
            mQuery.setPageSize(100);//设置每页个数
        } catch (AMapException e) {
            e.printStackTrace();
        }

        mCloudSearch.searchCloudAsyn(mQuery);// 异步搜索
    }

    /**
     * 标注自行车位置
     *
     * @param cloudResult
     */
    private Map<String, Marker> bikeMarkerMap = new HashMap<>();

    private void markBikes(CloudResult cloudResult) {
        ArrayList<CloudItem> newBikeList = null;
        if (cloudResult == null) return;
        newBikeList = cloudResult.getClouds();
        ArrayList<CloudItem> addBikeList = null;
        addBikeList = MarkerUtil.getAddList(oldBikeList, newBikeList);
        ArrayList<CloudItem> removeBikeList = null;
        removeBikeList = MarkerUtil.getRemoveList(oldBikeList, newBikeList);
        LogUtil.e(TAG, "添加marker个数:" + addBikeList.size());
        LogUtil.e(TAG, "删除marker个数:" + removeBikeList.size());
//        aMap.clear();// 清理地图上的所有覆盖物
        oldBikeList = null;
        oldBikeList = newBikeList;

        //去掉要删除的
        for (CloudItem item : removeBikeList) {
            String tid = item.getCustomfield().get("tid");
            Marker m = bikeMarkerMap.get(tid);
            m.remove();//从地图中删除
            m.destroy();//销毁图片等资源
            bikeMarkerMap.remove(tid);//从列表中删除
        }

        //显示要添加的
        for (CloudItem item : newBikeList) {
            String tid = item.getCustomfield().get("tid");
            //添加marker
            LatLng latLng = new LatLng(item.getLatLonPoint().getLatitude(), item
                    .getLatLonPoint().getLongitude());
            MarkerOptions markerOptions = AmapUtil.createMarker(MapActivity.this, latLng, R.drawable
                    .bicycle_position, tid, "locked");
            Marker marker = aMap.addMarker(markerOptions);
            bikeMarkerMap.put(tid, marker);
        }

        LogUtil.e(TAG, "当前共有可用自行车" + bikeMarkerMap.size() + "辆");
    }


    public void initUtils(){
        okhttpHelper = OkhttpHelper.getInstance();
        httpBikeBeanUtil = new HttpBikeBeanUtil(this);
        httpAccountBeanUtil = new HttpAccountBeanUtil(this);
        imageHelper = new ImageHelper(this);
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        TextView tvBikeNumber = (TextView) findViewById(R.id.bike_number);
        inUseWindow = (LinearLayout) findViewById(R.id.ll_inuse);
        orderWindow = (LinearLayout) findViewById(R.id.ll_order);
        inOrderWindow = (LinearLayout) findViewById(R.id.ll_inorder);
        LinearLayout fab = (LinearLayout) findViewById(R.id.scanBar);
        tvUserName = (TextView) findViewById(R.id.username);
        userIcon = (CircleImageView) findViewById(R.id.user_icon);
        tvIdentityState = (TextView) findViewById(R.id.identify_state);
        ivIdentityPic = (ImageView) findViewById(R.id.user_identity_pic);
        creditPoints = (TextView) findViewById(R.id.points_number);
        TextView title = (TextView) findViewById(R.id.tv_title);
        tvUseTime = (TextView) findViewById(R.id.use_time);
        tvDistance = (TextView) findViewById(R.id.ride_distance);
        tvCalory = (TextView) findViewById(R.id.calory);
        tvOrderedNum = (TextView) findViewById(R.id.tv_ordered_bikenum);
        tvEndPoint2 = (TextView) findViewById(R.id.tv_endpoint2);
        tvEndPoint1 = (TextView) findViewById(R.id.tv_endpoint);
        tvRemainTime = (TextView) findViewById(R.id.tv_remain_time);
        emulateTime = (TextView) findViewById(R.id.emulate_time);
        emulateDistance = (TextView) findViewById(R.id.tv_emulate_distance);

        if (!Constants.ON_LINE_MODE) {
            title.setText("微蓝畅享");
        }

        fab.setOnClickListener(this);
        findViewById(R.id.locate).setOnClickListener(this);
        findViewById(R.id.iv_icon).setOnClickListener(this);
        findViewById(R.id.iv_search).setOnClickListener(this);
        findViewById(R.id.user_wallet).setOnClickListener(this);
        findViewById(R.id.user_discount).setOnClickListener(this);
        findViewById(R.id.user_track).setOnClickListener(this);
        findViewById(R.id.user_msg).setOnClickListener(this);
        findViewById(R.id.user_friends).setOnClickListener(this);
        findViewById(R.id.user_guide).setOnClickListener(this);
        findViewById(R.id.about_us).setOnClickListener(this);
        findViewById(R.id.user_settings).setOnClickListener(this);
        findViewById(R.id.service).setOnClickListener(this);
        findViewById(R.id.btn_order).setOnClickListener(this);
        findViewById(R.id.btn_cancelOrder).setOnClickListener(this);
        findViewById(R.id.ll_creditPoints).setOnClickListener(this);
        tvUserName.setOnClickListener(this);
        findViewById(R.id.ll_identify).setOnClickListener(this);
        userIcon.setOnClickListener(this);

        invalidateViewByAccount();
    }

    private void initBle() {
        mLockManager = new LockManager(this);
        mLockManager.setLockCallbackHandler(new LockCallbackHandler() {
            @Override
            public void onSuccess(SearchResult device, final byte cmdId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (cmdId) {
                            case Command.CMD_ID_SET_SERVER:
                                Toast.makeText(getApplicationContext(), "设置服务器成功", Toast
                                        .LENGTH_SHORT).show();
                                break;
                            case Command.CMD_ID_UNLOCK:
                                Toast.makeText(getApplicationContext(), "开锁成功", Toast
                                        .LENGTH_SHORT).show();
                                startTrip();
                                unlockSuccess = true;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissBTUnlockDialog();
                                    }
                                });
                                break;
                        }
                    }
                });
            }

            @Override
            public void onLocated() {

            }

            @Override
            public void onFail(SearchResult device, int code, final String msg, final byte cmdId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (cmdId) {
                            case Command.CMD_ID_SET_SERVER:
                                Toast.makeText(getApplicationContext(), "设置服务器失败", Toast
                                        .LENGTH_SHORT).show();
                                break;
                            case Command.CMD_ID_UNLOCK:
                                Toast.makeText(getApplicationContext(), "开锁失败," + msg, Toast
                                        .LENGTH_SHORT).show();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissBTUnlockDialog();
                                        showManualUnlockHintDialog(getPswStr());
                                    }
                                });
                                break;
                        }
                    }
                });
            }
        });
    }

    private void initGestureDetector() {
        /**
         * 手势识别器
         */
        gestureDetector = new GestureDetector(getApplicationContext(), new
                GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                            float distanceY) {
//                        Log.e(TAG, "onScroll");
                        return false;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float
                            velocityY) {
                        if (Math.abs(velocityX) >= VELOCITY_X || Math.abs(velocityY) >=
                                VELOCITY_Y) {
                            startJumpAnimation();
                        }
                        Log.e(TAG, "onFling");
                        return false;
                    }
                });
    }


    /**
     * 开始搜索路径规划方案
     */
    private RouteSearch mRouteSearch;

    public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
        if (mStartPoint == null) {
            ToastUtil.show(this, getResources().getString(R.string.tip_143));
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(this, getResources().getString(R.string.tip_144));
        }
//        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                startPoint, endPoint);
        // 步行路径规划
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch
                .WalkDefault);
        mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询
    }

    /**
     * 初始化定位
     */
    private void initLoc() {
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }

    }

    /**
     * 初始化地图
     *
     * @param savedInstanceState
     */
    private void initMap(Bundle savedInstanceState) {
        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mapView.onCreate(savedInstanceState);

        //初始化地图变量
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(mRouteSearchListener);

//        //显示默认定位坐标按钮
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

//        setupLocationStyle();
        //隐藏缩放按钮,显示比例尺
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.setOnMarkerClickListener(mMarkerClicklistener);
        aMap.setOnMapClickListener(mMapClickListener);

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                //屏幕中心marker
                addMarkerInScreenCenter();
                moveToMyLoc();
                Log.d(TAG, "地图加载完成");
                handler.sendEmptyMessageDelayed(REFRESH_BIKES, REFRESH_BIKES_DELAYED);
            }
        });

        //地图视力区域移动监听
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

            }
        });
        //地图触摸监听（由于地图拦截了Activity的onTouchEvent，所以需要使用地图的onTouchEvent）
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
            }
        });

    }


    /**
     * 在屏幕中心地图上添加marker
     */
    private void addMarkerInScreenCenter() {
        //getCameraPosition（）返回地图的区域的当前位置
        //target目标位置的屏幕中心点经纬度坐标
        LatLng screenCenter = aMap.getCameraPosition().target;
        Point centerPoint = aMap.getProjection().toScreenLocation(screenCenter);
        //定义marker的在屏幕的锚点，因为是屏幕中心，所以x,y都是0.5f
        screenMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)//定义marker的在屏幕的锚点，因为是屏幕中心，所以x,y都是0.5f
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location)));
        //设置Marker在屏幕上不跟随地图移动
        screenMarker.setPositionByPixels(centerPoint.x, centerPoint.y);
        screenMarker.setToTop();
        Log.e(TAG, "添加屏幕中心marker");
    }

    /**
     * 屏幕中心Marker跳动动画
     */
    public void startJumpAnimation() {

        if (screenMarker != null) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = screenMarker.getPosition();
            Point point = aMap.getProjection().toScreenLocation(latLng);
            point.y -= DpPxUtil.dip2px(this, 125);
            LatLng target = aMap.getProjection().fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            screenMarker.setAnimation(animation);
            //开始动画
            screenMarker.startAnimation();

        } else {
            Log.e(TAG, "screenMarker is null");
        }
        screenMarker.setToTop();
    }

    //返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) return;
        switch (requestCode) {
            case REQUEST_SCAN_RESULT:
                if (resultCode == RESULT_OK) {
                    scanResult = data.getStringExtra("result");
                    Log.e(TAG, "scanResult=" + scanResult);
                    if (TextUtils.isEmpty(scanResult)) {
                        String serial_number = data.getStringExtra("serial_number");
                        scanResultToken = new ScanResultToken("", serial_number);
                        getBikePassword(serial_number);
                    } else {
                        scanResultToken = new ScanResultToken(scanResult);
                        getBikePassword(scanResultToken.bikeno);
                    }
                    showBTUnlockDialog();
                }
                break;
            case REQUEST_SEARCH_KEYWORD:
                if (resultCode == RESULT_OK) {
                    String key_word = data.getStringExtra("key_word");
//                    Toast.makeText(getApplicationContext(), "搜索" + key_word, Toast
// .LENGTH_SHORT).show();
                    doSearchQuery(key_word);
                }
                break;
            case REQUEST_LOGIN:
                if (resultCode == RESULT_OK) {
                    //login success
                    invalidateViewByAccount();
                }
                break;
        }
    }

    /**
     * 请求车辆密码
     */
    public void getBikePassword(final String tno) {
        if (currentState == UNDER_RESERVE) {
            cancelReservation();
        }
        String plateno = "";
        String userlng = "";
        String userlat = "";
        final BikePswBean bikePswBean = new BikePswBean(tno, plateno, userlng, userlat);
        if (mAuthNativeToken != null) {
            httpBikeBeanUtil.getBikePsw2(mAuthNativeToken.getAuthToken().getAccess_token(),
                    bikePswBean, new HttpCallbackHandler<BikePswToken>() {
                        @Override
                        public void onSuccess(BikePswToken bikePswToken) {
                            LogUtil.e(TAG, "请求密码成功:" + bikePswToken);
                            mBikePswToken = bikePswToken;
                            if (TextUtils.equals(bikePswToken.getErrcode() + "", Error.OK + "")) {
                                codeValue = Integer.parseInt(bikePswToken.getUnlockCode());
                                decoceArr = BikePwdUtil.decoce(codeValue);
                                String mac = bikePswToken.getMac();
                                LogUtil.e(TAG, "decoceArr=" + AES.byte2hex(decoceArr));
                                unlockBike(mac, tno);
                            } else {
                                ToastUtil.showUIThread(MapActivity.this, bikePswToken.getErrmsg());
                            }
                        }

                        @Override
                        public void onFailure(Exception error, String msg) {
                            dismissBTUnlockDialog();
                            ToastUtil.showUIThread(MapActivity.this, msg);
                        }
                    });
        }
    }

    /**
     * 获取密码后，处理开锁流程
     *
     * @param mac
     * @param tno
     */
    private void unlockBike(String mac, final String tno) {
        //车锁有蓝牙
        if (mLockManager != null && mLockManager.isBleSupported() && mLockManager
                .isBluetoothOpened()) {
            mLockManager.unlock(mac, decoceArr);
            //设置启用蓝牙开锁，并且蓝牙打开的状态
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    showBTUnlockDialog();
//                    Log.e(TAG, "获取密码成功，使用BLE开锁");
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showManualUnlockHintDialog(getPswStr());
                }
            });
        }
    }

    /**
     * update views through account data
     */
    private void invalidateViewByAccount() {
        LogUtil.e(TAG, "更新数据显示");
        boolean exists = CacheUtil.isCacheExists(this, Constants.AUTH_CACHE_FILE_NAME);

        if (!exists) {
            ToastUtil.showUIThread(this, getResources().getString(R.string.tip_147));
            tvIdentityState.setText(R.string.not_login);
            tvIdentityState.setTextColor(getResources().getColor(R.color.green));
            ivIdentityPic.setImageResource(R.drawable.id_number_green);
            userIcon.setImageResource(R.drawable.user_profile);
            tvUserName.setText(R.string.not_login_username);
            return;
        }

        mAccountToken = CacheUtil.getSerialToken(this, Constants.ACCOUNT_TOKEN_CACHE_FILE_NAME);
        mAuthNativeToken = CacheUtil.getSerialToken(this, Constants.AUTH_CACHE_FILE_NAME);
        if (mAuthNativeToken == null) {
            LogUtil.e(TAG, "invalidateViewByAccount():mAuthNativeToken==NULL");
            return;
        }
        if (mAccountToken == null) {
            LogUtil.e(TAG, "invalidateViewByAccount():mAccountToken==NULL");
            httpAccountBeanUtil.getUserInfos(mAuthNativeToken.getAuthToken().getAccess_token(),
                    new HttpCallbackHandler<AccountToken>() {

                        @Override
                        public void onSuccess(AccountToken accountToken) {
                            mAccountToken = accountToken;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvUserName.setText(mAccountToken.getRealInfo().getRealName());
                                    setIdentifyShow();
                                    CacheUtil.cacheSerialToken(getApplicationContext(), Constants
                                            .ACCOUNT_TOKEN_CACHE_FILE_NAME, mAccountToken);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Exception error, String msg) {

                        }
                    });
            return;
        }
        String nickName = mAccountToken.getBasicInfo().getNickName();
        tvUserName.setText(TextUtils.isEmpty(nickName) ? mAuthNativeToken.getAuthToken()
                .getMobile() : nickName);
        setIdentifyShow();
        updateCreditPoint(mAccountToken.getAccount().getPoint());

//        userIcon.setImageBitmap(Cach);
        //图片参数应该从后台获取
        httpAccountBeanUtil.getBasicUserInfo(mAuthNativeToken.getAuthToken().getAccess_token(),
                new HttpCallbackHandler<BasicUserInfoToken>() {
                    @Override
                    public void onSuccess(BasicUserInfoToken basicUserInfoToken) {
                        BasicInfo basicInfo = basicUserInfoToken.getBasicInfo();
                        String avatarUrl = basicInfo.getAvatarUrl();
                        if (avatarUrl != null) {
                            mOssParams = new AvatarURL2OSSParamsUtil(avatarUrl)
                                    .getOSSParams();
                            LogUtil.e(TAG, "userIconUrl from server:" + mOssParams);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LogUtil.e(TAG, "refresh usericon:" + mOssParams.getFileName());
                                    imageHelper.clear();
                                    imageHelper.display(userIcon, mOssParams);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {

                    }
                });

    }

    private void setIdentifyShow() {
        if (mAccountToken.getRealInfo().getVerifySpid() == 1) {
            tvIdentityState.setText(R.string.under_check);
            tvIdentityState.setTextColor(getResources().getColor(R.color.green));
            ivIdentityPic.setImageResource(R.drawable.id_number_green);
        } else if (mAccountToken.getRealInfo().getVerifySpid() == 2) {
            tvIdentityState.setText(R.string.check_done);
            tvIdentityState.setTextColor(getResources().getColor(R.color.user_text));
            ivIdentityPic.setImageResource(R.drawable.id_number);
        } else {
            tvIdentityState.setText(R.string.not_identify);
            tvIdentityState.setTextColor(getResources().getColor(R.color.green));
            ivIdentityPic.setImageResource(R.drawable.id_number_green);
        }
    }


    public void updateCreditPoint(String value) {
        creditPoints.setText(value);
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(String keyWord) {
        int currentPage = 0;
        showProgressDialog();// 显示进度框
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query = new PoiSearch.Query(keyWord, "", city);
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult result, int rCode) {
                dissmissProgressDialog();// 隐藏对话框
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.getQuery() != null) {// 搜索poi的结果
                        if (result.getQuery().equals(query)) {// 是否是同一条
                            poiResult = result;
                            // 取得搜索到的poiitems有多少页
                            List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                            List<SuggestionCity> suggestionCities = poiResult
                                    .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                            if (poiItems != null && poiItems.size() > 0) {
                                aMap.clear();// 清理之前的图标
                                PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                                poiOverlay.removeFromMap();
                                poiOverlay.addToMap();
                                poiOverlay.zoomToSpan();
                                aMap.clear();
                            } else if (suggestionCities != null
                                    && suggestionCities.size() > 0) {
                                showSuggestCity(suggestionCities);
                            } else {
                                ToastUtil.show(MapActivity.this, getResources().getString(R
                                        .string.tip_148));
                            }
                        }
                    } else {
                        ToastUtil.show(MapActivity.this, getResources().getString(R.string
                                .tip_148));
                    }
                } else {
                    ToastUtil.showerror(MapActivity.this, rCode);
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(this, infomation);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_icon) {
            drawerLayout.openDrawer(Gravity.LEFT);
            return;
        }

        if (v.getId() == R.id.iv_search) {
            Intent search = new Intent(this, SearchActivity.class);
            search.putExtra("myaddress", myAddress);
            if (mLocationClient != null && mLocationClient.getLastKnownLocation() != null)
                search.putExtra("mycity", mLocationClient.getLastKnownLocation().getCity());
            search.putExtras(getAuthBundle());
            startActivityForResult(search, REQUEST_SEARCH_KEYWORD);
            return;
        }

        if (v.getId() == R.id.service) {
            showServiceDialog();
            return;
        }

        if (v.getId() == R.id.btn_order) {
            reserveBike();
            return;
        }

        if (v.getId() == R.id.btn_cancelOrder) {
            cancelReservation();
            return;
        }
        if (v.getId() == R.id.locate) {
            //  unlockByBlueTooth();
            //重新定位
            Log.d(TAG, "重新定位");
            moveToMyLoc();
            return;
        }

        if (!AccountUtil.isLogin(this)) {
            login();
            return;
        }
        switch (v.getId()) {
            case R.id.service:
                showServiceDialog();
//                connectScannedDeviceDirectly("E8:EB:11:09:61:38");
//                showBTUnlockDialog();
//                startActivity(new Intent(this, CreditPointsActivity.class));
                break;

            case R.id.scanBar:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager
                            .PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                SDK_PERMISSION_REQUEST);
                    }
                }
                if (!AccountUtil.isLogin(this)) {
                    ToastUtil.showUIThread(this, getResources().getString(R.string.tip_149));
                    return;
                }
                String deposit = "";
                if (mBusinessParamsToken != null) {
                    ArrayList<BussinessParams> bizparamModels = mBusinessParamsToken
                            .getBizparamModels();
                    if (bizparamModels != null) {
                        for (int i = 0; i < bizparamModels.size(); i++) {
                            if (TextUtils.equals(bizparamModels.get(i).getName(),
                                    "GUARANTEE_DEPOSIT_AMOUNT")) {
                                deposit = bizparamModels.get(i).getValue();
                            }
                        }
                    }
                }
                LogUtil.e(TAG, "deposit=" + deposit);
//                if (!TextUtils.isEmpty(deposit)) {
//                    double v1 = Double.parseDouble(deposit);
//                    String guaranteeDeposit = mAccountToken.getAccount().getGuaranteeDeposit();
//                    if (!TextUtils.isEmpty(guaranteeDeposit)) {
//                        double v2 = Double.parseDouble(deposit);
//                        if (v1 > v2) {
//                            LogUtil.e(TAG, "mAccountToken.getAccount().getGuaranteeDeposit()=" +
//                                    mAccountToken.getAccount().getGuaranteeDeposit());
//                            Intent rechargeIntent = new Intent(this, PayRefundActivity.class);
//                            rechargeIntent.putExtras(getAuthAccountBundle());
//                            startActivity(rechargeIntent);
//                            return;
//                        }
//                    }
//                }
//                if (Double.parseDouble(mAccountToken.getAccount().getBalance()) <= 0) {
//                    Intent rechargeIntent = new Intent(this, RechargeActivity.class);
//                    rechargeIntent.putExtras(getAuthAccountBundle());
//                    startActivity(rechargeIntent);
//                    return;
//                }
//                if (currentState == UNDER_BICYCLING) {
//                    ToastUtil.showUIThread(this, getResources().getString(R.string.tip_150));
//                    return;
//                }
                //检测是否打开蓝牙
                startActivityForResult(new Intent(MapActivity.this, CaptureActivity.class),
                        REQUEST_SCAN_RESULT);

                break;
            case R.id.locate:
//                unlockByBlueTooth();
                //重新定位
                Log.d(TAG, "重新定位");
                moveToMyLoc();
                break;
            case R.id.iv_icon:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.iv_search:
                Intent search = new Intent(this, SearchActivity.class);
                search.putExtra("myaddress", myAddress);
                search.putExtras(getAuthBundle());
                startActivityForResult(search, REQUEST_SEARCH_KEYWORD);
                break;

            case R.id.user_wallet:
                Intent wallet = new Intent(this, WalletActivity.class);
                wallet.putExtras(getAuthAccountBundle());
                startActivity(wallet);
                break;
            case R.id.user_discount:
                Intent discount = new Intent(this, MyDiscountActivity.class);
                discount.putExtras(getAuthBundle());
                startActivity(discount);
                break;
            case R.id.user_track:
                Intent routes = new Intent(this, RouteActivity.class);
                routes.putExtras(getAuthBundle());
                startActivity(routes);
                break;
            case R.id.user_msg:
                startActivity(new Intent(this, MyMsgActivity.class));
                break;
            case R.id.user_friends:
                Intent friends = new Intent(this, ShareActivity.class);
                friends.putExtras(getAuthBundle());
                startActivity(friends);
                break;
            case R.id.user_guide:
                startActivity(new Intent(this, UserGuideActivity.class));
                break;
            case R.id.about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.user_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
//            case R.id.username:
//                //如果登录过期就重新登录
//                if (!AccountUtil.isLogin(this)) {
//                    login();
//                }
//                break;
            case R.id.ll_identify:
                startActivity(new Intent(this, IdentityActivity.class));
                break;
            case R.id.user_icon:
                Intent userInfoIntent = new Intent(this, UserInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AUTH_NATIVE_TOKEN, mAuthNativeToken);
                bundle.putSerializable(ACCOUNT_TOKEN, mAccountToken);
                bundle.putSerializable(USER_ICON_BEAN, mOssParams);
                userInfoIntent.putExtras(bundle);
                startActivity(userInfoIntent);
                break;
            case R.id.ll_creditPoints:
                Intent creditPoints = new Intent(this, CreditPointsActivity.class);
                creditPoints.putExtras(getAuthAccountBundle());
                startActivity(creditPoints);
                break;
        }
    }

    /**
     * 获取带有AuthToken的bundle
     */
    private Bundle getAuthBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MapActivity.AUTH_NATIVE_TOKEN, mAuthNativeToken);
        return bundle;
    }

    /**
     * 获取带有AuthToken,AccountToken的bundle
     */
    private Bundle getAuthAccountBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MapActivity.AUTH_NATIVE_TOKEN, mAuthNativeToken);
        bundle.putSerializable(ACCOUNT_TOKEN, mAccountToken);
        return bundle;
    }

    private void login() {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        registerIntent.putExtra(RegisterActivity.WHERE_FROM, RegisterActivity
                .MAP_START);
        startActivityForResult(registerIntent, REQUEST_LOGIN);
    }


    /**
     * 设置dialog样式
     */
    private void showServiceDialog() {
        final Dialog dialog = new Dialog(this);
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!AccountUtil.isLogin(MapActivity.this)) {
                    login();
                    return;
                }

                AMapLocation lastKnownLocation = mLocationClient.getLastKnownLocation();
                switch (v.getId()) {
                    case R.id.cannot_unlock:
                        Intent aunlock = new Intent(MapActivity.this, AUnlockActivity.class);
                        aunlock.putExtras(getAuthAccountBundle());
                        aunlock.putExtra("lat", String.valueOf(lastKnownLocation.getLatitude()));
                        aunlock.putExtra("lng", String.valueOf(lastKnownLocation.getLongitude()));
                        startActivity(aunlock);
                        break;
                    case R.id.report_illegal_stop:
                        Intent iLeagle = new Intent(MapActivity.this, ILeagleActivity.class);
                        iLeagle.putExtras(getAuthAccountBundle());
                        iLeagle.putExtra("lat", lastKnownLocation.getLatitude());
                        iLeagle.putExtra("lng", lastKnownLocation.getLongitude());
                        startActivity(iLeagle);
                        break;
                    case R.id.recharge:
                        startActivity(new Intent(MapActivity.this, RechargeActivity.class));
                        break;
                    case R.id.trouble_report:
                        Intent bikeBroken = new Intent(MapActivity.this, BikeBrokenActivity.class);
                        bikeBroken.putExtras(getAuthAccountBundle());
                        bikeBroken.putExtra("lat", lastKnownLocation.getLatitude());
                        bikeBroken.putExtra("lng", lastKnownLocation.getLongitude());
                        startActivity(bikeBroken);
                        break;
                }
                dialog.dismiss();
            }
        };

        //设置无标题栏
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_service);
        dialog.findViewById(R.id.cannot_unlock).setOnClickListener(listener);
        dialog.findViewById(R.id.report_illegal_stop).setOnClickListener(listener);
        dialog.findViewById(R.id.recharge).setOnClickListener(listener);
        dialog.findViewById(R.id.trouble_report).setOnClickListener(listener);
        //设置参数，宽度充满屏幕
        Window window = dialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;

        //设置从底部弹出和动画
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_service_style);
        window.setAttributes(attributes);

        dialog.show();

    }

    /**
     * 移动我我的位置完成回调
     */
    AMap.CancelableCallback callback = new AMap.CancelableCallback() {
        @Override
        public void onFinish() {
            startJumpAnimation();
        }

        @Override
        public void onCancel() {

        }
    };

    /**
     * 移动到我的位置
     */
    private void moveToMyLoc() {
        if (mLocationClient != null) {
            AMapLocation lastKnownLocation = mLocationClient.getLastKnownLocation();
            if (lastKnownLocation == null) return;
            LatLng point = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation
                    .getLongitude());
            //位置，缩放级别，倾斜角度，可视区域指向的方向
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition
                    (point, 17, 0, 0));

            aMap.animateCamera(cameraUpdate, RETURN_MYLOC_TIME, callback);
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(point));
        } else {
            LogUtil.e(TAG, "mLocationClient==null");
        }
    }


    /**
     * 使用蓝牙开锁的进度框
     */
    private void showBTUnlockDialog() {
        if (btProgress != null && btProgress.isShowing()) {
            return;
        }
        btProgress = new Dialog(this);
        btProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        btProgress.setContentView(R.layout.dialog_bluetooth_unlock);
        TextView tvBikeId = (TextView) btProgress.findViewById(R.id.tv_bikeID);
        tvBikeId.setText(scanResultToken.bikeno);
        pbUnlock = (ProgressBar) btProgress.findViewById(R.id.pb_bluetooth_unlock);
        pbUnlock.setMax(MAX_BLUETOOTH_PB);
        pbUnlock.setProgress(0);
        btProgress.setCancelable(false);
        //设置参数，宽度充满屏幕
        Window window = btProgress.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(attributes);
        btProgress.show();
        handler.sendEmptyMessageDelayed(MSG_UNLOCK_BLUETOOTH, DELAY_BT_PB);
    }

    /**
     * 取消蓝牙开锁进度框
     */
    private void dismissBTUnlockDialog() {
        if (btProgress == null) return;
        btProgress.dismiss();
        handler.removeMessages(MSG_UNLOCK_BLUETOOTH);
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mOnLocationChangedListener = onLocationChangedListener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(mAMapLocationListener);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode
                    .Hight_Accuracy);
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否强制刷新WIFI，默认为true，强制刷新。
            mLocationOption.setWifiActiveScan(false);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(20000);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();

        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mAMapLocationListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    private String permissionInfo;

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                    .PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                    .PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager
                    .PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }

//            ActivityCompat.requestPermissions(this, getPermissions(permissions), 2);
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]),
                        SDK_PERMISSION_REQUEST);
            }
        }
    }

    int SDK_PERMISSION_REQUEST = 127;

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { //
            // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }
        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    public String[] getPermissions(ArrayList<String> permissions) {
        if (permissions == null) return null;
        String[] strings = new String[permissions.size()];
        for (int i = 0; i < permissions.size(); i++) {
            strings[i] = permissions.get(i);
        }
        return strings;
    }

    @Override
    public void onBackPressed() {
        if (start == 0) {
            start = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_151),
                    Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(TWICE_BACK_QUIT, 2000);
            return;
        } else {
            end = System.currentTimeMillis();
        }
        if (end - start <= 2000) {
            super.onBackPressed();
            start = 0;
            end = 0;
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.map);
    }


    /**
     * 请求预定车辆
     */
    private BikeReserveToken mBikeReserveToken;
    private static final int RESERVER_REMAIN_TIME = 15 * 60;

    public void reserveBike() {
        String tid = currentMarker.getTitle();
        String plateno = "";
        String userlng = "";
        String userlat = "";
        String bikelng = "";
        String bikelat = "";
        BikeBean bikeBean = new BikeBean(tid, plateno, userlng, userlat, bikelng, bikelat);
        LogUtil.e(TAG, "将要请求预定的车辆信息:" + bikeBean);
        httpBikeBeanUtil.reserveBike(mAuthNativeToken.getAuthToken().getAccess_token(), bikeBean,
                new HttpCallbackHandler<BikeReserveToken>() {

                    @Override
                    public void onSuccess(BikeReserveToken bikeReserveToken) {
                        if (TextUtils.equals(BikeReserveToken.STATE_AVAILABLE + "", bikeReserveToken
                                .getErrcode())) {
                            LogUtil.e(TAG, "预定车辆成功,将保留15分钟,tid=" + bikeReserveToken.getTid());
                            //显示预约中
                            hideAllWindows();
                            showInOrderWindow(bikeReserveToken.getTid(), RESERVER_REMAIN_TIME);
                            mBikeReserveToken = bikeReserveToken;
                            //缓存预定信息
                            CacheUtil.cacheSerialToken(getApplicationContext(), Constants
                                    .CURRENT_RESERVATION_TOKEN_FILE, bikeReserveToken);
                            ToastUtil.showUIThread(MapActivity.this, getResources().getString(R
                                    .string.tip_152));
                            setCurrentState(UNDER_RESERVE);
                            return;
                        } else if (TextUtils.equals(BikeReserveToken.STATE_BALANCE_EAGER + "",
                                bikeReserveToken.getErrcode())) {
                            ToastUtil.showUIThread(MapActivity.this, getResources().getString(R
                                    .string.tip_153));
                            //进入充值界面
                            Intent recharge = new Intent(MapActivity.this, RechargeActivity.class);
                            recharge.putExtras(getAuthBundle());
                            startActivity(recharge);
                        } else {
                            ToastUtil.showUIThread(MapActivity.this, bikeReserveToken.getErrmsg());
                        }
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        ToastUtil.showUIThread(MapActivity.this, getResources().getString(R
                                .string.tip_154));
                    }
                });
    }

    /**
     * 取消预约车辆
     */
    public void cancelReservation() {
        if (mBikeReserveToken == null) {
            mBikeReserveToken = CacheUtil.getSerialToken(this, Constants
                    .CURRENT_RESERVATION_TOKEN_FILE);
            if (mBikeReserveToken == null) return;
        }
        String reservationId = mBikeReserveToken.getReservationId();
        httpBikeBeanUtil.cancelReservationBike(mAuthNativeToken.getAuthToken().getAccess_token(),
                reservationId, new HttpCallbackHandler<CancelReserveToken>() {
                    @Override
                    public void onSuccess(CancelReserveToken cancelReserveToken) {
                        if (cancelReserveToken.getErrcode() == 0) {
                            hideAllWindows();
                            remainReservetime = 0;
                            LogUtil.e(TAG, "取消预约成功");
                            ToastUtil.showUIThread(MapActivity.this, getResources().getString(R
                                    .string.tip_155));
                            setCurrentState(UNDER_FREE);
                        }
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        ToastUtil.showUIThread(MapActivity.this, getResources().getString(R
                                .string.tip_156));
                    }
                });
    }

    /**
     * 显示预约窗口
     */
    public void showOrderWindow(Marker currentMarker) {
        hideAllWindows();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                orderWindow.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 刷新预约界面显示
     */
    private void refreshOrderWindow() {
        tvEndPoint1.setText(tvEndPoint2.getText());
    }


    /**
     * 显示正在预约窗口
     */
    private int remainReservetime = 15 * 60;//预约保留时间
    private static final int COUNTING_RESERVE_LEFT_TIME = 51;

    public void showInOrderWindow(final String tid, final int remainSeconds) {
        hideAllWindows();
        setCurrentState(UNDER_RESERVE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvOrderedNum.setText(tid);
                tvEndPoint1.setText(tvEndPoint2.getText());
                inOrderWindow.setVisibility(View.VISIBLE);
                tvRemainTime.setText(TimeUtil.getMiniteSecondsStr(remainSeconds));
                handler.sendEmptyMessageDelayed(COUNTING_RESERVE_LEFT_TIME,
                        COUNT_BIKING_TIME_DELAYED);
            }
        });
    }


    /**
     * 显示正在骑行窗口
     */
    private static final int COUNT_BIKING_TIME = 21;
    private static final int COUNT_BIKING_TIME_DELAYED = 1000;

    public void showInUserWindow() {
        hideAllWindows();
        setCurrentState(UNDER_BICYCLING);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                inUseWindow.setVisibility(View.VISIBLE);
            }
        });
        startCountingTime();
        startCountingRideDistance();
    }

    /**
     * 刷新显示
     */
    private void refreshInUseWindow() {
        usingTimeSeconds++;
        LogUtil.e(TAG, "刷新时间 ：" + usingTimeSeconds);
        tvUseTime.setText(TimeUtil.getMiniteSecondsStr(usingTimeSeconds));
        tvDistance.setText(rideDistance + "");
        tvCalory.setText(rideDistance / 20 + "");
        LogUtil.d(TAG, "骑行距离" + rideDistance);
    }

    private int usingTimeSeconds = 0;

    private void startCountingTime() {
        handler.sendEmptyMessageDelayed(COUNT_BIKING_TIME, COUNT_BIKING_TIME_DELAYED);
        usingTimeSeconds = 0;
    }

    private long rideDistance = 0;
    private static final int COUNT_BIKING_DISTANCE = 22;
    private static final int COUNT_BIKING_DISTANCE_DELAYED = 10 * 1000;
    private LatLng tripStartPoint = null;
    private LatLng realTimeLocation = null;

    public void startCountingRideDistance() {
        handler.sendEmptyMessageDelayed(COUNT_BIKING_DISTANCE, COUNT_BIKING_DISTANCE_DELAYED);
        tripStartPoint = realTimeLocation;
    }

    public void countRideDistance() {
        rideDistance += AMapUtils.calculateLineDistance(tripStartPoint, realTimeLocation);
        tripStartPoint = realTimeLocation;
    }

    public void hideAllWindows() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                orderWindow.setVisibility(View.GONE);
                tvEndPoint2.setText("未知位置");
                emulateDistance.setText("0");
                emulateTime.setText("0");
                inOrderWindow.setVisibility(View.GONE);
                tvEndPoint1.setText("未知位置");
                tvOrderedNum.setText("");
                tvRemainTime.setText("15:00");
                inUseWindow.setVisibility(View.GONE);
                tvUseTime.setText("00:00");
                tvDistance.setText("0");
                tvCalory.setText("0");
            }
        });
    }

    public void setCurrentState(int state) {
        this.currentState = state;
    }

    private Dialog hintDialog;

    public void showManualUnlockHintDialog(String unlockPsd) {
        if (unlockSuccess) {
            return;
        }
        if (hintDialog == null) {
            hintDialog = new Dialog(this, R.style.CustomDialogStyle);
            hintDialog.setContentView(R.layout.dialog_manual_unlock_hint);
            hintDialog.setCanceledOnTouchOutside(false);
            hintDialog.setCancelable(false);
//           Window  dialogWindow = hintDialog.getWindow();
//           WindowManager.LayoutParams p =  dialogWindow.getAttributes();
//           p.width = 600;
//           p.height = 700;

        }
        TextView unlockPsd_tv = (TextView) hintDialog.findViewById(R.id.tv_unlockPsd);
        TextView orderActiveHint_tv = (TextView) hintDialog.findViewById(R.id
                .tv_orderActiveHint);
        Button cancelOrder_btn = (Button) hintDialog.findViewById(R.id.btn_cancel_order);
        Button continueUse_btn = (Button) hintDialog.findViewById(R.id.btn_continueUse);
        unlockPsd_tv.setText(StringUtil.getRiceText(this, getString(R.string.unlock_psd,
                unlockPsd), 5, 9, R.color.green_7b, DpPxUtil.sp2px(this, 20)));
        cancelOrder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hintDialog.isShowing()) {
                    hintDialog.dismiss();
                }
                stopCountDown();
            }
        });
        continueUse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hintDialog.isShowing()) {
                    hintDialog.dismiss();
                }
                stopCountDown();
            }
        });
        if (!hintDialog.isShowing())

        {
            startCountDown(orderActiveHint_tv);
            hintDialog.show();
        }

    }

    private CountDownTimer countDownTimer;

    public void startCountDown(final TextView tv) {
        countDownTimer = new CountDownTimer(120 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String hint = MapActivity.this.getString(R.string.order_hint, millisUntilFinished
                        / 1000 + "");
                SpannableString hintInfo = StringUtil.getRiceText(MapActivity.this, hint, 4, hint
                        .length() - 5, R.color.red_24, DpPxUtil.sp2px(MapActivity.this, 18));
                tv.setText(hintInfo);
            }

            @Override
            public void onFinish() {
                stopCountDown();
                if (hintDialog.isShowing()) {
                    hintDialog.dismiss();
                }
            }
        };
        countDownTimer.start();
    }

    public void stopCountDown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }


}
