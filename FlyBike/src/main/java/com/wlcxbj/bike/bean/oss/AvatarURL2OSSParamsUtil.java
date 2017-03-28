package com.wlcxbj.bike.bean.oss;

import com.wlcxbj.bike.global.Constants;
import com.wlcxbj.bike.util.LogUtil;

/**
 * Created by Administrator on 2017/2/16.
 */
public class AvatarURL2OSSParamsUtil {
    private String avatarUrl;

    public AvatarURL2OSSParamsUtil(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public OSSFileParams getOSSParams() {
        //http://commonataayun.oss-cn-beijing.aliyuncs.com/userIcon
        String bucketName = avatarUrl.substring(7, avatarUrl.indexOf("."));
        String sub1 = avatarUrl.substring(avatarUrl.indexOf(".") + 1);//oss-cn-beijing.aliyuncs
        LogUtil.e("AvatarURL2OSSParamsUtil", "sub1=" + sub1);
        // .com/userIcon
        String endpoint = avatarUrl.substring(0, sub1.indexOf("/"));
        String sub2 = sub1.substring(sub1.indexOf("/") + 1);
        String objectKey = sub2;

        return new OSSFileParams(Constants.ENDPOINT, bucketName, objectKey);
    }
}
