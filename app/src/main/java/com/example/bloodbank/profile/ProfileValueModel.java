package com.example.bloodbank.profile;

import com.example.bloodbank.LatLon;

import java.util.HashMap;

public class ProfileValueModel {
    String imageUrl, bloodGroup, name, email, occupation, comment, mobileNo, location, birthday;
    String drug,aids,jaundice,otherDiseases,cancer;

    HashMap <String,Double> latLon;
    long timeEntry;
    long bloodEntryTimeSnapshot;

    public ProfileValueModel() {
    }

    public ProfileValueModel(String imageUrl, String bloodGroup, String name, String email, String occupation, String comment, String mobileNo, String location,String drug,String aids,String jaundice,String otherDiseases,String cancer, String birthday, HashMap<String, Double> latLon,long bloodEntryTimeSnapshot) {
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
        this.bloodEntryTimeSnapshot=bloodEntryTimeSnapshot;
        this.drug=drug;
        this.aids=aids;
        this.jaundice=jaundice;
        this.cancer=cancer;
        this.otherDiseases=otherDiseases;
    }

    public String getCancer() {
        return cancer;
    }

    public void setCancer(String cancer) {
        this.cancer = cancer;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public void setAids(String aids) {
        this.aids = aids;
    }

    public void setJaundice(String jaundice) {
        this.jaundice = jaundice;
    }

    public void setOtherDiseases(String otherDiseases) {
        this.otherDiseases = otherDiseases;
    }

    public String getDrug() {
        return drug;
    }

    public String getAids() {
        return aids;
    }

    public String getJaundice() {
        return jaundice;
    }

    public String getOtherDiseases() {
        return otherDiseases;
    }

    public long getBloodEntryTimeSnapshot() {
        return bloodEntryTimeSnapshot;
    }

    public void setBloodEntryTimeSnapshot(long bloodEntryTimeSnapshot) {
        this.bloodEntryTimeSnapshot = bloodEntryTimeSnapshot;
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
