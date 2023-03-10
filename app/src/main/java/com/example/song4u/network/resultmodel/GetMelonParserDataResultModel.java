package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetMelonParserDataResultModel implements Serializable {

    private String songid;
    private String title;
    private String singer;
    private String image;

    public String getSongid() { return songid; }
    public String getTitle() { return title; }
    public String getSinger() { return singer; }
    public String getImage() {
        return image;
    }

}
