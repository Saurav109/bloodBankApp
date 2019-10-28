package com.example.bloodbank.bloodBank;

public class BloodBankValueHolder {
    String name,address,contact;

    BloodBankValueHolder(){ }

    public BloodBankValueHolder(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }
}
