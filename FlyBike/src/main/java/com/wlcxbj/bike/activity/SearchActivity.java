package com.wlcxbj.bike.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;

import java.util.ArrayList;
import java.util.List;

import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.commonaddress.CommonAddressBean;
import com.wlcxbj.bike.bean.commonaddress.CommonAddressListToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.net.beanutil.HttpAccountOtherBeanUtil;
import com.wlcxbj.bike.net.beanutil.HttpCallbackHandler;
import com.wlcxbj.bike.util.DpPxUtil;
import com.wlcxbj.bike.adapter.HeaderAndFootWrapper;
import com.wlcxbj.bike.adapter.ViewHolder;
import com.wlcxbj.bike.bean.SearchBean;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.ToastUtil;
import com.wlcxbj.bike.util.account.AccountUtil;
import com.wlcxbj.bike.util.cache.CacheUtil;

/**
 * Created by Administrator on 2016/11/15.
 */
public class SearchActivity extends BaseActivity implements View.OnTouchListener, View
        .OnClickListener, TextWatcher, Inputtips.InputtipsListener {
    private static final String TAG = "SearchActivity";
    public static final int REQUEST_EDIT_ADDRESS = 1000;
    public static final int REQUEST_ADD_ADDRESS = 1001;
    private static final int TYPE_HISTORY = 1;
    private static final int TYPE_USUAL_ADDRESS = 2;
    private static final int TYPE_SEARCH_ADDRESS = 3;
    private EditText etSearch;
    private Drawable drawableRight;
    private RecyclerView rvHistory;
    private Drawable drawableLeft;
    private ArrayList<SearchBean> searchData;
    private ArrayList<SearchBean> historyData;
    private ArrayList<SearchBean> showData;
    private HeaderAndFootWrapper headerAndFootWrapper;
    private TextView footer;
    private String myaddress;
    private ArrayList<CommonAddressBean> addresses;
    private ArrayList<CommonAddressBean> emptyAddress;
    private ArrayList<CommonAddressBean> showAddress;
    private ArrayList<CommonAddressBean> commonAddressList;
    private ArrayList<String> localHistory;
    private HttpAccountOtherBeanUtil httpAccountOtherBeanUtil;
    private String mycity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        initData();
        initView();
    }

    private boolean hasCommonAddress = false;
    private ArrayList<CommonAddressBean> commonAddressBeanArrayList;

    private void initData() {
        httpAccountOtherBeanUtil = new HttpAccountOtherBeanUtil(this);
        if (mAuthNativeToken != null && mAuthNativeToken.getAuthToken() != null)
            httpAccountOtherBeanUtil.getCommonAddressList(mAuthNativeToken.getAuthToken()
                    .getAccess_token(), new HttpCallbackHandler<CommonAddressListToken>() {
                @Override
                public void onSuccess(CommonAddressListToken commonAddressListToken) {
                    commonAddressBeanArrayList = commonAddressListToken.getData();
                    hasCommonAddress = true;
                    if (commonAddressBeanArrayList == null || commonAddressBeanArrayList.size()
                            == 0) {
                        hasCommonAddress = false;
                        for (int i = 0; i < 2; i++) {
                            commonAddressBeanArrayList.add(new CommonAddressBean(-1, "", "",
                                    "常用地址" +
                                    (i + 1)));
                        }
                        hasCommonAddress = false;
                    } else if (commonAddressBeanArrayList.size() == 1) {
                        commonAddressBeanArrayList.add(new CommonAddressBean(-1, "", "", "常用地址" +
                                2));
                    }
                    showAddress = commonAddressBeanArrayList;
                    hasCommonAddress = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            headerAndFootWrapper.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onFailure(Exception error, String msg) {

                }
            });
        localHistory = new ArrayList<>();
        searchData = new ArrayList<>();
        addresses = new ArrayList<>();
        emptyAddress = new ArrayList<>();
        myaddress = getIntent().getStringExtra("myaddress");

        //常用地址模拟数据
        for (int i = 0; i < 2; i++) {
            addresses.add(new CommonAddressBean(-1, "", "", "常用地址" + i));
        }

        //添加历史搜索标题
        localHistory = CacheUtil.getSerialToken(this, Constants
                .SEARCH_HISTORY);
        if (localHistory != null) {
            LogUtil.e(TAG, "从缓存获取搜索历史，localHistory＝" + localHistory);
        }
        if (localHistory == null) {
            localHistory = new ArrayList<>();
        }
        historyData = new ArrayList<>();
        historyData.add(new SearchBean(-1, getResources().getString(R.string.tip_174), ""));
        //搜索历史数据
        for (int i = 0; i < localHistory.size(); i++) {
            SearchBean bean = new SearchBean();
            bean.iconId = R.mipmap.poi_search_type_history;
            bean.building = localHistory.get(i);
            bean.streets = "";
            historyData.add(bean);
        }
        showData = historyData;
        showAddress = addresses;
        mycity = getIntent().getStringExtra("mycity");
    }


    private void initView() {
        LinearLayout llSearch = (LinearLayout) findViewById(R.id.ll_search);
        etSearch = (EditText) findViewById(R.id.et_search);
        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        rvHistory = (RecyclerView) findViewById(R.id.rv_history);
        RelativeLayout addAddress = (RelativeLayout) findViewById(R.id.add_address);
        addAddress.setOnClickListener(this);
        etSearch.setOnTouchListener(this);
        etSearch.addTextChangedListener(this);
        tvCancel.setOnClickListener(this);
        drawableLeft = getResources().getDrawable(R.drawable.search_icon);
        drawableLeft.setBounds(0, 0, drawableLeft.getIntrinsicWidth(), drawableLeft
                .getIntrinsicHeight());
        drawableRight = getResources().getDrawable(R.mipmap.clear_text);
        //Drawable使用前必须先要setbounds设置宽高才行
        drawableRight.setBounds(0, 0, drawableRight.getIntrinsicWidth(), drawableRight
                .getIntrinsicHeight());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        HistoryAdapter historyAdapter = new HistoryAdapter();
        headerAndFootWrapper = new HeaderAndFootWrapper(historyAdapter);
        historyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View v, int type) {
                TextView tv = (TextView) v.findViewById(R.id.building);
                String str = tv.getText().toString().trim();
                Log.e(TAG, "点击了条目str=" + str);
                if (type == TYPE_HISTORY) {
                    Log.e(TAG, "keyword=" + str);
                    //点击条目开始搜索
                    if (TextUtils.equals(str, getResources().getString(R.string.tip_174))) {
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("key_word", str);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (type == TYPE_USUAL_ADDRESS) {
                    if (str.contains(getResources().getString(R.string.tip_175))) {
                        for (int i = 0; i < showAddress.size(); i++) {
                            if (TextUtils.equals(showAddress.get(i).getAddr(), str)) {
                                editAddress(showAddress.get(i).getId());
                            }
                        }
                    } else {
                        //点击条目开始搜索
                        Intent intent = new Intent();
                        intent.putExtra("key_word", str);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                localHistory.add(str);
                boolean b = CacheUtil.cacheSerialToken(getApplicationContext(), Constants
                        .SEARCH_HISTORY, localHistory);
                LogUtil.e(TAG, "缓存搜索历史,b=" + b);
            }
        });
        rvHistory.setLayoutManager(linearLayoutManager);
        rvHistory.setAdapter(headerAndFootWrapper);
        rvHistory.addItemDecoration(new HistoryDecoration(this, LinearLayoutManager.VERTICAL));

        addHeaderAndFooter();
    }

    private void addHeaderAndFooter() {
        View header = View.inflate(this, R.layout.search_history_header, null);
        TextView myLoc = (TextView) header.findViewById(R.id.my_location);
        myLoc.setText(myaddress);

        headerAndFootWrapper.addHeader(header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        addHeader();
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (historyData.size() == 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string
                            .tip_171), Toast.LENGTH_SHORT).show();
                } else {
                    showClearDialog();
                }
            }
        });
        if (localHistory == null || localHistory.size() == 0) {
            footer.setVisibility(View.GONE);
        }
    }

    private void addHeader() {
        footer = new TextView(this);
        footer.setGravity(Gravity.CENTER);
        footer.setText(getResources().getString(R.string.tip_172));
        footer.setTextSize(14);
        LinearLayout.LayoutParams footerParams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        footerParams.setMargins((int) DpPxUtil.getPix(this, 15),
                (int) DpPxUtil.getPix(this, 15),
                (int) DpPxUtil.getPix(this, 15),
                (int) DpPxUtil.getPix(this, 250));
        footer.setLayoutParams(footerParams);
        headerAndFootWrapper.addFooter(footer);
    }

    private void showClearDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.tip_173))
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getResources().getString(R.string.confirm), new
                        DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //清空历史记录
                                historyData.clear();
                                historyData.add(new SearchBean(-1, getResources().getString(R
                                        .string.tip_174), ""));
                                headerAndFootWrapper.notifyDataSetChanged();
                                dialog.dismiss();
                                headerAndFootWrapper.removeFooter(0);
                                CacheUtil.clearCache(getApplicationContext(), Constants
                                        .SEARCH_HISTORY);
