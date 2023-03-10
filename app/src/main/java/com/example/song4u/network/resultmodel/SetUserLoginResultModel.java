package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class SetUserLoginResultModel implements Serializable {

    private String message;
    private String code;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }


}
