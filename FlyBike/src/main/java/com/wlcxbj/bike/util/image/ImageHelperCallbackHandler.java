package com.wlcxbj.bike.util.image;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

/**
 * Created by Administrator on 2017/2/24.
 */
public interface ImageHelperCallbackHandler {
    void onUploadSuccess(PutObjectRequest request, PutObjectResult result);
    void onUploadFailure(PutObjectRequest request, ClientException clientExcepion,
                   ServiceException serviceException);
}
