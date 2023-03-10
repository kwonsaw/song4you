package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetCouponDataResultModel implements Serializable {

    private String price;
    private String gifticonImageUrl;
    private String insertedDatetime;
    private String epin;
    private String exdate;
    private String productname;
    private String brandname;

    public String getPrice() { return price; }
    public String getGifticonImageUrl() { return gifticonImageUrl; }
    public String getInsertedDatetime() { return insertedDatetime; }
    public String getEpin() {
        return epin;
    }
    public String getExdate() {
        return exdate;
    }
    public String getProductname() {
        return productname;
    }
    public String getBrandname() {
        return brandname;
    }

}
