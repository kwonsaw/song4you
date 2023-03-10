package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class MobifeedResultModel implements Serializable {
    private String result;
    private String resultCode;

    private List<MobifeedDateResultModel> newsList;


    public String getResult() {
        return result;
    }

    public String getResultCode() {
        return resultCode;
    }

    public List<MobifeedDateResultModel> getNewsList() {
        return newsList;
    }
}