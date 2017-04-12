package com.wlcxbj.bike.activity;

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
import com.wlcxbj.bike.adapter.MsgAdapter;
import com.wlcxbj.bike.bean.MsgDataEntity;
import com.wlcxbj.bike.net.beanutil.HttpAccountOtherBeanUtil;
import com.wlcxbj.bike.view.StateView;

/**
 * Created by bain on 16-11-28.
 */
public class MyMsgActivity extends BaseActivity {
    private static final int MSG_CANCEL_REFRESH = 1009;
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
    private MsgAdapter msgAdapter;
    private ArrayList<MsgDataEntity> msgDataEntities;
    private SwipeRefreshLayout srl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();

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
//        stateView.showEmpytView();
    }

    /**
     * 设置加载成功数据展示
     */
    private void initSuccessView() {
        if (successView == null) return;
        rvMsg = (RecyclerView) successView.findViewById(R.id.rv_msg);
        srl = (SwipeRefreshLayout) successView.findViewById(R.id.srl);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMsg.setLayoutManager(linearLayoutManager);

        msgDataEntities = new ArrayList<>();
        getMoreData();
        msgAdapter = new MsgAdapter(R.layout.item_msg, msgDataEntities);
        msgAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter
                .OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, int i) {
                startActivity(new Intent(getApplicationContext(), MsgDetailactivity.class));
            }
        });
        msgAdapter.addFooterView(getFooterView());
        rvMsg.setAdapter(msgAdapter);
        setLoadMore();
        setRefresh();
        msgAdapter.openLoadMore(PAGE_SIZE, true);
    }
    private static class MyHandler extends Handler {
        private MyMsgActivity thisActivity;
        public MyHandler(MyMsgActivity activity) {
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
    private Handler mHandler = new MyHandler(this);
    /**
     * 设置下拉刷新
     */
    private void setRefresh() {
        if (srl == null) return;
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.tip_161), Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        Looper.prepare();
//                        new Handler().postDelayed(//停止刷新
//                                new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        srl.setRefreshing(false);
//                                    }
//                                }, 2000);
                        mHandler.sendEmptyMessageDelayed(MSG_CANCEL_REFRESH, 2000);
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
                                                    (msgDataEntities, true);

                                        }
                                    }
                                }, 2000);

                        Log.e("MyMsgActivity", "加载更多,mCurrentCounter" + mCurrentCounter);
                    }
                });
            }
        });
    }


    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_my_msg);
    }

    @OnClick({R.id.ib_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
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
                int index = msgDataEntities.size();
                String content = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "ssssssssssssssssssssssssssssssssssssssssss" +
                        "ddddddddddddddddddddddddddddddd" +
                        "ffffffffffffffffffffffxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
                for (int i = 0; i < 6; i++) {
                    msgDataEntities.add(new MsgDataEntity(i % 2 == 0 ? true : false, "title" + i,
                            content));
                    Log.e("MyMsgActivity", "添加数据--" + i);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
