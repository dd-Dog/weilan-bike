package com.wlcxbj.bike.ble;

import android.content.Context;

import com.inuker.bluetooth.library.BluetoothClient;


/**
 * Created by Administrator on 2017/3/28.
 */

public class BleClientManager {
    private static BluetoothClient mClient;

    private BleClientManager(){}
    public static BluetoothClient getClient(Context context) {
        if (mClient == null) {
            synchronized (BleClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(context);
                }
            }
        }
        return mClient;
    }
}
