package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class GetSupportMusicResultModel implements Serializable {

    private String message;
    private String code;
    private List<GetMusicListDataResultModel> musiclist;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public List<GetMusicListDataResultModel> getMusiclist() {
        return musiclist;
    }


}
