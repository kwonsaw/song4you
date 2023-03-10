package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetReplyDataResultModel implements Serializable {

    private String replyid;
    private String userid;
    private String nickname;
    private String profileimgurl;
    private String musicid;
    private String deleteyn;
    private String description;
    private String inserteddatetime;
    private String myreply;

    public String getReplyid() { return replyid; }
    public String getUserid() { return userid; }
    public String getNickname() { return nickname; }
    public String getProfileimgurl() { return profileimgurl; }
    public String getMusicid() { return musicid; }
    public String getDeleteyn() {
        return deleteyn;
    }
    public String getDescription() {
        return description;
    }
    public String getInserteddatetime() {
        return inserteddatetime;
    }
    public String getMyreply() {
        return myreply;
    }

}
