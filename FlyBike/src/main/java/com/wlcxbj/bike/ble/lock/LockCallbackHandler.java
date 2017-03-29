package com.wlcxbj.bike.ble.lock;

import com.inuker.bluetooth.library.search.SearchResult;

/**
 * Created by Administrator on 2017/3/28.
 */

public interface LockCallbackHandler {

    void onSuccess(SearchResult device, byte cmdId);
    void onLocated();

    void onFail(SearchResult device, int code, String msg, byte cmdId);
}
