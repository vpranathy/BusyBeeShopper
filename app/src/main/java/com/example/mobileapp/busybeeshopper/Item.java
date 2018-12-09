package com.example.mobileapp.busybeeshopper;

/**
 * Created by anike on 07-12-2018.
 */

public class Item {

    private String ItemID, ItemName, Description;

    public String getItemID() {
        return ItemID;
    }

    public Item() {

    }

    public String getItemName() {
        return ItemName;
    }

    public String getDescription() {
        return Description;
    }

    public Item(String description, String itemName, String itemID) {

        this.ItemID = itemID;
        this.ItemName = itemName;
        this.Description = description;
    }
}
