package com.example.mstbullion;

public class Margin {
    String Label;
    double Rate;

    public Margin(String label, double rate) {
        Label = label;
        Rate = rate;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

}
