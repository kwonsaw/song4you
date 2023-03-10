package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetAppVersionResultModel implements Serializable  {

    private String message;
    private String code;
    private String marketurl;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public String getMarketUrl() {
        return marketurl;
    }
}
