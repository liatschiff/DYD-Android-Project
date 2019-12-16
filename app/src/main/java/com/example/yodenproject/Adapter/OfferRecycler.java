package com.example.yodenproject.Adapter;

import java.io.Serializable;

public class OfferRecycler implements Serializable {

    private String idOrder;
    private String idProfessional;
    private String nameProfessional;
    private int relevantPrice;
    private String moreInfo;
    private String photoPro;

    public OfferRecycler(String idOrder, String idProfessional, String nameProfessional, int relevantPrice, String moreInfo, String photoPro) {
        this.idOrder = idOrder;
        this.idProfessional = idProfessional;
        this.nameProfessional = nameProfessional;
        this.relevantPrice = relevantPrice;
        this.moreInfo = moreInfo;
        this.photoPro = photoPro;
    }

    public OfferRecycler() {
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdProfessional() {
        return idProfessional;
    }

    public void setIdProfessional(String idProfessional) {
        this.idProfessional = idProfessional;
    }

    public String getNameProfessional() {
        return nameProfessional;
    }

    public void setNameProfessional(String nameProfessional) {
        this.nameProfessional = nameProfessional;
    }

    public int getRelevantPrice() {
        return relevantPrice;
    }

    public void setRelevantPrice(int relevantPrice) {
        this.relevantPrice = relevantPrice;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getPhotoPro() {
        return photoPro;
    }

    public void setPhotoPro(String photoPro) {
        this.photoPro = photoPro;
    }
}

