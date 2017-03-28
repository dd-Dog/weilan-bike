package com.wlcxbj.bike.bean.oss;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/20.
 */
public class StsCredentialsModelToken implements Serializable {
    /**
     * {"errcode":0,"errmsg":"OK","stsCredentialsModel":
     * {"expiration":"2017-02-20T10:51:35Z",
     * "securityToken":"CAIS/AF1q6Ft5B2yfSjIq5OGHYjnoetZ4puOUU//skwFXdlVpPKTkTz2IHxMfXJqBOAYt/wzm25V5/YclqB+TZNYXkHeKJAoaDKoQM7iMeT7oMWQweEuqv/MQBq+aXPS2MvVfJ+KLrf0ceusbFbpjzJ6xaCAGxypQ12iN+/i6/clFKN1ODO1dj1bHtxbCxJ/ocsBTxvrOO2qLwThjxi7biMqmHIl1zgiufjik5HEsEeP3QGr8IJP+dSteKrDRtJ3IZJyX+2y2OFLbafb2EZSkUMWrP4t1vEUp26f4o7DXggBvQ/6KePR+dpqKQh/b7AiAaNNoP/6kexoP1wPfl1mUHwagAFKMPvHgSWaCluDO9U1baqVvNm/q2fF59H6kIUK2GrSaHuAxioDBEsWmfYu77WF299dJtAYDCWSOuZrKujeokM+3NQFWFLfVmRTWxfSO9IxH2B4ajyLOpxeMqp0uMbN1rTZE+qrwwzIb+M/aCHBe4GvO5fL5SToaHxBN8YNapnP+A==",
     * "accessKeyId":"STS.JP3V2SL4xUYeSiNVLPQVyK78s",
     * "accessKeySecret":"F7XY11B6Ax2W5y1c1osHyQgD4GoBZho7spwMgsV4QrYY",
     * "roleSessionName":"enduser-3"}}
     */
    private int errcode;
    private String errmsg;
    private StsCredentialsModelBean stsCredentialsModel;

    @Override
    public String toString() {
        return "StsCredentialsModelToken{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", stsCredentialsModel=" + stsCredentialsModel +
                '}';
    }

    public int getErrcode() {
        return errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public StsCredentialsModelBean getStsCredentialsModel() {
        return stsCredentialsModel;
    }
}
