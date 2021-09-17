package com.example.mstbullion;

public class UserProfile {
    String Business,Name,Phone;
    int limit = 2;

    public UserProfile(String business, String name, String phone) {
        Business = business;
        Name = name;
        Phone = phone;
    }

    public UserProfile() {

    }

    public String getBusiness() {
        return Business;
    }

    public void setBusiness(String business) {
        Business = business;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
