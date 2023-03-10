package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class GetNoticeResultModel implements Serializable {

    private String message;
    private String code;
    private String totalPage;
    private List<GetNoticeDataResultModel> noticelist;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public String getTotalPage() {
        return totalPage;
    }
    public List<GetNoticeDataResultModel> getNoticelist() {
        return noticelist;
    }


}