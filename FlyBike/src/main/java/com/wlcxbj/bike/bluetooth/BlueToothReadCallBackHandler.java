package com.wlcxbj.bike.bluetooth;

/**
 * Created by bain on 17-1-22.
 */
public interface BlueToothReadCallBackHandler {

    void onReceive(String msg);
    void onError(String error);
    void finish();
}
