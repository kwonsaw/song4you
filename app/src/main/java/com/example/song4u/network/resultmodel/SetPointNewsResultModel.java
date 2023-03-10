package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class SetPointNewsResultModel implements Serializable {

    private String message;
    private String code;
    private String savemoney;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public String getSavemoney() {
        return savemoney;
    }

}
