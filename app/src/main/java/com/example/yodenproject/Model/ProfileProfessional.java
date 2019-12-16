package com.example.yodenproject.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfileProfessional implements Serializable {

    private ArrayList<String> images;
    private ArrayList<Review> reviews;
    private String information;

    public ProfileProfessional() {
    }


    public ProfileProfessional(ArrayList<String> images, ArrayList<Review> reviews, String information) {
        this.images = images;
        this.reviews = reviews;
        this.information = information;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
