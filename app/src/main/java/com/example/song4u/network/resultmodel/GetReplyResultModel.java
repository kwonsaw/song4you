package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class GetReplyResultModel implements Serializable {

    private String message;
    private String code;
    private int totalPage;
    private List<GetReplyDataResultModel> replylist;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public int getTotalpage() {
        return totalPage;
    }
    public List<GetReplyDataResultModel> getReplylist() {
        return replylist;
    }


}
