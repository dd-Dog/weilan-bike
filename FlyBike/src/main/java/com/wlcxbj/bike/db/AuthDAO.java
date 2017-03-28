package com.wlcxbj.bike.db;

import android.content.Context;

/**
 * Created by Administrator on 2017/2/6.
 */
public class AuthDAO {

    public AuthDAO(Context context) {
        Context mContext = context;
        AuthDbHelper authDbHelper = new AuthDbHelper(context);
    }

}
