package com.wlcxbj.bike.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by Administrator on 2017/2/12.
 */
public class URIUtil {

    /**
     * 从URI获取文件路径
     *
     * @param originalUri
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri originalUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String path = null;
        Cursor cursor = context.getContentResolver().query(originalUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
}
