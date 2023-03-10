package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetPointHistoryDataResultModel implements Serializable {

    private String pointhistoryid;
    private String savemoney;
    private String userid;
    private String typecode;
    private String inserteddatetime;
    private String contenttitle;

    public String getPointHistoryId() { return pointhistoryid; }
    public String getSaveMoney() { return savemoney; }
    public String getUserId() { return userid; }
    public String getTypecode() {
        return typecode;
    }
    public String getInsertedDatetime() {
        return inserteddatetime;
    }
    public String getContentTitle() {
        return contenttitle;
    }


}
