package com.wlcxbj.bike.util.image;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.wlcxbj.bike.activity.RegisterActivity;
import com.wlcxbj.bike.bean.oss.OSSFileParams;
import com.wlcxbj.bike.bean.account.AuthNativeToken;
import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.oss.ImageDisplayer;
import com.wlcxbj.bike.oss.OSSUtil;
import com.wlcxbj.bike.oss.OssService;
import com.wlcxbj.bike.oss.ProgressInputStream;
import com.wlcxbj.bike.util.LogUtil;
import com.wlcxbj.bike.util.ToastUtil;
import com.wlcxbj.bike.util.cache.CacheUtil;
import com.wlcxbj.bike.util.properties.PropertiesUtil;

/**
 * Created by Administrator on 2017/2/12.
 */
public class ImageHelper {
    private static final String TAG = "ImageHelper";
    private Activity mContext;
    //内存缓存的工具类,使用最近最少使用算法
    private LruCache<String, Bitmap> lruCache;
    private String cacheDir;
    private OSSUtil ossUtil;
    private OssService ossService;
    private final AuthNativeToken authNativeToken;

    public ImageHelper(Activity context) {
        // 获取模拟器运行时可以使用的内存大小 / 8，为什么除以8？
        long maxMemory = Runtime.getRuntime().maxMemory() / 8;
        //参数，Lrucache缓存大小,lrucache要计算缓存大小，需要知道每张图片的大小
        lruCache = new LruCache<String, Bitmap>((int) maxMemory) {
            //Bitmap即将缓存的图片
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 返回当前图片的大小, 给Lrucache去计算当前缓存的内存大小
                //getByteCount()//获取字节数量，就是图片的大小，但是这个方法要求api12以上
                //其实这个方法内部就是getRowBytes() * getHeight()
                return value.getRowBytes() * value.getHeight();
            }
        };
        mContext = context;
        cacheDir = context.getCacheDir().getPath();
        ossUtil = new OSSUtil();
        //获取本地缓存
        authNativeToken = CacheUtil.getSerialToken(mContext, Constants
                .AUTH_CACHE_FILE_NAME);
    }

    /**
     * 上传图片文件
     *
     * @param picturePath 本地文件路径
     * @param objectName  要上传的文件在OSS服务器上的名称
     */
    public void uploadImageToOss(final String picturePath, String objectName) {
        ossUtil.setUploadOssCallBackHandler(new OSSUtil.OssUploadCallBackHandler() {

            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.e(TAG, "上传成功");
                ToastUtil.showUIThread(mContext, "上传成功");
                if (imageHelperCallbackHandler != null) {
                    imageHelperCallbackHandler.onUploadSuccess(request, result);
                }

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion,
                                  ServiceException serviceException) {
                String info = "";
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    info = clientExcepion.toString();
                }
                if (serviceException != null) {
                    // 服务异常
                    LogUtil.e("ErrorCode", serviceException.getErrorCode());
                    LogUtil.e("RequestId", serviceException.getRequestId());
                    LogUtil.e("HostId", serviceException.getHostId());
                    LogUtil.e("RawMessage", serviceException.getRawMessage());
                    info = serviceException.toString();
                }
                final String outputinfo = new String(info);
                LogUtil.e(TAG, outputinfo);
                ToastUtil.showUIThread(mContext, "上传失败");

            }
        });
        ossService = ossUtil.createWriteOssService(mContext, Constants.ENDPOINT, Constants
                        .WRITE_ACCESS_AT_OSS,
                authNativeToken.getAuthToken().getAccess_token());
        //必须设置bucket
        String bucket = PropertiesUtil.getProperties(mContext, PropertiesUtil.OSS_BUCKET);
        if (TextUtils.isEmpty(bucket)) return;
        ossService.setBucket(bucket);
        ossService.asyncPutImage(objectName, picturePath, ossUtil.getPutCallback(), new OSSUtil
                .ProgressCallbackFactory<PutObjectRequest>().get());

    }

    public void display(ImageView imageView, OSSFileParams params) {
        //1.先从内存获取
        //把url地址加密,来去掉一些特殊字符作为文件名
        String fileName = params.getFileName();
        Bitmap bitmap = lruCache.get(fileName);
        if (bitmap == null) {
            LogUtil.e(TAG, "内存缓存为空");
        } else {
            imageView.setImageBitmap(bitmap);
            LogUtil.e(TAG, "从内存缓存获取图片成功,fileName=" + fileName);
            return;
        }
        //2.如果内存没有,再从本地获取
        bitmap = getBitmapFromLocal(fileName);
        if (bitmap == null) {
            LogUtil.e(TAG, "本地文件缓存为空");
        } else {
            imageView.setImageBitmap(bitmap);
            lruCache.put(fileName, bitmap);//缓存到内存
            LogUtil.e(TAG, "从本地缓存获取图片成功");
            return;
        }
        //3.如果本地没有,再从网络获取
        getBitmapFromServer(imageView, params);
    }


    /**
     * 从OSS服务器获取图片
     *
     * @param imageView
     * @param params
     */
    public void getBitmapFromServer(ImageView imageView, OSSFileParams params) {
        final String fileName = params.getFileName();
        LogUtil.e(TAG, "从网络获取图片,fileName=" + fileName);
        String objName = params.getObjectkey();
        final ImageDisplayer displayer = new ImageDisplayer(imageView);
        ossUtil.setDownloadOssCallBackHandler(new OSSUtil.OssDownloadCallBackHandler() {
            @Override
            public void onSuccess(final GetObjectRequest request, final GetObjectResult result,
                                  final ProgressInputStream progressStream) {
                //读取流的操作也是在访问网络,所以也要在子线程
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            //需要根据对应的View大小来自适应缩放
                            final Bitmap bm = displayer.autoResizeFromStream(progressStream);
                            String object = request.getObjectKey();
                            String requestid = result.getRequestId();
                            LogUtil.e(TAG, "fileName=" + fileName + ", bm=" + bm);
                            //缓存到内存和本地文件
                            if (TextUtils.isEmpty(fileName) || bm == null) return;
                            lruCache.put(fileName, bm);
                            writeBitmapToLocal(bm, fileName);
                            LogUtil.e(TAG, "下载成功,显示图片");
                            mContext.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //在UI线程修改UI
                                    displayer.setImageBitmap(bm);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion,
                                  ServiceException serviceException) {
                String info = "";
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    info = clientExcepion.toString();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    info = serviceException.toString();
                }
                final String outputinfo = new String(info);
                LogUtil.e("下载失败", outputinfo);
            }
        });
        if (authNativeToken == null) {
            LogUtil.e(TAG, "缓存被清除,重新登陆");
            ToastUtil.showUIThread(mContext, "缓存被清除,请重新登陆");
            mContext.startActivity(new Intent(mContext, RegisterActivity.class));
            return;
        }
        ossService = ossUtil.createReadOssService(mContext, Constants.ENDPOINT, Constants
                        .WRITE_ACCESS_AT_OSS,
                authNativeToken.getAuthToken().getAccess_token());
        String bucket = PropertiesUtil.getProperties(mContext, PropertiesUtil.OSS_BUCKET);
        if (TextUtils.isEmpty(bucket)) return;
        ossService.setBucket(bucket);
        ossService.asyncGetImage(objName, ossUtil.getGetCallback());
    }

    /**
     * 把图片缓存在本地
     *
     * @param bm
     * @param fileName
     */
    public String writeBitmapToLocal(Bitmap bm, String fileName) {
        try {
            if (fileName.contains("/")) {
                String relativeDir = fileName.substring(0, fileName.lastIndexOf("/"));
                File file = new File(cacheDir, relativeDir);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
            FileOutputStream fos = new FileOutputStream(new File(cacheDir, fileName));

            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            LogUtil.e(TAG, "缓存图片到本地成功, key=" + fileName);
            return fileName;
        } catch (Exception e) {
            LogUtil.e(TAG, "缓存图片到本地失败");
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据url, 从本地取图片
     *
     * @param fileName
     * @return
     */
    private Bitmap getBitmapFromLocal(String fileName) {
        try {
            LogUtil.e(TAG, fileName);
            File file = new File(cacheDir, fileName);
            LogUtil.e(TAG, "从本地获取缓存图片,key=" + fileName);
            if (file.exists()) {
                return BitmapFactory.decodeFile(file.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeToMem(String fileName, Bitmap bm) {
        Bitmap remove = lruCache.remove(fileName);
        LogUtil.e(TAG, "add to mem,remove=" + remove);
        LogUtil.e(TAG, "add to mem,userIcon=" + lruCache.get(fileName));
        Bitmap put = lruCache.put(fileName, bm);
        LogUtil.e(TAG, "add to mem,put=" + put);
    }

    public void clear() {
        lruCache.evictAll();
    }

    private ImageHelperCallbackHandler imageHelperCallbackHandler;

    public void setCallbackHandler(ImageHelperCallbackHandler imageHelperCallbackHandler) {
        this.imageHelperCallbackHandler = imageHelperCallbackHandler;
    }

}
