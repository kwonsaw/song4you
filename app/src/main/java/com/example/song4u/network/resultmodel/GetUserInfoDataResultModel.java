package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetUserInfoDataResultModel implements Serializable {

    private String userid;
    private String nickname;
    private String profileimgurl;
    private String reservesavemoney;
    private String logintype;
    private String os;
    private String adid;
    private String appver;
    private String osinfo;

    /* 후원 랭킹 조인 데이터 */
    private String removemoney;
    private String play_cnt;


    public String getUserId() { return userid; }
    public String getNickname() { return nickname; }
    public String getpImageUrl() { return profileimgurl; }
    public String getSavemoney() { return reservesavemoney; }
    public String getLogintype() {
        return logintype;
    }
    public String getOs() {
        return os;
    }
    public String getAdid() {
        return adid;
    }
    public String getAppver() {
        return appver;
    }
    public String getOsinfo() {
        return osinfo;
    }

    /* 후원 랭킹 조인 데이터 */
    public String getRemovemoney() {
        return removemoney;
    }
    public String getPlay_cnt() {
        return play_cnt;
    }

}
