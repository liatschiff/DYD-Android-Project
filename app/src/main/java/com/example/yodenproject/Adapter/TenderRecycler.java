package com.example.yodenproject.Adapter;

import android.graphics.Bitmap;

import java.io.Serializable;

public class TenderRecycler implements Serializable {

    private String idOrder;
    private String nameOrder;
    private String finalDate;
    private String photo;


    public TenderRecycler() {
    }

    public TenderRecycler(String idOrder, String nameOrder, String finalDate, String photo) {
        this.idOrder = idOrder;
        this.nameOrder = nameOrder;
        this.finalDate = finalDate;
        this.photo = photo;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(String nameOrder) {
        this.nameOrder = nameOrder;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

