package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetBannerDataResultModel implements Serializable {

    private String uid;
    private String content;
    private String savemoney;
    private String sourceid;
    private String saveyn;
    private String viewingyn;

    public String getUid() { return uid; }
    public String getContent() { return content; }
    public String getSavemoney() { return savemoney; }
    public String getSourceId() {
        return sourceid;
    }
    public String getSaveyn() {
        return saveyn;
    }
    public String getViewingYn() {
        return viewingyn;
    }

}
