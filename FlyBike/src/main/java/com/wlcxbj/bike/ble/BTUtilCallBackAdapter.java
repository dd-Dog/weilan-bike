package com.wlcxbj.bike.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import java.util.List;

/**
 * Created by bain on 17-3-23.
 */

public class BTUtilCallBackAdapter implements BleUtil.BTUtilListener {
    @Override
    public void onLeScanStart() {
        Log.e("BTUtilCallBackAdapter","onLeScanStart");
    }

    @Override
    public void onLeScanStop() {
        Log.e("BTUtilCallBackAdapter","onLeScanStop");
    }

    @Override
    public void onLeScanDevices(List<BleDeviceBean> listDevice) {
        Log.e("BTUtilCallBackAdapter","onLeScanDevices");
    }

    @Override
    public void onConnected(BluetoothDevice mCurDevice) {
        Log.e("BTUtilCallBackAdapter","onConnected");
    }

    @Override
    public void onDisConnected(BluetoothDevice mCurDevice) {
        Log.e("BTUtilCallBackAdapter","onDisConnected");
    }

    @Override
    public void onConnecting(BluetoothDevice mCurDevice) {
        Log.e("BTUtilCallBackAdapter","BTUtilCallBackAdapter");
    }

    @Override
    public void onDisConnecting(BluetoothDevice mCurDevice) {
        Log.e("BTUtilCallBackAdapter","onDisConnecting");
    }

    @Override
    public void onStrength(int strength) {
        Log.e("BTUtilCallBackAdapter","onStrength");
    }

    @Override
    public void onModel(int model) {
        Log.e("BTUtilCallBackAdapter","BTUtilCallBackAdapter");
    }

    @Override
    public void onServiceDiscover(BluetoothGatt gatt) {
        Log.e("BTUtilCallBackAdapter","onServiceDiscover");
    }

    @Override
    public void onCharRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.e("BTUtilCallBackAdapter","onCharRead");
    }

    @Override
    public void onCharWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.e("BTUtilCallBackAdapter","onCharWrite");
    }

    @Override
    public void onCharateristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        Log.e("BTUtilCallBackAdapter","onCharateristicChanged");
    }
}
