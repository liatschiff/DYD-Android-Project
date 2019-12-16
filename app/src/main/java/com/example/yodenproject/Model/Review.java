package com.example.yodenproject.Model;

import java.io.Serializable;

public class Review implements Serializable {

    private String review;
    private String nameClient;
    private String clientImage;

    public Review() {
    }

    public Review(String review, String nameClient, String clientImage) {
        this.review = review;
        this.nameClient = nameClient;
        this.clientImage = clientImage;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getNameClient() {
        return nameClient;
    }

    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

    public String getClientImage() {
        return clientImage;
    }

    public void setClientImage(String clientImage) {
        this.clientImage = clientImage;
    }
}
