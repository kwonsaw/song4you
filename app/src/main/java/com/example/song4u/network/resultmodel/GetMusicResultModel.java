package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class GetMusicResultModel implements Serializable {

    private String message;
    private String code;
    private List<GetMusicDataResultModel> music;
    private List<GetMusicListDataResultModel> musiclist;
    private List<GetMusicListDataResultModel> ranklist;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public List<GetMusicDataResultModel> getMusic() {
        return music;
    }
    public List<GetMusicListDataResultModel> getMusiclist() {
        return musiclist;
    }
    public List<GetMusicListDataResultModel> getRanklist() {
        return ranklist;
    }


}