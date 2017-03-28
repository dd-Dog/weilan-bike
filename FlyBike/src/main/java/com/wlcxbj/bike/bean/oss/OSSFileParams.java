package com.wlcxbj.bike.bean.oss;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Administrator on 2017/2/12.
 */
public class OSSFileParams implements Serializable{
    private String endpoint;
    private String bucket;
    private String relativePath;
    private String fileName;
    private String objectkey;

    public OSSFileParams(String endpoint, String bucket, String relativePath, String fileName) {
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.objectkey = relativePath + "/" + fileName;
        this.relativePath = relativePath;
        this.fileName = fileName;
    }

    public OSSFileParams(String endpoint, String bucket, String objectkey) {
        this.endpoint = endpoint;
        this.bucket = bucket;
        this.objectkey = objectkey;
        if (objectkey.contains("/")) {
            this.relativePath = objectkey.substring(0, objectkey.lastIndexOf("/"));
        }else {
            relativePath = "";
        }
        this.fileName = objectkey.substring(objectkey.lastIndexOf("/") + 1);
    }

    public String getObjectkey() {
        return objectkey;
    }

    @Override
    public String toString() {
        return "OSSFileParams{" +
                "endpoint='" + endpoint + '\'' +
                ", bucket='" + bucket + '\'' +
                ", relativePath='" + relativePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", objectkey='" + objectkey + '\'' +
                '}';
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
