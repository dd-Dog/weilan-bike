package com.wlcxbj.bike.bean.account;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/13.
 */
public class RealInfo implements Serializable {
    /*
     *   "realInfo": {
"enduserId": 3,
"realName": "张三",
"mobile": "15033262664",
"idno": "130682199104264535",
"idcardFrontUrl": null,
"idcardBackUrl": null,
"verifySpid": 1,认证状态，取值范围参考系统参数 groupId=COMMON, codeType= USER_VERIFY_STATUS
"auditorId": null,
"approvedTime": null,
"submitTime": null
},
     */
    private String enduserId;
    private String realName;
    private String mobile;
    private String idno;
    private String idcardFrontUrl;
    private String idcardBackUrl;
    private int verifySpid;
    private String auditorId;
    private long approvedTime;
    private long submitTime;

    @Override
    public String toString() {
        return "RealInfo{" +
                "enduserId='" + enduserId + '\'' +
                ", realName='" + realName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", idno='" + idno + '\'' +
                ", idcardFrontUrl='" + idcardFrontUrl + '\'' +
                ", idcardBackUrl='" + idcardBackUrl + '\'' +
                ", verifySpid=" + verifySpid +
                ", auditorId='" + auditorId + '\'' +
                ", approvedTime=" + approvedTime +
                ", submitTime=" + submitTime +
                '}';
    }

    public String getIdcardFrontUrl() {
        return idcardFrontUrl;
    }

    public String getIdcardBackUrl() {
        return idcardBackUrl;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public long getApprovedTime() {
        return approvedTime;
    }

    public long getSubmitTime() {
        return submitTime;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getVerifySpid() {
        return verifySpid;
    }

    public void setVerifySpid(int verifySpid) {
        this.verifySpid = verifySpid;
    }


    public String getEnduserId() {
        return enduserId;
    }

    public void setEnduserId(String enduserId) {
        this.enduserId = enduserId;
    }

}
