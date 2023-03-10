package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class PointNewsResultModel implements Serializable {
    private String message;
    private String code;

    private List<MobifeedDateResultModel> list;


    public String getResult() {
        return message;
    }

    public String getResultCode() {
        return code;
    }

    public List<MobifeedDateResultModel> getNewsList() {
        return list;
    }
}