package com.wlcxbj.bike.bean;

/**
 * Created by bain on 17-2-7.
 */
public class ScanResultToken {

    public String bikeno;
    public String url;

    public ScanResultToken(String str) {
        if (str.contains("?")) {
            String codeStr = str.substring(str.indexOf("?") + 1);
            url = str.substring(0, str.indexOf("?"));
            if (codeStr.contains("=")) {
                bikeno = codeStr.substring(codeStr.indexOf("=") + 1);
            }
        }
    }
}
