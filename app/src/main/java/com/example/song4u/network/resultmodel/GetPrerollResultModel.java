package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class GetPrerollResultModel implements Serializable {

    private String message;
    private String code;
    private List<GetPrerollDataResultModel> CPVCList;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public List<GetPrerollDataResultModel> getPrerollList() {
        return CPVCList;
    }


}
