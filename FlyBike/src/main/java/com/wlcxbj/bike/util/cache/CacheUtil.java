package com.wlcxbj.bike.util.cache;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.wlcxbj.bike.util.LogUtil;

/**
 * Created by Administrator on 2017/2/6.
 */
public class CacheUtil {

    public static<O> boolean cacheSerialToken(Context context, String fileName, O object) {
        //序列化到本地
        String dir = context.getCacheDir().getPath() + File.separator + fileName;
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(dir));
            oos.writeObject(object);
            oos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static<T> T getSerialToken(Context context, String fileName) {
        String dir = context.getCacheDir().getPath() + File.separator + fileName;
        File file = new File(dir);
        if (!file.exists()) {
            LogUtil.e("CacheUtil", "没有缓存");
            return null;
        }
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(dir));
            T t = (T) ois.readObject();
            LogUtil.e("CacheUtil", "反序列化:" + t);
            ois.close();
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static boolean clearCache(Context context, String fileName) {
        String dir = context.getCacheDir().getPath() + File.separator + fileName;
        File file = new File(dir);
        if (file.exists()) {
            LogUtil.e("CacheUtil", "清除文件缓存:" + fileName);
            return file.delete();
        }
        return false;
    }

    public static boolean isCacheExists(Context context, String fileName) {
        String dir = context.getCacheDir().getPath() + File.separator + fileName;
        File file = new File(dir);
        return file.exists();
    }


}
