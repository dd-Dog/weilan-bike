package com.wlcxbj.bike.bean.account;

/**
 * Created by Administrator on 2017/2/13.
 */
public class IdentityToken {
    /**
     * {
     "errcode": 0,
     "errmsg": "OK",
     -"realInfo": {
     "enduserId": 3,
     "realName": "DDdog",
     "mobile": "15033262664",
     "idno": "130682199104264535",
     "idcardFrontUrl": null,
     "idcardBackUrl": null,
     "verifySpid": 1,
     "auditorId": null,
     "approvedTime": null,
     "submitTime": null
     }
     }
     */
    private int errcode;
    private String errmsg;
    private RealInfo realInfo;


    @Override
    public String toString() {
        return "IdentityToken{" +
                "errcode='" + errcode + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", realInfo=" + realInfo +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public RealInfo getRealInfo() {
        return realInfo;
    }
}
