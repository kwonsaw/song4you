package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class GetGenieParserResultModel implements Serializable {

    private String message;
    private String code;
    private List<GetGenieParserDataResultModel> list;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public List<GetGenieParserDataResultModel> getList() {
        return list;
    }

}