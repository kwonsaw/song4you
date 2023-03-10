package com.example.song4u.network.resultmodel;

import java.io.Serializable;
import java.util.List;

public class GetMusicInfoResultModel implements Serializable {

    private String message;
    private String code;
    private String savemoney;
    private String nickname;
    private String profileimgurl;
    private String likeyn;

    private List<GetMusicInfoDataResultModel> musicinfo;
    private List<GetUserInfoDataResultModel> playerlist;
    private List<GetUserInfoDataResultModel> ranklist;

    public String getResult() {
        return message;
    }
    public String getResultCode() {
        return code;
    }
    public String getSavemoney() {
        return savemoney;
    }
    public String getNickname() {
        return nickname;
    }
    public String getProfileimgurl() {
        return profileimgurl;
    }
    public String getLikeyn() {
        return likeyn;
    }

    public List<GetMusicInfoDataResultModel> getMusicinfo() {
        return musicinfo;
    }
    public List<GetUserInfoDataResultModel> getPlayerlist() {
        return playerlist;
    }
    public List<GetUserInfoDataResultModel> getRanklist() {
        return ranklist;
    }


}