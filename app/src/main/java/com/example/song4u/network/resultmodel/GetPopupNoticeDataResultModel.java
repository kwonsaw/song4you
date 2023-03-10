package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetPopupNoticeDataResultModel implements Serializable {

    private String noticetitle;
    private String noticeimgurl;
    private String notitype;
    private String minver;
    private String maxver;
    private String noticedescription;

    public String getNoticeTitle() { return noticetitle; }
    public String getNoticeImgUrl() { return noticeimgurl; }
    public String getNotiType() { return notitype; }
    public String getMinVer() {
        return minver;
    }
    public String getMaxVer() {
        return maxver;
    }
    public String getNoticedescription() {
        return noticedescription;
    }

}
