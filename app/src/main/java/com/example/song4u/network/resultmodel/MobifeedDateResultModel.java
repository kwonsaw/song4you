package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class MobifeedDateResultModel implements Serializable {

    /* 기존 포인트뉴스 API */
    private String title;
    private String link;
    private String clickurl;
    private String uid;
    private String isback;
    private String backurl;
    private String isscroll;
    private String savemoney;
    private String sourceId;
    private String thumbnail;
    private String description;
    private String saveyn;

    /* 모비피드 API */
    private String news_title;
    private String link_url;
    private String news_img_url;
    private String object_id;
    private String site_name;
    private String root_domain;
    private String pub_date;
    private String msaveyn = "y";


    /* 기존 포인트뉴스 API */
    public String getTitle() { return title; }
    public String getLink() { return link; }
    public String getClickurl() { return clickurl; }
    public String getUid() {
        return uid;
    }
    public String getIsback() {
        return isback;
    }
    public String getBackurl() {
        return backurl;
    }
    public String getIsscroll() {
        return isscroll;
    }
    public String getSavemoney() {
        return savemoney;
    }
    public String getSourceId() {
        return sourceId;
    }
    public String getImageUrl() { return thumbnail; }
    public String getDescription() {
        return description;
    }
    public String getViewingYn() {
        return saveyn;
    }


    /* 모비피드 API */
    public String getPub_date() {
        return pub_date;
    }
    public String getNews_title() { return news_title; }
    public String getLink_url() { return link_url; }
    public String getNews_img_url() {
        return news_img_url;
    }
    public String getObject_id() {
        return object_id;
    }
    public String getSite_name() {
        return site_name;
    }
    public String getRoot_domain() {
        return root_domain;
    }
    public String getViewingYnM() {
        return msaveyn;
    }
}