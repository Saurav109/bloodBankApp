package com.example.bloodbank.requestBlood;

import com.google.firebase.database.ServerValue;

import java.util.Map;

public class BloodRequestValueModel {
    public long time;
    private String name, location, comment, mobileNo, bloodGroup, uid;
    private boolean bloodFound;
    BloodRequestValueModel() {
    }

    public BloodRequestValueModel(String name, String location, String comment, String mobileNo, String bloodGroup,boolean bloodFound, String uid) {
        this.name = name;
        this.location = location;
        this.comment = comment;
        this.mobileNo = mobileNo;
        this.bloodGroup = bloodGroup;
        this.uid = uid;
        this.bloodFound=bloodFound;
    }

    public boolean isBloodFound() {
        return bloodFound;
    }

    public void setBloodFound(boolean bloodFound) {
        this.bloodFound = bloodFound;
    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public String getLocation() {
        return location;
    }

//    public void setLocation(String location) {
//        this.location = location;
//    }

    public String getComment() {
        return comment;
    }

//    public void setComment(String comment) {
//        this.comment = comment;
//    }

    public String getMobileNo() {
        return mobileNo;
    }

//    public void setMobileNo(String mobileNo) {
//        this.mobileNo = mobileNo;
//    }

    public String getBloodGroup() {
        return bloodGroup;
    }

//    public void setBloodGroup(String bloodGroup) {
//        this.bloodGroup = bloodGroup;
//    }

    public String getUid() {
        return uid;
    }

//    public void setUid(String uid) {
//        this.uid = uid;
//    }

    public Map<String, String> getTIME() {
        return ServerValue.TIMESTAMP;
    }

}
