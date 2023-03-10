package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetMusicDataResultModel implements Serializable {

    private String musicid;
    private String title;
    private String singer;
    private String description;
    private String mainimgurl ;
    private Integer playtime ;
    private String melon_songid ;
    private String genie_songid ;
    private String point ;
    private String inserteddatetime ;

    public String getMusicId() { return musicid; }
    public String getTitle() { return title; }
    public String getSinger() { return singer; }
    public String getDescription() { return description; }
    public String getMainimgurl() {
        return mainimgurl ;
    }
    public Integer getPlaytime() {
        return playtime ;
    }
    public String getMelon_songid() {
        return melon_songid ;
    }
    public String getGenie_songid() {
        return genie_songid ;
    }
    public String getPoint() {
        return point ;
    }
    public String getInserteddatetime() {
        return inserteddatetime ;
    }

}
