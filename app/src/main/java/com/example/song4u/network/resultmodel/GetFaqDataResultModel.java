package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetFaqDataResultModel implements Serializable {

    private String faqid;
    private String faqtitle;
    private String faqdescription;
    private String inserteddatetime;


    public String getFaqId() { return faqid; }
    public String getFaqTitle() { return faqtitle; }
    public String getFaqDescription() { return faqdescription; }
    public String getInsertedDatetime() {
        return inserteddatetime;
    }


}
