package com.wlcxbj.bike.util.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.View;

import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;

import com.wlcxbj.bike.R;

/**
 * Created by Administrator on 2017/2/21.
 * Marker聚合类
 */
public class MarkerCluster {
    private MarkerOptions options;
    private ArrayList<MarkerOptions> includeMarkers;
    private LatLngBounds bounds;// 创建区域

    /**
     * @param activity
     * @param centerMarker
     * @param projection
     * @param gridSize     区域大小参数
     */
    public MarkerCluster(Activity activity, MarkerOptions centerMarker,
                         Projection projection, int gridSize) {
        options = new MarkerOptions();
        Activity activity1 = activity;
        //1.把一个中心marker的经纬度坐标转换成屏幕坐标
        Point point = projection.toScreenLocation(centerMarker.getPosition());
        //2.计算出一个marker代表区域的左上角和右下角
        Point southwestPoint = new Point(point.x - gridSize, point.y + gridSize);
        Point northeastPoint = new Point(point.x + gridSize, point.y - gridSize);
        //3.生成区域的对象
        bounds = new LatLngBounds(
                projection.fromScreenLocation(southwestPoint),
                projection.fromScreenLocation(northeastPoint));
        options.anchor(0.5f, 0.5f).title(centerMarker.getTitle())
                .position(centerMarker.getPosition())
                .icon(centerMarker.getIcon())
                .snippet(centerMarker.getSnippet());
        includeMarkers = new ArrayList<>();
        includeMarkers.add(centerMarker);
    }

    /**
     * 添加marker到Cluster中
     */
    public void addMarker(MarkerOptions markerOptions) {
        includeMarkers.add(markerOptions);// 添加到列表中
    }

    /**
     * 设置聚合点的中心位置以及图标
     * @param tid 中心图标的title，自行车编号
     */
    public void setpositionAndIcon(Context context, String tid) {
        String id = tid;
        int size = includeMarkers.size();
        if (size == 1) {
            return;
        }
        double lat = 0.0;
        double lng = 0.0;
        String snippet = "";
        //计算中心的位置，经纬度的平均值
        for (MarkerOptions op : includeMarkers) {
            lat += op.getPosition().latitude;
            lng += op.getPosition().longitude;
            snippet += op.getTitle() + "\n";
        }
        options.position(new LatLng(lat / size, lng / size));// 设置中心位置为聚集点的平均距离
        options.title(id);
        options.snippet(snippet);
        int iconType = size / 2;
        switch (iconType) {
            default:
                options.icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory
                                .decodeResource(context.getResources(), R.drawable
                                        .bicycle_position)));
                break;
        }
    }

    public LatLngBounds getBounds() {
        return bounds;
    }

    public MarkerOptions getOptions() {
        return options;
    }

    public void setOptions(MarkerOptions options) {
        this.options = options;
    }

    public View getView(int carNum, String text, int resourceId) {
//        View view = activity.getLayoutInflater().inflate(R.layout.my_car_cluster_view, null);
//        TextView carNumTextView = (TextView) view.findViewById(R.id.my_car_num);
//        TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
//        TextView tv_price_status = (TextView) view.findViewById(R.id.tv_price_status);
//        tv_price.setText(text);
//        tv_price_status.setText("元/天");
//        tv_price.setTextColor(Color.parseColor("#FFBB18"));
//        tv_price_status.setTextColor(Color.parseColor("#FFBB18"));
//        LinearLayout myCarLayout = (LinearLayout) view.findViewById(R.id.my_car_bg);
//        myCarLayout.setBackgroundResource(resourceId);
//        carNumTextView.setText(String.valueOf(carNum));
//        return view;
        return null;
    }

    /**
     * 把一个view转化成bitmap对象
     */
    public static Bitmap getViewBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }
}
