package com.wlcxbj.bike.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import com.wlcxbj.bike.R;
import com.wlcxbj.bike.bean.RouteBean;
import com.wlcxbj.bike.util.TimeUtil;


/**
 * Created by bain on 16-11-28.
 */
public class HelpActivity extends BaseActivity implements View.OnClickListener {


    ListView lvRoutes;
    private ArrayList<Object> routeBeens;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        findViewById(R.id.ib_back).setOnClickListener(this);
        lvRoutes = (ListView) findViewById(R.id.lv_routes);
        routeBeens = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RouteBean routeBean = new RouteBean(123456789 + i*100000, 15234789 + i*100000, 10000000 * (2 + i), i + "", 3611 * (i + 3), 123.1f * (2 + i));
            routeBeens.add(routeBean);
        }
        lvRoutes.setAdapter(new RouteAdapter());
        lvRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RouteProblemFeedBackActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item_feedback", (RouteBean)routeBeens.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    class RouteAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return routeBeens.size();
        }

        @Override
        public Object getItem(int position) {
            return routeBeens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder =  new ViewHolder();
                convertView = View.inflate(getApplicationContext(), R.layout.item_list_route_help, null);
                viewHolder.tvtime = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.tvTimeLength = (TextView) convertView.findViewById(R.id.tv_time_length);
                viewHolder.tvBikeNum = (TextView) convertView.findViewById(R.id.tv_bikeNum);
                viewHolder.tvUsingMoney = (TextView) convertView.findViewById(R.id.tv_using_money);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            RouteBean bean = (RouteBean) getItem(position);
            viewHolder.tvtime.setText(TimeUtil.getTimeStr(bean.time));
            viewHolder.tvBikeNum.setText(bean.bikeID);
            viewHolder.tvTimeLength.setText(TimeUtil.getTimeShort(bean.usingTime));
            viewHolder.tvUsingMoney.setText(bean.money + "元");
            return convertView;
        }

    }

    private static class ViewHolder {
        TextView tvtime;
        TextView tvTimeLength;
        TextView tvBikeNum;
        TextView tvUsingMoney;
    }

    @Override
    public void setContentViewID() {
        setContentView(R.layout.activity_route_help);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
        }
    }
}
