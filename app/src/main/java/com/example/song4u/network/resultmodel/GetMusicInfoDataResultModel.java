package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetMusicInfoDataResultModel implements Serializable {

    private String musicid;
    private String userid;
    private String title;
    private String singer;
    private String mainimgurl ;
    private String type ;
    private String songid;
    private int play_cnt ;
    private int totalplay_cnt ;
    private int reply_cnt ;
    private int like_cnt ;
    private String point ;
    private String inserteddatetime ;

    public String getMusicId() { return musicid; }
    public String getUserid() { return userid; }
    public String getTitle() { return title; }
    public String getSinger() { return singer; }
    public String getMainimgurl() {
        return mainimgurl ;
    }
    public String getType() {
        return type ;
    }
    public String getSongid() {
        return songid;
    }
    public int getPlay_cnt() {
        return play_cnt ;
    }
    public int getTotalplay_cnt() {
        return totalplay_cnt ;
    }
    public int getReply_cnt() {
        return reply_cnt ;
    }
    public int getLike_cnt() {
        return like_cnt ;
    }
    public String getPoint() {
        return point ;
    }
    public String getInserteddatetime() {
        return inserteddatetime ;
    }

}