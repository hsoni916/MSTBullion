package com.example.mstbullion;

public class BullionEntry {
    String Label;
    double Price;

    public BullionEntry(String label, double price) {
        Label = label;
        Price = price;
    }

    public BullionEntry() {

    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

}
