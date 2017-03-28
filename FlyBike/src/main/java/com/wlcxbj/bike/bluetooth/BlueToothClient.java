package com.wlcxbj.bike.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


/**
 * 蓝牙客户端类
 */
public class BlueToothClient extends Thread {

    private static final String TAG = "BTClient";
    private BlueToothReadCallBackHandler handler;
    private BluetoothDevice device;
    private BluetoothSocket clientSocket;
    private BlueToothReadThread mreadThread;

    public BlueToothClient(BluetoothDevice device, BlueToothReadCallBackHandler handler) {
        this.device = device;
        this.handler = handler;
    }

    /**
     * run方法，连接服务器
     */
    public void run() {
        try {
            Log.e(TAG, "DEVICE=" + "name=" + device.getName() + "device=" + device
                    .toString());
            Log.e(TAG, "正在连接服务器。。。");
            clientSocket = device.createRfcommSocketToServiceRecord(UUID.fromString
                    ("00001101-0000-1000-8000-00805F9B34FB"));
            clientSocket.connect();
            mreadThread = new BlueToothReadThread(clientSocket, handler);
            mreadThread.start();
            send("psw:123456");
            handler.finish();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "连接服务器失败:" + e.toString());
        }
    }

    /**
     * 发送消息
     *
     * @param msg
     * @throws IOException
     */
    public void send(String msg) throws IOException {
        OutputStream os = clientSocket.getOutputStream();
        os.write("psw:123456".getBytes());
        Log.e(TAG, "bluetooth-client" + "write:" + "psw:123456");
    }


    /**
     * 关闭线程
     */
    public void close() {
        if (mreadThread != null) {
            mreadThread.interrupt();
            mreadThread = null;
        }
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientSocket = null;
        }
//        interrupt();//关闭本线程
    }
}
