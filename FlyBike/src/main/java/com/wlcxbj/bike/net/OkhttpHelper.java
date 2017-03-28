package com.wlcxbj.bike.net;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.util.LogUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/10.
 */

/**
 * 网络请求的帮助类,对Okhttp进行进一步的封装
 * 使用静态内部类的单例模式
 */
public class OkhttpHelper {

    private static final String TAG = "OkhttpHelper";
    private final OkHttpClient client;
    private final ThreadPoolManager threadMgr;
    private HashMap<String, Call> calls = new HashMap<>();

    private OkhttpHelper() {
        client = new OkHttpClient.Builder()
                .build();
        threadMgr = ThreadPoolManager.getInstance();
    }

    public static OkhttpHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final OkhttpHelper INSTANCE = new OkhttpHelper();
    }

    /**
     * POST请求
     *
     * @param url
     * @param callBack
     */
    public void postReq(final String url, final RequestBody requestBody, final OkhttpCallBack
            callBack) {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
//                        .header("Content-Type", "application/json")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                LogUtil.d(TAG, e.toString());
                callBack.failure(new Exception("网络故障,请求失败"), "请检查网络连接后重试~");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.success(response);
            }
        });

    }

    /**
     * 带有Token的POST请求
     *
     * @param url
     * @param callBack
     */
    public void postReqWithToken(final String url, final RequestBody requestBody, final String
            tokenType, final String tokenStr, final OkhttpCallBack
                                         callBack) {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header(Constants.AUTH_BASE_KEY, tokenType + " " + tokenStr)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                LogUtil.d(TAG, e.toString());
                callBack.failure(new Exception("网络故障,请求失败"), "请检查网络连接后重试~");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.success(response);
            }
        });

    }

    /**
     * POST请求
     *
     * @param url
     * @param callBack
     */

    public void postReqWithAuthHeader(final String url, final RequestBody requestBody, final
    String key, final String value, final OkhttpCallBack callBack) {
        Runnable runnable = new Runnable() {
            public void run() {
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(url)
                        .post(requestBody)
                        .header(key, "Basic " + Base64.encodeToString(value.getBytes(), Base64
                                .NO_WRAP))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .build();
                LogUtil.e(TAG, "request body:" + request.header("Content-Type"));
                Response response = null;
                try {
                    Call call = client.newCall(request);
                    response = call.execute();
                    callBack.success(response);
//                    LogUtil.e("OkhttpHelper", "code=" + response.code());
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.d(TAG, e.toString());
                    callBack.failure(new Exception("网络故障,请求失败"), "请检查网络连接后重试~");
                } finally {
                    if (response != null) {
                        response.close();
                    }
                }
            }
        };
        threadMgr.execute(runnable);
    }


    /**
     * okHttp post同步请求
     */
    public String requestPostBySyn(String requestUrl, RequestBody requestBody, String tokenType,
                                   String tokenStr) {
        try {
            //创建一个请求
            Request.Builder builder = new Request.Builder();
            final Request request = builder.url(requestUrl)
                    .header(Constants.AUTH_BASE_KEY, tokenType + " " + tokenStr)
                    .post(requestBody)
                    .build();
            //创建一个Call
            final Call call = client.newCall(request);
            //执行请求
            Response response = call.execute();
            //请求执行成功
            if (response.isSuccessful()) {
                //获取返回数据 可以是String，bytes ,byteStream
                String result = response.body().string();
                Log.e(TAG, "同步post请求,response ----->" + result);
                return result;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        } finally {

        }
        return "";
    }


    public void cancel(String url) {
        Call call = calls.get(url);
        calls.remove(url);//移除
        if (call != null && call.isExecuted()) {
            call.cancel();
        }
    }
}
