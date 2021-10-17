package com.example.mstbullion;

public class UserProfile {
    String Business,Name,Phone,GST = "";
    int limit = 2;
    boolean Trading = false;

    public UserProfile(String business, String name, String phone, boolean trading, String gst) {
        Business = business;
        Name = name;
        Phone = phone;
        Trading = trading;
        GST = gst;
    }

    public UserProfile() {

    }

    public boolean isTrading() {
        return Trading;
    }

    public void setTrading(boolean trading) {
        Trading = trading;
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

    public String getGST() {
        return GST;
    }

    public void setGST(String GST) {
        this.GST = GST;
    }
}
