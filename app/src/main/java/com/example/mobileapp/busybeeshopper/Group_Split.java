package com.example.mobileapp.busybeeshopper;

/**
 * Created by anike on 09-12-2018.
 */

public class Group_Split {
    private String addedBy, boughtBy, itemName;
    private int itemPrice;

    public Group_Split() {
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public String getBoughtBy() {
        return boughtBy;
    }

    public void setBoughtBy(String boughtBy) {
        this.boughtBy = boughtBy;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Group_Split(String addedBy, String boughtBy, String itemName, int itemPrice) {

        this.addedBy = addedBy;
        this.boughtBy = boughtBy;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }
}
