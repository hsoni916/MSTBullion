package com.example.mstbullion;

import android.os.Parcel;
import android.os.Parcelable;

public class BullionRecord implements Parcelable {

    String buyer,timestamp,label,phone;
    double price,amount,quantity;

    public BullionRecord(){

    }

    public BullionRecord(String buyer, String timestamp, String label, String phone, double price, double amount, double quantity) {

        this.buyer = buyer;
        this.timestamp = timestamp;
        this.label = label;
        this.phone = phone;
        this.price = price;
        this.amount = amount;
        this.quantity = quantity;

    }

    protected BullionRecord(Parcel in) {
        buyer = in.readString();
        timestamp = in.readString();
        label = in.readString();
        phone = in.readString();
        price = in.readDouble();
        amount = in.readDouble();
        quantity = in.readDouble();
    }

    public static final Creator<BullionRecord> CREATOR = new Creator<BullionRecord>() {
        @Override
        public BullionRecord createFromParcel(Parcel in) {
            return new BullionRecord(in);
        }

        @Override
        public BullionRecord[] newArray(int size) {
            return new BullionRecord[size];
        }
    };

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(buyer);
        dest.writeString(timestamp);
        dest.writeString(label);
        dest.writeString(phone);
        dest.writeDouble(price);
        dest.writeDouble(amount);
        dest.writeDouble(quantity);

    }
}
