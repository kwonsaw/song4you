package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetNoticeDataResultModel implements Serializable {

    private String noticeid;
    private String noticetitle;
    private String noticedescription;
    private String inserteddatetime;
    private String notitype;


    public String getNoticeId() { return noticeid; }
    public String getNoticeTitle() { return noticetitle; }
    public String getNoticeDescription() { return noticedescription; }
    public String getInsertedDatetime() {
        return inserteddatetime;
    }
    public String getType() {
        return notitype;
    }


}
