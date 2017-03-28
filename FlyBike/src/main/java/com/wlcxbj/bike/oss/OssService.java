package com.wlcxbj.bike.oss;

import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.util.HashMap;

import com.wlcxbj.bike.util.LogUtil;

/**
 * Created by oss on 2015/12/7 0007.
 * 支持普通上传，普通下载和断点上传
 */
public class OssService {

    private OSS oss;
    private String bucket;
    //    private ImageDisplayer ImageDisplayer;
    private MultiPartUploadManager multiPartUploadManager;
    private String callbackAddress;
    //根据实际需求改变分片大小
    private final static int partSize = 256 * 1024;


    public OssService(OSS oss) {
        this.oss = oss;
        this.multiPartUploadManager = new MultiPartUploadManager(oss, bucket, partSize);
    }

    public OssService(OSS oss, String bucket) {
        this.oss = oss;
        this.bucket = bucket;
        this.multiPartUploadManager = new MultiPartUploadManager(oss, bucket, partSize);
    }

    public void setCallbackAddress(String callbackAddress) {
        this.callbackAddress = callbackAddress;
    }

    /**
     * 设置bucket
     *
     * @param bucket
     */
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }


    public void asyncGetImage(String object,
                              @NonNull final OSSCompletedCallback<GetObjectRequest,
                                      GetObjectResult> userCallback) {
        if ((object == null) || object.equals("")) {
            Log.w("AsyncGetImage", "ObjectNull");
            return;
        }

        Log.d("GetImage", "Start");
        GetObjectRequest get = new GetObjectRequest(bucket, object);
        OSSAsyncTask task = oss.asyncGetObejct(get, userCallback);
    }

    public void asyncPutImage(String object, String localFile,
                              @NonNull final OSSCompletedCallback<PutObjectRequest,
                                      PutObjectResult> userCallback,
                              final OSSProgressCallback<PutObjectRequest> userProgressCallback) {
        if (object.equals("")) {
            LogUtil.e("AsyncPutImage", "ObjectNull");
            return;
        }

        File file = new File(localFile);
        if (!file.exists()) {
            LogUtil.e("AsyncPutImage", "FileNotExist,localFile=" + localFile);
            LogUtil.e("LocalFile", localFile);
            return;
        }


        // 构造上传请求
        final PutObjectRequest put = new PutObjectRequest(bucket, object, localFile);
        // 文件元信息的设置是可选的
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("application/x-png"); // 设置content-type
//        metadata.setContentMD5(BinaryUtil.calculateBase64Md5(uploadFilePath)); // 校验MD5
        put.setMetadata(metadata);
        if (callbackAddress != null) {
            // 传入对应的上传回调参数，这里默认使用OSS提供的公共测试回调服务器地址
            put.setCallbackParam(new HashMap<String, String>() {
                {
                    put("callbackUrl", callbackAddress);
                    //callbackBody可以自定义传入的信息
                    put("callbackBody", "filename=${object}");
                }
            });
        }

        // 异步上传时可以设置进度回调
        if (userProgressCallback != null) {
            put.setProgressCallback(userProgressCallback);
        }
        /*
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                //Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                int progress = (int) (100 * currentSize / totalSize);

                ImageDisplayer.updateProgress(progress);
                ImageDisplayer.displayInfo("上传进度: " + String.valueOf(progress) + "%");
            }
        });*/

        OSSAsyncTask task = oss.asyncPutObject(put, userCallback);
    }

    //断点上传，返回的task可以用来暂停任务
    public PauseableUploadTask asyncMultiPartUpload(String object,
                                                    String localFile,
                                                    @NonNull final
                                                    OSSCompletedCallback<PauseableUploadRequest,
                                                            PauseableUploadResult> userCallback,
                                                    final
                                                    OSSProgressCallback<PauseableUploadRequest>
                                                            userProgressCallback) {
        if (object.equals("")) {
            Log.w("AsyncMultiPartUpload", "ObjectNull");
            return null;
        }

        File file = new File(localFile);
        if (!file.exists()) {
            Log.w("AsyncMultiPartUpload", "FileNotExist");
            Log.w("LocalFile", localFile);
            return null;
        }

        Log.d("MultiPartUpload", localFile);
        PauseableUploadTask task = multiPartUploadManager.asyncUpload(object, localFile,
                userCallback, userProgressCallback);
        return task;
    }

}
