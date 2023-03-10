package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetGenieSearchDataResultModel implements Serializable {

    private String title;
    private String singer;
    private String songid ;

    public String getTitle() { return title; }
    public String getSinger() { return singer; }
    public String getSongid() {
        return songid ;
    }

}
