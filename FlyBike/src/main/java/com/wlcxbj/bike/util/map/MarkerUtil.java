package com.wlcxbj.bike.util.map;

import android.text.TextUtils;
import com.amap.api.services.cloud.CloudItem;
import java.util.ArrayList;


/**
 * Created by Administrator on 2017/2/16.
 */
public class MarkerUtil {
    private static final String TAG = "MarkerUtil";

    /**
     * 比较获取要添加的Markers
     *
     * @param oldList 上次显示的Markers
     * @param newList 从云图刚刚检索到的markers
     * @return
     */
    public static ArrayList<CloudItem> getAddList(ArrayList<CloudItem> oldList, ArrayList<CloudItem>
            newList) {
        ArrayList<CloudItem> and = getAnd(oldList, newList);
        ArrayList<CloudItem> add = getDiff(newList, and);
        return add;
    }
    /**
     * 比较获取要删除的Markers
     *
     * @param oldList 上次显示的Markers
     * @param newList 从云图刚刚检索到的markers
     * @return
     */
    public static ArrayList<CloudItem> getRemoveList(ArrayList<CloudItem> oldList, ArrayList<CloudItem>
            newList) {
        ArrayList<CloudItem> and = getAnd(oldList, newList);
        ArrayList<CloudItem> remove = getDiff(oldList, and);
        return remove;
    }

    /**
     * 获取list1-list2的差集
     * @param list1
     * @param list2
     * @return
     */
    public static ArrayList<CloudItem> getDiff(ArrayList<CloudItem> list1, ArrayList<CloudItem> list2) {
        ArrayList<CloudItem> diff = new ArrayList<>();
        for (CloudItem item : list1) {
            if (!contains(list2, item)) {
                diff.add(item);
            }
        }
        return diff;
    }


    /**
     * 获取两个list的交集
     * @param list1
     * @param list2
     * @return
     */
    public static ArrayList<CloudItem> getAnd(ArrayList<CloudItem> list1, ArrayList<CloudItem> list2) {
        ArrayList<CloudItem> and = new ArrayList<>();
        for (CloudItem item : list1) {
            if (contains(list2, item)) {
                and.add(item);
            }
        }
        return and;
    }


    /**
     * 比较是否包含
     * @param list
     * @param item
     */
    public static boolean contains(ArrayList<CloudItem> list, CloudItem item) {
        boolean contains = false;
        for (CloudItem i : list) {
            if (equals(i, item)) {
                contains = true;
            }
        }
        return contains;
    }

    /**
     * 比较两个CloudItem是否相等
     *
     * @param c1
     * @param c2
     * @return
     */
    public static boolean equals(CloudItem c1, CloudItem c2) {
        if (TextUtils.equals(c1.getCustomfield().get("tid"), c2.getCustomfield().get("tid"))) {
            return true;
        }
        return false;
    }
}
