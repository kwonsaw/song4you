package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class GetAddMusicCheckResultModel implements Serializable {

    private String message;
    private String code;
    private List<GetMusicInfoDataResultModel> musicinfo;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public List<GetMusicInfoDataResultModel> getMusicinfo() {
        return musicinfo;
    }

}
