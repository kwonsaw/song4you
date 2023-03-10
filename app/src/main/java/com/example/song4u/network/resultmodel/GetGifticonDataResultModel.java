package com.example.song4u.network.resultmodel;

import java.io.Serializable;

public class GetGifticonDataResultModel implements Serializable {

    private String gifticonproductid;
    private String productcode;
    private String brandname;
    private String productname;
    private String price;
    private String gifticonproductinfourl;
    private String imageurl;
    private String exdate;

    public String getGifticonproductid() { return gifticonproductid; }
    public String getProductcode() { return productcode; }
    public String getBrandname() { return brandname; }
    public String getProductname() { return productname; }
    public String getPrice() {
        return price;
    }
    public String getGifticonproductinfourl() {
        return gifticonproductinfourl;
    }
    public String getImageurl() {
        return imageurl;
    }
    public String getExdate() {
        return exdate;
    }

}
