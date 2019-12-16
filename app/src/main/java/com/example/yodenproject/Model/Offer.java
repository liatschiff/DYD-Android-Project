package com.example.yodenproject.Model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Offer implements Serializable {

    private String idProfessional;
    private String idOrder;
    private int relevantPrice;
    private String moreInformation;
    private String imageUrl;
    private String fullNamePro;

    public Offer() {
    }

    public Offer(String idProfessional, String idOrder, int relevantPrice, String moreInformation, String imageUrl, String fullNamePro) {
        this.idProfessional = idProfessional;
        this.idOrder = idOrder;
        this.relevantPrice = relevantPrice;
        this.moreInformation = moreInformation;
        this.imageUrl = imageUrl;
        this.fullNamePro = fullNamePro;
    }

    public String getIdProfessional() {
        return idProfessional;
    }

    public void setIdProfessional(String idProfessional) {
        this.idProfessional = idProfessional;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public int getRelevantPrice() {
        return relevantPrice;
    }

    public void setRelevantPrice(int relevantPrice) {
        this.relevantPrice = relevantPrice;
    }

    public String getMoreInformation() {
        return moreInformation;
    }

    public void setMoreInformation(String moreInformation) {
        this.moreInformation = moreInformation;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullNamePro() {
        return fullNamePro;
    }

    public void setFullNamePro(String fullNamePro) {
        this.fullNamePro = fullNamePro;
    }
}
