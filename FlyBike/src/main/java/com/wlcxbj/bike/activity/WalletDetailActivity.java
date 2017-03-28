package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.history.TransactionBean;
import com.wlcxbj.bike.bean.history.TransactionListBean;
import com.wlcxbj.bike.bean.history.TransactionListToken;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.net.beanutil.HttpHistoryBeanUtil;
import com.wlcxbj.bike.util.TimeUtil;


/**
 * Created by bain on 16-11-28.
 */
public class WalletDetailActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<TransactionBean> transactions;
    private TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getRechargeHistory();
        ListView lvTrans = (ListView) findViewById(R.id.lv_transaction);
        findViewById(R.id.tv_right).setOnClickListener(this);
        findViewById(R.id.ib_back).setOnClickListener(this);
        transactions = new ArrayList<>();
        transactionAdapter = new TransactionAdapter();
        lvTrans.setAdapter(transactionAdapter);
    }

    //获取充值记录
    private void getRechargeHistory() {
//        initToken();
        HttpHistoryBeanUtil httpHistoryBeanUtil = new HttpHistoryBeanUtil(this);
        final TransactionListBean transactionListBean = new TransactionListBean();
        transactionListBean.setOrderTypeSpids("1,2,3");
        httpHistoryBeanUtil.getTransactionList(mAuthNativeToken.getAuthToken()
                .getAccess_token(), transactionListBean, new
                HttpCallbackHandler<TransactionListToken>() {

                    @Override
                    public void onSuccess(TransactionListToken transactionListToken) {
                        transactions = transactionListToken.getTransactions();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                transactionAdapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Exception error, String msg) {

                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_right:
                startActivity(new Intent(WalletDetailActivity.this, RefundDesActivity.class));
                break;
        }
    }

    class TransactionAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return transactions.size();
        }

        @Override
        public Object getItem(int position) {
            return transactions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TransViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_tran_list, null);
                holder = new TransViewHolder();
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tvState = (TextView) convertView.findViewById(R.id.tv_state);
                holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
                holder.tvMethod = (TextView) convertView.findViewById(R.id.tv_method);
                convertView.setTag(holder);
            } else {
                holder = (TransViewHolder) convertView.getTag();
            }
            TransactionBean bean = (TransactionBean) getItem(position);
            holder.tvTime.setText(TimeUtil.getTimeStr(bean.getIntoAccountTime()));
            holder.tvState.setText(bean.getOrderTypeStr(bean.getOrderTypeSpid()));
            holder.tvNumber.setText(getResources().getString(R.string.currency_symble) + new
                    DecimalFormat("######0.00").format(bean.getAmount()));
            holder.tvMethod.setText(bean.getPayTypeSpid() == 1 ?
                    getResources().getString(R.string.tip_12) :
                    getResources().getString(R.string.tip_11));
            return convertView;
        }


    }

    private static class TransViewHolder {
        TextView tvTime;
        TextView tvState;
        TextView tvNumber;
        TextView tvMethod;
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_wallet_detail);
    }
}
