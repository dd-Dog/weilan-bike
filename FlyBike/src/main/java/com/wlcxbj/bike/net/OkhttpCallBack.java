package com.wlcxbj.bike.net;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/10.
 */
public interface OkhttpCallBack {
    void success(Response response);
    void failure(Exception error, String msg);
}
