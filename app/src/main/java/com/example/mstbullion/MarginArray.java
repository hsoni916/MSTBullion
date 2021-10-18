package com.example.mstbullion;

import java.util.List;

public class MarginArray extends Margin {

    List<Margin> margins;
    boolean tradingflag;

    public List<Margin> getMargins() {
        return margins;
    }

    public void setMargins(List<Margin> margins) {
        this.margins = margins;
    }

    public MarginArray(String label, double rate) {
        super(label, rate);
    }

    public int getSize(){
        return margins.size();
    }

    public boolean isTradingflag() {
        return tradingflag;
    }
}
