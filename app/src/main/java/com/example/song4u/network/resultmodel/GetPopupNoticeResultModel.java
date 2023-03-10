package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class GetPopupNoticeResultModel implements Serializable {

    private String message;
    private String code;
    private List<GetPopupNoticeDataResultModel> popupnoticelist;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public List<GetPopupNoticeDataResultModel> getPopupNoticeList() {
        return popupnoticelist;
    }

}
