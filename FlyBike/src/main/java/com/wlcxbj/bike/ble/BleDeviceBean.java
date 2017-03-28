package com.wlcxbj.bike.ble;

import android.bluetooth.BluetoothDevice;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/14.
 */

public class BleDeviceBean {
    private BluetoothDevice device;
    private int rssi;
    private byte[] scanRecord;

    public BleDeviceBean(BluetoothDevice device, int rssi, byte[] scanRecord) {
        this.device = device;
        this.rssi = rssi;
        this.scanRecord = scanRecord;
    }

    @Override
    public String toString() {
        return "BleDeviceBean{" +
                "device=" + device +
                ", rssi=" + rssi +
                ", scanRecord=" + Arrays.toString(scanRecord) +
                '}';
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public byte[] getScanRecord() {
        return scanRecord;
    }

    public void setScanRecord(byte[] scanRecord) {
        this.scanRecord = scanRecord;
    }
}
