package com.example.bloodbank.profile;

import com.example.bloodbank.LatLon;

import java.util.HashMap;

public class ProfileValueModel {
    String imageUrl, bloodGroup, name, email, occupation, comment, mobileNo, location, birthday;

    HashMap <String,Double> latLon;

    public ProfileValueModel() {
    }

    public ProfileValueModel(String imageUrl, String bloodGroup, String name, String email, String occupation, String comment, String mobileNo, String location, String birthday, HashMap<String, Double> latLon) {
        this.imageUrl = imageUrl;
        this.bloodGroup = bloodGroup;
        this.name = name;
        this.email = email;
        this.occupation = occupation;
        this.comment = comment;
        this.mobileNo = mobileNo;
        this.location = location;
        this.birthday = birthday;
        this.latLon = latLon;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setLatLon(HashMap<String, Double> latLon) {
        this.latLon = latLon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getComment() {
        return comment;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getLocation() {
        return location;
    }

    public String getBirthday() {
        return birthday;
    }

    public HashMap<String, Double> getLatLon() {
        return latLon;
    }
}
