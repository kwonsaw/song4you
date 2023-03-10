package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class SetGifticonResultModel implements Serializable {

    private String message;
    private String code;
    private String gifticonImageUrl;
    private String epin;
    private String exdate;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public String getGifticonImageUrl() {
        return gifticonImageUrl;
    }
    public String getEpin() {
        return epin;
    }
    public String getExdate() {
        return exdate;
    }


}