package com.wlcxbj.bike.oss;

import android.content.Context;
import android.os.Looper;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.InputStream;

import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.util.LogUtil;

/**
 * Created by Administrator on 2017/2/10.
 */
public class OSSUtil {
    private static final String TAG = "OSSUtil";
    private static UIDispatcher uiDispatcher;
    private OssDownloadCallBackHandler ossDownloadCallBackHandler;
    private OssUploadCallBackHandler ossUploadCallBackHandler;

    public OSSUtil() {
        uiDispatcher = new UIDispatcher(Looper.getMainLooper());
    }


    //初始化一个OssService用来上传下载
    public OssService createReadOssService(Context context, String endpoint, String stsServer, String
            bucket, String tokenStr) {
        //如果希望直接使用accessKey来访问的时候，可以直接使用OSSPlainTextAKSKCredentialProvider来鉴权。
        //OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider
        // (accessKeyId, accessKeySecret);

        //使用自己的获取STSToken的类

        OSSCredentialProvider credentialProvider = new STSGetter(stsServer, context, tokenStr,
                Constants.READ);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);
        return new OssService(oss, bucket);
    }
    //初始化一个OssService用来上传下载

    /**
     * 重载
     *
     * @param context
     * @param endpoint
     * @param stsServer
     * @param tokenStr
     * @return
     */
    public OssService createReadOssService(Context context, String endpoint, String stsServer, String
            tokenStr) {
        //如果希望直接使用accessKey来访问的时候，可以直接使用OSSPlainTextAKSKCredentialProvider来鉴权。
        //OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider
        // (accessKeyId, accessKeySecret);

        //使用自己的获取STSToken的类
        OSSCredentialProvider credentialProvider = new STSGetter(stsServer, context, tokenStr,
                Constants.READ);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);
        return new OssService(oss);
    }

    /**
     * 创建可写的ossService
     *
     * @param context
     * @param endpoint
     * @param stsServer
     * @param tokenStr
     * @return
     */
    public OssService createWriteOssService(Context context, String endpoint, String stsServer, String
            tokenStr) {
        //如果希望直接使用accessKey来访问的时候，可以直接使用OSSPlainTextAKSKCredentialProvider来鉴权。
        //OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider
        // (accessKeyId, accessKeySecret);

        //使用自己的获取STSToken的类
        OSSCredentialProvider credentialProvider = new STSGetter(stsServer, context, tokenStr,
                Constants.WRITE);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);
        return new OssService(oss);
    }
    /**
     * 下载文件回调
     *
     * @return
     */
    public UICallback<GetObjectRequest, GetObjectResult> getGetCallback() {
        return new UICallback<GetObjectRequest, GetObjectResult>(uiDispatcher) {
            @Override
            public void onSuccess(final GetObjectRequest request, final GetObjectResult result) {
                addCallback(new Runnable() {
                    // 请求成功
                    InputStream inputStream = result.getObjectContent();
                    //重载InputStream来获取读取进度信息
                    ProgressInputStream progressStream = new ProgressInputStream(inputStream,
                            new OSSUtil.ProgressCallbackFactory<GetObjectRequest>().get(),
                            result.getContentLength());

                    @Override
                    public void run() {
                        ossDownloadCallBackHandler.onSuccess(request, result, progressStream);
                    }
                }, null);
                super.onSuccess(request, result);
            }

            @Override
            public void onFailure(final GetObjectRequest request, final ClientException
                    clientExcepion,
                                  final ServiceException serviceException) {

                addCallback(null, new Runnable() {
                    @Override
                    public void run() {
                        ossDownloadCallBackHandler.onFailure(request, clientExcepion,
                                serviceException);
                    }
                });
                super.onFailure(request, clientExcepion, serviceException);
            }
        };
    }


    /**
     * 上传文件回调
     *
     * @return
     */
    public UICallback<PutObjectRequest, PutObjectResult> getPutCallback() {
        return new UICallback<PutObjectRequest, PutObjectResult>(uiDispatcher) {
            @Override
            public void onSuccess(final PutObjectRequest request, final PutObjectResult result) {

                addCallback(new Runnable() {
                    @Override
                    public void run() {
                        ossUploadCallBackHandler.onSuccess(request, result);
                    }
                }, null);
                super.onSuccess(request, result);
            }

            @Override
            public void onFailure(final PutObjectRequest request, final ClientException
                    clientExcepion, final ServiceException serviceException) {
                addCallback(null, new Runnable() {
                    @Override
                    public void run() {
                        ossUploadCallBackHandler.onFailure(request, clientExcepion,
                                serviceException);
                    }
                });
                super.onFailure(request, clientExcepion, serviceException);
            }
        };
    }

    /**
     * 下载进度回调工厂类
     *
     * @param <T>
     */
    public static class ProgressCallbackFactory<T> {
        public ProgressCallbackFactory() {
        }

        public UIProgressCallback<T> get() {
            return new UIProgressCallback<T>(uiDispatcher) {
                @Override
                public void onProgress(T request, long currentSize, long totalSize) {
                    final int progress = (int) (100 * currentSize / totalSize);
                    addCallback(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.e(TAG, "当前进度:" + progress);
                        }
                    });
                    super.onProgress(request, currentSize, totalSize);
                }
            };
        }
    }


    public void setUploadOssCallBackHandler(OssUploadCallBackHandler ossUploadCallBackHandler) {
        this.ossUploadCallBackHandler = ossUploadCallBackHandler;
    }

    public void setDownloadOssCallBackHandler(OssDownloadCallBackHandler
                                                      ossDownloadCallBackHandler) {
        this.ossDownloadCallBackHandler = ossDownloadCallBackHandler;
    }

    /**
     * 请求上传的回调接口
     */
    public interface OssUploadCallBackHandler {
        void onSuccess(PutObjectRequest request, PutObjectResult result);

        void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException
                serviceException);
    }


    /**
     * 请求下载结果的回调接口
     */
    public interface OssDownloadCallBackHandler {
        void onSuccess(GetObjectRequest request, GetObjectResult result, ProgressInputStream
                progressStream);

        void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException
                serviceException);
    }
}
