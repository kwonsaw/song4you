package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class GetCouponResultModel implements Serializable {

    private String message;
    private String code;
    private String totalPage;
    private List<GetCouponDataResultModel> list;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public String getTotalpage() {
        return totalPage;
    }
    public List<GetCouponDataResultModel> getList() {
        return list;
    }

}
