package com.wlcxbj.bike.net.beanutil;

/**
 * Created by Administrator on 2017/2/13.
 */
public interface HttpCallbackHandler<T> {
    void onSuccess(T t);

    void onFailure(Exception error, String msg);
}