//                                footer.setVisibility(View.GONE);
                            }
                        })
                .show();
    }

    private class HistoryAdapter extends RecyclerView.Adapter<ViewHolder> {

        public OnItemClickListener mListener;

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HISTORY) {
                parent = getHistoryHolder();
                return new HistoryViewHolder(parent);
            } else if (viewType == TYPE_USUAL_ADDRESS) {
                parent = getUsualAddressHolder();
                return new UsualAddressHolder(parent);
            }
            return null;
        }

        @NonNull
        private ViewGroup getHistoryHolder() {
            ViewGroup parent;
//            parent = (ViewGroup) View.inflate(getApplicationContext(), R.layout.item_history,
// false);
            parent = (ViewGroup) LayoutInflater.from(getApplicationContext()).inflate(R.layout
                    .item_history, rvHistory, false);
            //添加点击事件
            parent.findViewById(R.id.fl_history).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(v, TYPE_HISTORY);
                }
            });
            return parent;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (holder instanceof HistoryViewHolder) {
                HistoryViewHolder historyHolder = (HistoryViewHolder) holder;
                LogUtil.e(TAG, "showData=" + showData);
                SearchBean bean = showData.get(position - showAddress.size());
                if (bean.iconId != -1) {
                    historyHolder.ivIcon.setImageResource(bean.iconId);
                    historyHolder.tvBuilding.setText(bean.building);
                    historyHolder.tvStreets.setText(bean.streets);
                } else {
                    historyHolder.tvBuilding.setText(bean.building);
//                    historyHolder.tvBuilding.setGravity(Gravity.BOTTOM);
                }
            } else if (holder instanceof UsualAddressHolder) {
                UsualAddressHolder usualAddressHolder = (UsualAddressHolder) holder;
                CommonAddressBean bean = showAddress.get(position);
                final long addressID = bean.getId();
                String addr = bean.getAddr();
                usualAddressHolder.tvAddress.setText(TextUtils.isEmpty(addr) ? getResources()
                        .getString(R.string.tip_105) : addr);
                if (addressID == -1) {
                    usualAddressHolder.tvTip.setVisibility(View.VISIBLE);
                } else {
                    usualAddressHolder.tvTip.setVisibility(View.GONE);
                }
                usualAddressHolder.ivIcon.setImageResource(addressID == -1 ? R.drawable
                        .searchlocation_grey : R.drawable.search_locationlight);

                usualAddressHolder.lLContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (addressID == -1) {
                            editAddress(addressID);
                        } else {
                            //点击条目开始搜索
                            Intent intent = new Intent();
                            for (int i = 0; i < showAddress.size(); i++) {
                                if (showAddress.get(i).getId() == addressID) {
                                    String keyWord = showAddress.get(i).getAddr();
                                    intent.putExtra("key_word", keyWord);
                                    setResult(RESULT_OK, intent);
                                    //常用地址不保存到历史搜索
//                                    localHistory.add(keyWord);
//                                    boolean b = CacheUtil.cacheSerialToken(getApplicationContext
//                                            (), Constants
//                                            .SEARCH_HISTORY, localHistory);
//                                    LogUtil.e(TAG, "缓存搜索历史,b=" + b);
                                    finish();
                                    return;
                                }
                            }
                        }

                    }
                });
                //点击编辑监听
                usualAddressHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //编辑该条目
                        editAddress(addressID);
                    }
                });
                //点击删除监听
                usualAddressHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtil.e(TAG, "position=" + position);
                        if (showAddress.get(position).getId() == -1) {
                            return;
                        }
                        //删除该条目
                        CommonAddressBean remove = showAddress.remove(position);
                        if (commonAddressBeanArrayList.size() == 1) {
                            if (remove.getId() != -1) {
                                CommonAddressBean commonAddressBean = commonAddressBeanArrayList
                                        .get(0);
                                if (commonAddressBean.getId() != -1) {
                                    commonAddressBeanArrayList.add(new CommonAddressBean(-1, "",
                                            "", "常用地址" + 2));
                                } else {
                                    commonAddressBeanArrayList.add(0, new CommonAddressBean(-1, "",
                                            "", "常用地址" + 1));
                                }

                            }
                        }
                        headerAndFootWrapper.notifyDataSetChanged();
                        deleteAddress(remove.getId());
                    }
                });
            }
        }


        @Override
        public int getItemViewType(int position) {
            if (showAddress == null) {
                return 0;
            }
            return position < showAddress.size() ? TYPE_USUAL_ADDRESS : TYPE_HISTORY;
        }

        @Override
        public int getItemCount() {
            return (showData==null? 0:showData.size()) + (showAddress==null? 0:showAddress.size())
                    ;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public ViewGroup getUsualAddressHolder() {
            final ViewGroup usualAddressHolder;
            usualAddressHolder = (ViewGroup) View.inflate(getApplicationContext(), R.layout
                    .item_usual_address_list, null);
            //添加点击事件
            usualAddressHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(v, TYPE_USUAL_ADDRESS);
                }
            });
            return usualAddressHolder;
        }
    }

    /**
     * 删除一条常用地址
     *
     * @param id
     */
    private void deleteAddress(long id) {
        if (httpAccountOtherBeanUtil == null) return;
        httpAccountOtherBeanUtil.deketeCommonAddress(mAuthNativeToken.getAuthToken()
                .getAccess_token(), id, new HttpCallbackHandler<String>() {

            @Override
            public void onSuccess(String result) {
                ToastUtil.showUIThread(SearchActivity.this, "OK");
            }

            @Override
            public void onFailure(Exception error, String msg) {
                ToastUtil.showUIThread(SearchActivity.this, "FAILED");
            }
        });
    }

    private void editAddress(long addressID) {
        if (!AccountUtil.isLogin(this)) {
            startActivity(new Intent(this, RegisterActivity.class));
            return;
        }
        Intent editAddress = new Intent(this, EditAddressActivity.class);
        LogUtil.e(TAG, "传递参数addressID＝" + addressID);
        editAddress.putExtras(getAuthBundle());
        editAddress.putExtra("requestcode", REQUEST_EDIT_ADDRESS);
        editAddress.putExtra("addressid", addressID);
        editAddress.putExtra("mycity", mycity);
        startActivityForResult(editAddress, REQUEST_EDIT_ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        long addressid = data.getLongExtra("addressid", -1);
        String address = data.getStringExtra("address");
        LogUtil.e(TAG, "更新常用地址id=" + addressid);
        switch (requestCode) {
            case REQUEST_ADD_ADDRESS:
                for (int i = 0; i < showAddress.size(); i++) {
                    if (TextUtils.equals(showAddress.get(i).getAddr(), address)) {
                        return;
                    }
                }
                if (commonAddressBeanArrayList.get(0).getId() == -1) {
                    CommonAddressBean commonAddressBean = commonAddressBeanArrayList.get(0);
                    commonAddressBean.setAddr(address);
                    commonAddressBean.setId(addressid);
                } else if (commonAddressBeanArrayList.get(1).getId() == -1) {
                    CommonAddressBean commonAddressBean = commonAddressBeanArrayList.get(1);
                    commonAddressBean.setAddr(address);
                    commonAddressBean.setId(addressid);
                } else {
                    commonAddressBeanArrayList.add(new CommonAddressBean(addressid, "", "",
                            address));
                }
                headerAndFootWrapper.notifyDataSetChanged();
                LogUtil.e(TAG, "添加一条常用地址数据，address=" + address);
                break;
            case REQUEST_EDIT_ADDRESS:
                if (commonAddressBeanArrayList.get(0).getId() == -1) {
                    CommonAddressBean commonAddressBean = commonAddressBeanArrayList.get(0);
                    commonAddressBean.setAddr(address);
                    commonAddressBean.setId(addressid);
                } else if (commonAddressBeanArrayList.get(1).getId() == -1) {
                    CommonAddressBean commonAddressBean = commonAddressBeanArrayList.get(1);
                    commonAddressBean.setAddr(address);
                    commonAddressBean.setId(addressid);
                } else {
                    for (int i = 0; i < commonAddressBeanArrayList.size(); i++) {
                        CommonAddressBean commonAddressBean = showAddress.get(i);
                        if (commonAddressBean.getId() == addressid) {
                            commonAddressBean.setAddr(address);
                            commonAddressBean.setId(addressid);
                            break;
                        }
                    }
                }
                headerAndFootWrapper.notifyDataSetChanged();
                break;
        }
    }

    class UsualAddressHolder extends ViewHolder {
        public TextView tvTip;
        public TextView tvAddress;
        public TextView tvDelete;
        public TextView tvEdit;
        public ImageView ivIcon;
        public LinearLayout lLContent;

        public UsualAddressHolder(View itemView) {
            super(getApplicationContext(), itemView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout
                    .LayoutParams.MATCH_PARENT, LinearLayout
                    .LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(layoutParams);
            tvAddress = (TextView) itemView.findViewById(R.id.address);
            tvTip = (TextView) itemView.findViewById(R.id.tv_tip);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
            tvEdit = (TextView) itemView.findViewById(R.id.tv_edit);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            lLContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
        }
    }

    class HistoryViewHolder extends ViewHolder {

        public ImageView ivIcon;
        public TextView tvBuilding;
        public TextView tvStreets;

        public HistoryViewHolder(View itemView) {
            super(getApplicationContext(), itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvBuilding = (TextView) itemView.findViewById(R.id.building);
            tvStreets = (TextView) itemView.findViewById(R.id.streets);
        }
    }

    /**
     * 条目点击回调接口
     */
    interface OnItemClickListener {
        void onClick(View v, int type);
    }

    /**
     * 设置分割线的类
     */
    private class HistoryDecoration extends RecyclerView.ItemDecoration {
        private int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

        private Drawable mDivider;

        private int mOrientation;

        public HistoryDecoration(Context context, int orientation) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
            setOrientation(orientation);
        }

        public void setOrientation(int orientation) {
            if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrientation = orientation;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent) {
            if (mOrientation == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }


        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                android.support.v7.widget.RecyclerView v = new android.support.v7.widget
                        .RecyclerView(parent.getContext());
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }


    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_search);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (!(view.getId() == R.id.et_search)) return false;
        Drawable drawable = etSearch.getCompoundDrawables()[2];
        if (drawable == null) return false;
        //如果不是按下事件，不再处理
        if (motionEvent.getAction() != MotionEvent.ACTION_UP)
            return false;
        //getIntrinsicHeight返回的是以dp为单位的
        if (motionEvent.getX() > etSearch.getWidth()
                - etSearch.getPaddingRight()
                - drawable.getIntrinsicWidth() &&
                motionEvent.getY() < etSearch.getHeight()) {
            etSearch.setText("");
            Log.e(TAG, "etSearch被清空");
            etSearch.setCompoundDrawables(drawableLeft, null, null, null);
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.add_address:
                if (!AccountUtil.isLogin(this)) {
                    startActivity(new Intent(this, RegisterActivity.class));
                    return;
                }
                Intent intent = new Intent(this, EditAddressActivity.class);
                intent.putExtras(getAuthBundle());
                intent.putExtra("mycity", mycity);
                intent.putExtra("requestcode", REQUEST_ADD_ADDRESS);
                startActivityForResult(intent, REQUEST_ADD_ADDRESS);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String newText = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(newText)) {
            etSearch.setCompoundDrawables(drawableLeft, null, null, null);
            showData = historyData;
//            showAddress = hasCommonAddress ? commonAddressBeanArrayList : addresses;
            showAddress = commonAddressBeanArrayList;
            headerAndFootWrapper.notifyDataSetChanged();
            rvHistory.scrollToPosition(0);
            footer.setVisibility(View.VISIBLE);
            Log.e(TAG, "etSearch为空");
        } else {
            etSearch.setCompoundDrawables(null, null, drawableRight, null);
            showData = searchData;
            showAddress = emptyAddress;
            rvHistory.scrollToPosition(0);
            Log.e(TAG, "etSearch不为空");
            if (footer != null) {
                footer.setVisibility(View.INVISIBLE);
            }
            //添加提示
            String city = "北京";
            InputtipsQuery inputquery = new InputtipsQuery(newText, city);
            inputquery.setCityLimit(true);
            Inputtips inputTips = new Inputtips(SearchActivity.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
            headerAndFootWrapper.notifyDataSetChanged();
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * 监听输入提示内容，显示提示内容
     *
     * @param tipList
     * @param rCode
     */
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            searchData.clear();//清除上次搜索结果
            for (int i = 0; i < tipList.size(); i++) {
                SearchBean bean = new SearchBean();
                bean.iconId = R.mipmap.poi_search_type_location;
                bean.building = tipList.get(i).getName() + "-" + tipList.get(i).getDistrict();
                String address = tipList.get(i).getAddress();
                bean.streets = TextUtils.isEmpty(address) ? getResources().getString(R.string
                        .tip_176) : address;
                searchData.add(bean);
            }
            headerAndFootWrapper.notifyDataSetChanged();
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
}
