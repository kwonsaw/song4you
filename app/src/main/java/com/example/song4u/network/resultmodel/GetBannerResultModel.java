package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class GetBannerResultModel implements Serializable {

    private String message;
    private String code;
    private List<GetBannerDataResultModel> bannerlist;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public List<GetBannerDataResultModel> getBannerlist() {
        return bannerlist;
    }


}
