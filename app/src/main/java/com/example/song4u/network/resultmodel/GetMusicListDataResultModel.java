package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetMusicListDataResultModel  implements Serializable {

    private String musicid;
    private String title;
    private String singer;
    private String mainimgurl ;
    private String type ;
    private String songid;
    private int play_cnt ;
    private String totalplay_cnt ;
    private String reply_cnt ;
    private String like_cnt ;
    private String point ;
    private String inserteddatetime ;

    public String getMusicId() { return musicid; }
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
    public String getTotalplay_cnt() {
        return totalplay_cnt ;
    }
    public String getReply_cnt() {
        return reply_cnt ;
    }
    public String getLike_cnt() {
        return like_cnt ;
    }
    public String getPoint() {
        return point ;
    }
    public String getInserteddatetime() {
        return inserteddatetime ;
    }

}
