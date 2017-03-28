package com.wlcxbj.bike.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by bain on 17-1-22.
 */
public class BlueToothServer extends Thread {

    private BluetoothAdapter mBluetoothAdapter;
    private BlueToothReadCallBackHandler handler;
    public BlueToothServer(BluetoothAdapter mBluetoothAdapter, BlueToothReadCallBackHandler handler) {
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.handler = handler;
    }

    /* 常量，代表服务器的名称 */
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";

    public void run() {
        try {

            // 参数：服务器名称、UUID
            BluetoothServerSocket mserverSocket = mBluetoothAdapter
                    .listenUsingRfcommWithServiceRecord
                    (PROTOCOL_SCHEME_RFCOMM,
                            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            Log.e("server", "wait cilent connect...");
            while (true) {
                BluetoothSocket socket = mserverSocket.accept();
                Log.d("server", "accept success !");
                //启动接收数据
                BlueToothReadThread mreadThread = new BlueToothReadThread(socket, handler);
                mreadThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("server", "连接失败，" + e.toString());
        }
    }


}
