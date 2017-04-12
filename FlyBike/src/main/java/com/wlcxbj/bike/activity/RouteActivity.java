package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.adapter.MsgAdapter;
import com.wlcxbj.bike.adapter.MyRoutesAdapter;
import com.wlcxbj.bike.bean.MsgDataEntity;
import com.wlcxbj.bike.bean.history.RentBikeBean;
import com.wlcxbj.bike.bean.history.RequestRentHistoryListBean;
import com.wlcxbj.bike.bean.history.RentHistoryListToken;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.net.beanutil.HttpHistoryBeanUtil;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.TimeUtil;
import com.wlcxbj.bike.util.ToastUtil;
import com.wlcxbj.bike.view.StateView;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;
import static com.wlcxbj.bike.R.id.stateView;

/**
 * Created by bain on 16-11-28.
 */
public class RouteActivity extends BaseActivity {
    @Bind(R.id.ib_back)
    FrameLayout titleBack;
    @Bind(R.id.tv_right)
    TextView tvRight;
    RecyclerView rvRoutes;
    private ArrayList<RentBikeBean> routeBeans;
    private static final int PAGESIZE = 10;
    private static final String TAG = "RouteActivity";
    private MyRoutesAdapter routeAdapter;
    private View successView;
    private StateView stateView;
    private SwipeRefreshLayout srl;
    private int currentPageNo = 0;
    private static final int TYPE_INIT = 1;
    private static final int TYPE_REFRESH = 2;
    private static final int TYPE_LOAD_MORE = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        getRentHistory(currentPageNo, PAGESIZE, TYPE_INIT);
    }

    private void initView() {
        stateView = (StateView) findViewById(R.id.stateView);
        stateView.setEmpytView(View.inflate(this, R.layout.myroutes_layout_empy, null));
        successView = LayoutInflater.from(this).inflate(R.layout.mymsg_layout_success, null);
        initSuccessView();
        stateView.setSuccessView(successView);
        stateView.setOnReloadListener(new StateView.OnReloadListener() {
            @Override
            public void onReload() {
                Log.e(TAG, "重新加载布局");
            }
        });
        stateView.showSuccessView();
//        stateView.showEmpytView();
    }

    /**
     * 设置加载成功数据展示
     */
    private void initSuccessView() {
        if (successView == null) return;
        rvRoutes = (RecyclerView) successView.findViewById(R.id.rv_msg);
        srl = (SwipeRefreshLayout) successView.findViewById(R.id.srl);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvRoutes.setLayoutManager(linearLayoutManager);
        routeBeans = new ArrayList<>();
        routeAdapter = new MyRoutesAdapter(R.layout.item_list_route, routeBeans, this);
        routeAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter
                .OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Intent rentDetail = new Intent(RouteActivity.this, RouteDetailActivity.class);
                Bundle authBundle = getAuthBundle();
                authBundle.putSerializable("routebean", routeBeans.get(position));
                rentDetail.putExtras(authBundle);
                startActivity(rentDetail);
            }
        });
        routeAdapter.addFooterView(getFooterView());
        rvRoutes.setAdapter(routeAdapter);
        setLoadMore();
        setRefresh();
        routeAdapter.openLoadMore(PAGESIZE, true);
    }

    /**
     * 获取租车历史记录
     *
     * @param pageNo   页码
     * @param pageSize 页大小
     * @param type     true下拉刷新 false上拉加载
     */
    private void getRentHistory(int pageNo, int pageSize, final int type) {
        HttpHistoryBeanUtil httpHistoryBeanUtil = new HttpHistoryBeanUtil(this);
        RequestRentHistoryListBean requestRentHistoryListBean = new RequestRentHistoryListBean();
        requestRentHistoryListBean.setPageNo(pageNo);
        requestRentHistoryListBean.setPageSize(pageSize);
        httpHistoryBeanUtil.getRentHistory(mAuthNativeToken.getAuthToken().getAccess_token(),
                requestRentHistoryListBean, new HttpCallbackHandler<RentHistoryListToken>() {

                    @Override
                    public void onSuccess(RentHistoryListToken rentHistoryListToken) {
                        if (rentHistoryListToken != null && rentHistoryListToken.getRents() !=
                                null) {
                            if (type == TYPE_REFRESH) {
                                routeBeans.clear();
                            }
                            final ArrayList<RentBikeBean> rents = rentHistoryListToken.getRents();
                            for (int i = 0; i < rents.size(); i++) {
                                routeBeans.add(rents.get(i));
                            }
                            LogUtil.e(TAG, "获取租车记录条数：" + rents.size());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (type == TYPE_INIT) {
                                        routeAdapter.notifyDataSetChanged();
//                                        routeAdapter.notifyDataChangedAfterLoadMore(false);
                                    } else if (type == TYPE_REFRESH) {
//                                        rvRoutes.setAdapter(routeAdapter);
                                        routeAdapter.notifyDataSetChanged();
                                    } else if (type == TYPE_LOAD_MORE) {
                                        //设置下拉刷新是否可用，并且刷新数据
                                        routeAdapter.notifyDataChangedAfterLoadMore(false);
                                    }
                                    mHandler.sendEmptyMessage(MSG_CANCEL_REFRESH);
                                    currentPageNo++;
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        ToastUtil.showUIThread(RouteActivity.this, getResources().getString(R.string
                                .tip_191));
                        mHandler.sendEmptyMessage(MSG_CANCEL_REFRESH);
                    }
                });
    }

    /**
     * 设置下拉刷新
     */
    private void setRefresh() {
        if (srl == null) return;
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getRentHistory(0, PAGESIZE, TYPE_REFRESH);
                    }
                });
            }
        });
    }

    /**
     * 设置加载更多
     */
    private void setLoadMore() {
        routeAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getRentHistory(currentPageNo, PAGESIZE, TYPE_LOAD_MORE);
                    }
                });
            }
        });
    }


    /**
     * 获取带有AuthToken的bundle
     */
    private Bundle getAuthBundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MapActivity.AUTH_NATIVE_TOKEN, mAuthNativeToken);
        return bundle;
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_routes);
    }

    @OnClick({R.id.ib_back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_right:
                startActivity(new Intent(this, ChargeRuleActivity.class));
                break;
        }
    }

    private static final int MSG_CANCEL_REFRESH = 1009;
    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private RouteActivity thisActivity;

        public MyHandler(RouteActivity activity) {
            thisActivity = activity;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CANCEL_REFRESH:
                    thisActivity.srl.setRefreshing(false);
                    break;
            }
        }
    }

    public View getFooterView() {
        View footerView = View.inflate(getApplicationContext(), R.layout.msg_footer, null);
        return footerView;
    }

    public void getMoreData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //从后台获取更多数据
                getRentHistory(currentPageNo, PAGESIZE, TYPE_LOAD_MORE);
            }
        }).start();
    }

}
