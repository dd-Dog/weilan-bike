package com.wlcxbj.bike.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bain on 17-1-22.
 */
public class BlueToothReadThread extends Thread {

    private BluetoothSocket socket;
    private BlueToothReadCallBackHandler handler;
    public BlueToothReadThread(BluetoothSocket socket, BlueToothReadCallBackHandler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    //读取数据
    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;
        InputStream mmInStream = null;
        try {
            mmInStream = socket.getInputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
            handler.onError(e1.toString());
        }
        while (true) {
            try {
                // Read from the InputStream
                if ((bytes = mmInStream.read(buffer)) > 0) {
                    byte[] buf_data = new byte[bytes];
                    for (int i = 0; i < bytes; i++) {
                        buf_data[i] = buffer[i];
                    }
                    String s = new String(buf_data);
                    handler.onReceive(s);
                    Log.e("ReadThread", "client:" + s);
                }
            } catch (IOException e) {
                handler.onError(e.toString());
                try {
                    mmInStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            }
        }
    }
}
