package com.example.mstbullion;

import java.util.ArrayList;
import java.util.List;

public class PamperPackArray extends BullionEntry{
    ArrayList<BullionEntry> bullionEntries;

    PamperPackArray(){

    }

    public PamperPackArray(ArrayList<BullionEntry> bullionEntryList2) {
        this.bullionEntries = bullionEntryList2;
    }

    public ArrayList<BullionEntry> getBullionEntries() {
        return bullionEntries;
    }

    public void setBullionEntries(ArrayList<BullionEntry> bullionEntries) {
        this.bullionEntries = bullionEntries;
    }
}
