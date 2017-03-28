package com.wlcxbj.bike.bean.account;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/6.
 */
public class AuthToken implements Serializable{
    /*
    {
    "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjdWlkIjoiTk8wMDAwMDEiLCJ1c2VyX25hbWUiOiIxNTAzMzI2MjY2NCIsInNjb3BlIjpbImFwcGNsaWVudCJdLCJpbnZpdGVDb2RlIjoiMTQ4NjE5MjYwOTk1MjI2NjQzNzM2IiwibW9iaWxlIjoiMTUwMzMyNjI2NjQiLCJleHAiOjE0ODYzODA0MDgsInVzZXJJZCI6MywiaW52aXRlTm90ZSI6bnVsbCwianRpIjoiZmZlMDI1YzktMjEyYy00NmMxLWE1NmUtOThkMWIyYTE4ODEwIiwiY2xpZW50X2lkIjoiYXBwQ2xpZW50In0.djsJMjc0TwD6JsyNYLf97OCaKM2tKa2FWYkOGnG-2J0lGG7cQGP4xpAgJITG0Gy3KZxH5alXj8FWmL80DBbQU7rNhToWxlNiioZeKf6w2bgTIKCiC-EbiPlHcBMAiGWnWNAVgUrVeLc1v2B5KzC7bK87QK1T6blo2v1vsAVNNo8Bx4mgm33HQ_wcFNdln8fAs1CeSM0vgWHtGWwk2CbByhq0Mi6BwSJIBGyz-0ECkvLrnIKl0l1SgoukFt5htti6y1xgvXVtqLEnSWyaF6YLaUcYnc1a-b7x6I1j5i5LnriNeXTQ8eTHSsoIuyx_UXJFSrnQHZIqPJAR3W-m5-S9ZQ",
    "cuid": "NO000001",
    "expires_in": 35999,
    "inviteCode": "148619260995226643736",
    "jti": "ffe025c9-212c-46c1-a56e-98d1b2a18810",
    "mobile": "15033262664",
    "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjdWlkIjoiTk8wMDAwMDEiLCJ1c2VyX25hbWUiOiIxNTAzMzI2MjY2NCIsInNjb3BlIjpbImFwcGNsaWVudCJdLCJpbnZpdGVDb2RlIjoiMTQ4NjE5MjYwOTk1MjI2NjQzNzM2IiwiYXRpIjoiZmZlMDI1YzktMjEyYy00NmMxLWE1NmUtOThkMWIyYTE4ODEwIiwibW9iaWxlIjoiMTUwMzMyNjI2NjQiLCJleHAiOjE0ODYzODA0MDgsInVzZXJJZCI6MywiaW52aXRlTm90ZSI6bnVsbCwianRpIjoiYmNjY2Y5NTItYTI3Mi00NmUwLTliMTktYzFkM2U4MGJlODdjIiwiY2xpZW50X2lkIjoiYXBwQ2xpZW50In0.PgXYZtk5PTbKOvFPETfJtVqhkbP5uWRbcmH8PcyIWwc0r5HnTMD27xIuBfQvvhT1VgLjffMUAuv9RDL70gEo5A56tSYMy2KSoUHA2pk9XgFsT7tQHrWhVAP4Q9bDr4GUkAg3bN-N5lXBuL7oTxOZeBITTXfnw31ZJv7ImSIxu770zsJ0KpY3siHYEMh3ZtB92oGxAMr_Lif-fnB6RPFjrEj-dZZAj8R5wPh-NUIpjwEYOtZx-MvpP_RiZqnww7wLBgHP9D-ZwN9XtLJ0H2bY9SESMPcsjDsh0Gx-hH64VyVewdLVYoQ8U2xJsjaCZAGdeB-qVU5eKEMg76MF3N_ljQ",
    "scope": "appclient",
    "token_type": "bearer",
    "userId": 3
}
     */
    private String access_token;//登陆凭证,用来访问用户私有信息
    private String cuid;
    private long expires_in;//登陆有效期
    private String inviteCode;//邀请码
    private String jti;
    private String mobile;//登陆用户名
    private String refresh_token;//刷新token
    private String scope;
    private String token_type;//token类型

    @Override
    public String toString() {
        return "AuthToken{" +
                "access_token='" + access_token + '\'' +
                ", cuid='" + cuid + '\'' +
                ", expires_in=" + expires_in +
                ", inviteCode='" + inviteCode + '\'' +
                ", jti='" + jti + '\'' +
                ", mobile='" + mobile + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", scope='" + scope + '\'' +
                ", token_type='" + token_type + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;//用户id号



}
