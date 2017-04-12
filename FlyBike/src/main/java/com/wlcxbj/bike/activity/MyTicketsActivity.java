package com.wlcxbj.bike.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.adapter.TicketsAdapter;
import com.wlcxbj.bike.bean.TicketsDataEntity;
import com.wlcxbj.bike.bean.bike.TiketsExchangeToken;
import com.wlcxbj.bike.bean.other.CouponBean;
import com.wlcxbj.bike.bean.other.CouponsToken;
import com.wlcxbj.bike.global.Error;
import com.wlcxbj.bike.net.beanutil.HttpAccountOtherBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.view.StateView;

/**
 * Created by bain on 17-1-18.
 */
public class MyTicketsActivity extends BaseActivity implements View.OnClickListener {
    private static final int MSG_CANCEL_REFRESH = 1000;
    @Bind(R.id.ib_back)
    FrameLayout titleBack;
    @Bind(R.id.stateView)
    StateView stateView;

    private static final int TOTAL_COUNTER = 18;
    private static final int PAGE_SIZE = 6;
    private int mCurrentCounter = 0;
    private static final String TAG = "MyMsgActivity";
    private View successView;
    private RecyclerView rvMsg;
    private TicketsAdapter msgAdapter;
    private ArrayList<CouponBean> ticketDataEntities;
    private SwipeRefreshLayout srl;
    private HttpAccountOtherBeanUtil httpAccountOtherBeanUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        initData();
    }


    private void initData() {
        httpAccountOtherBeanUtil = new HttpAccountOtherBeanUtil(this);
        if (mAuthNativeToken != null) {
            if (mAuthNativeToken.getAuthToken() != null) {
                httpAccountOtherBeanUtil.getCouponsList(mAuthNativeToken.getAuthToken()
                        .getAccess_token(), -1, -1, new HttpCallbackHandler<CouponsToken>() {
                    @Override
                    public void onSuccess(final CouponsToken couponsToken) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ticketDataEntities = couponsToken.getCoupons();
                                msgAdapter = new TicketsAdapter(R.layout.item_tickets,
                                        ticketDataEntities);
                                msgAdapter.addFooterView(getFooterView());
                                msgAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter
                                        .OnRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        exchangeTickets(position);
                                    }
                                });
                                rvMsg.setAdapter(msgAdapter);
                                LogUtil.e(TAG, "刷新数据");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {

                    }
                });
            }
        }
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_tickets);
        findViewById(R.id.ib_back).setOnClickListener(this);
    }


    private void initView() {
        stateView.setEmpytView(View.inflate(this, R.layout.mymsg_layout_empy, null));
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
    }

    /**
     * 设置加载成功数据展示
     */
    private static class MyHandler extends Handler {
        private MyTicketsActivity thisActivity;

        public MyHandler(MyTicketsActivity thisActivity) {
            this.thisActivity = thisActivity;
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

    private Handler mHandler = new MyHandler(this);

    private void initSuccessView() {
        if (successView == null) return;
        rvMsg = (RecyclerView) successView.findViewById(R.id.rv_msg);
        srl = (SwipeRefreshLayout) successView.findViewById(R.id.srl);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMsg.setLayoutManager(linearLayoutManager);

        ticketDataEntities = new ArrayList<>();
//        getMoreData();
        msgAdapter = new TicketsAdapter(R.layout.item_tickets, ticketDataEntities);
        msgAdapter.addFooterView(getFooterView());
        rvMsg.setAdapter(msgAdapter);
//        setLoadMore();
        setRefresh();
//        msgAdapter.openLoadMore(PAGE_SIZE, true);
    }

    /**
     * 兑换优惠券
     */
    private void exchangeTickets(final int position) {
        int couponId = ticketDataEntities.get(position).getId();
        httpAccountOtherBeanUtil.exchangeCoupon(mAuthNativeToken.getAuthToken().getAccess_token()
                , couponId, new HttpCallbackHandler<TiketsExchangeToken>() {

                    @Override
                    public void onSuccess(TiketsExchangeToken tiketsExchangeToken) {
                        if (tiketsExchangeToken != null && tiketsExchangeToken.getErrcode() ==
                                Error.OK) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ticketDataEntities.remove(position);
                                    msgAdapter.notifyDataSetChanged();
                                    showExhangeTickketDialog(true);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showExhangeTickketDialog(false);
                            }
                        });
                    }
                });
    }

    private void showExhangeTickketDialog(boolean b) {

        new AlertDialog.Builder(this)
                .setTitle(b ? getResources().getString(R.string.tip_190) : getResources()
                        .getString(R.string.tip_189))
                .setPositiveButton(getResources().getString(R.string.confirm), null)
                .create()
                .show();

    }

    /**
     * 设置下拉刷新
     */
    private void setRefresh() {
        if (srl == null) return;
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string
                        .tip_161), Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessageDelayed(MSG_CANCEL_REFRESH, 1500);
                    }
                }).start();
            }
        });
    }

    /**
     * 设置加载更多
     */
    private void setLoadMore() {

        msgAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                rvMsg.post(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().postDelayed(//停止刷新
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mCurrentCounter >= TOTAL_COUNTER) {
                                            msgAdapter.notifyDataChangedAfterLoadMore(false);

                                        } else {
                                            mCurrentCounter = msgAdapter.getItemCount();
                                            msgAdapter.notifyDataChangedAfterLoadMore
                                                    (ticketDataEntities, true);

                                        }
                                    }
                                }, 2000);

                        Log.e("MyMsgActivity", "加载更多,mCurrentCounter" + mCurrentCounter);
                    }
                });
            }
        });
    }


    @OnClick({R.id.ib_back, R.id.tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_right:
                startActivity(new Intent(this, TicketsGuideActivity.class));
                break;
        }
    }

    public View getFooterView() {
        View footerView = View.inflate(getApplicationContext(), R.layout.msg_footer, null);
        return footerView;
    }

    /**
     * 加载更多
     */
    public void getMoreData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
