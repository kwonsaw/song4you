package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class GetUserInfoResultModel implements Serializable {

    private String message;
    private String code;
    private int point;
    private List<GetUserInfoDataResultModel> userinfo;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public int getPoint() {
        return point;
    }
    public List<GetUserInfoDataResultModel> getUserinfo() {
        return userinfo;
    }

}
