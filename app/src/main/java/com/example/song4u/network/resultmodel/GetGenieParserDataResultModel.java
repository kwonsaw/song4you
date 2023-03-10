package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetGenieParserDataResultModel implements Serializable {

    private String songid;
    private String title;
    private String singer;
    private String image;
    private String playtime;

    public String getSongid() { return songid; }
    public String getTitle() { return title; }
    public String getSinger() { return singer; }
    public String getImage() {
        return image;
    }
    public String getPlaytime() {
        return playtime;
    }

}