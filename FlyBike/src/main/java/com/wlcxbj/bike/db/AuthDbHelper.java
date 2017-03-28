package com.wlcxbj.bike.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/2/6.
 */
public class AuthDbHelper extends SQLiteOpenHelper {

    public AuthDbHelper(Context context) {
        super(context, AuthDbConstants.AUTH_DB_NAME, null, AuthDbConstants.AUTH_DB_VERSION);
    }

    /**
     * 第一次创建数据库时执行
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(AuthDbConstants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
