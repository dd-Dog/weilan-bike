package com.wlcxbj.bike.db;

/**
 * Created by Administrator on 2017/2/6.
 */
public class AuthDbConstants {
    public static final String AUTH_DB_NAME = "auth_db";
    public static final int AUTH_DB_VERSION = 1;
    public static final String CREATE_TABLE = "CREATE TABLE " +
            "auth_token " +
            "(_id integer PRIMARY KEY autoincrement, " +
            "access_token varchar(65535) UNIQUE, " +
            "cuid varchar(100), " +
            "expires_in long, " +
            "inviteCode varchar(100), " +
            "jti varchar(100), " +
            "moblie varchar(20), " +
            "refresh_token varchar(65535), " +
            "SCOPE varchar(50), " +
            "token_type varchar(50), " +
            "userId varchar(50));";
}
