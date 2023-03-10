package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetPrerollDataResultModel implements Serializable {

    private String loadingtime;
    private String uid;
    private String subject;
    private String img;
    private String clickurl;
    private String savemoney;

    public String getLoadingtime() { return loadingtime; }
    public String getUid() { return uid; }
    public String getSubject() { return subject; }
    public String getImg() {
        return img;
    }
    public String getClickurl() {
        return clickurl;
    }
    public String getSavemoney() {
        return savemoney;
    }

}
