package com.example.yodenproject.Model;

import java.io.Serializable;

public class User implements Serializable {

    private String imageUrl;
    private String fullname;
    private String phone;
    private String country;
    private String city;
    private String address;
    private String gender;
    private String userId;
    private String status;
    private int age;
    private boolean isClient;

    public User() {
    }

    public User(String phone, String imageUrl, String fullname, String country, String city, String address, String gender, String userId, String status, int age, boolean isClient) {
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.fullname = fullname;
        this.country = country;
        this.city = city;
        this.address = address;
        this.gender = gender;
        this.userId = userId;
        this.status = status;
        this.age = age;
        this.isClient = isClient;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean isClient) {
        this.isClient = isClient;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
