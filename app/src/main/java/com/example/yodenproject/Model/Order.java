package com.example.yodenproject.Model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {


    private String idClientUser;
    private String idProfessionalUser;

    private String orderName;
    private String photo;
    private String golves;
    private String sleeves;
    private List<String> fabrics = new ArrayList<>();
    private String cleavageShape;
    private String backShape;
    private String lengthDress;
    private int hips;
    private int bust;
    private int waist;
    private int distHips;
    private int distBust;
    private int distWaist;
    private int yourHeight;
    private int maxPrice;
    private String anotherDescription;
    private String finalDate;

    public Order() {
    }


    public Order(String idClientUser, String idProfessionalUser, String orderName, String photo, String golves, String sleeves, List<String> fabrics, String cleavageShape, String backShape, String lengthDress, int hips, int bust, int waist, int distHips, int distBust, int distWaist, int yourHeight, int maxPrice, String anotherDescription, String finalDate) {
        this.idClientUser = idClientUser;
        this.idProfessionalUser = idProfessionalUser;
        this.orderName = orderName;
        this.photo = photo;
        this.golves = golves;
        this.sleeves = sleeves;
        this.fabrics = fabrics;
        this.cleavageShape = cleavageShape;
        this.backShape = backShape;
        this.lengthDress = lengthDress;
        this.hips = hips;
        this.bust = bust;
        this.waist = waist;
        this.distHips = distHips;
        this.distBust = distBust;
        this.distWaist = distWaist;
        this.yourHeight = yourHeight;
        this.maxPrice = maxPrice;
        this.anotherDescription = anotherDescription;
        this.finalDate = finalDate;
    }

    public String getIdClientUser() {
        return idClientUser;
    }

    public void setIdClientUser(String idClientUser) {
        this.idClientUser = idClientUser;
    }

    public String getIdProfessionalUser() {
        return idProfessionalUser;
    }

    public void setIdProfessionalUser(String idProfessionalUser) {
        this.idProfessionalUser = idProfessionalUser;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getGolves() {
        return golves;
    }

    public void setGolves(String golves) {
        this.golves = golves;
    }

    public String getSleeves() {
        return sleeves;
    }

    public void setSleeves(String sleeves) {
        this.sleeves = sleeves;
    }

    public List<String> getFabrics() {
        return fabrics;
    }

    public void setFabrics(List<String> fabrics) {
        this.fabrics = fabrics;
    }

    public String getCleavageShape() {
        return cleavageShape;
    }

    public void setCleavageShape(String cleavageShape) {
        this.cleavageShape = cleavageShape;
    }

    public String getBackShape() {
        return backShape;
    }

    public void setBackShape(String backShape) {
        this.backShape = backShape;
    }

    public String getLengthDress() {
        return lengthDress;
    }

    public void setLengthDress(String lengthDress) {
        this.lengthDress = lengthDress;
    }

    public int getHips() {
        return hips;
    }

    public void setHips(int hips) {
        this.hips = hips;
    }

    public int getBust() {
        return bust;
    }

    public void setBust(int bust) {
        this.bust = bust;
    }

    public int getWaist() {
        return waist;
    }

    public void setWaist(int waist) {
        this.waist = waist;
    }

    public int getDistHips() {
        return distHips;
    }

    public void setDistHips(int distHips) {
        this.distHips = distHips;
    }

    public int getDistBust() {
        return distBust;
    }

    public void setDistBust(int distBust) {
        this.distBust = distBust;
    }

    public int getDistWaist() {
        return distWaist;
    }

    public void setDistWaist(int distWaist) {
        this.distWaist = distWaist;
    }

    public int getYourHeight() {
        return yourHeight;
    }

    public void setYourHeight(int yourHeight) {
        this.yourHeight = yourHeight;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getAnotherDescription() {
        return anotherDescription;
    }

    public void setAnotherDescription(String anotherDescription) {
        this.anotherDescription = anotherDescription;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }
}
