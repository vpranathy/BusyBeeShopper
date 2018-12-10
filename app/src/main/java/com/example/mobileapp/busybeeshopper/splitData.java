package com.example.mobileapp.busybeeshopper;

/**
 * Created by anike on 09-12-2018.
 */

public class splitData {
    private String usernam;
    private int amount;

    public String getUsernam() {
        return usernam;
    }

    public void setUsernam(String usernam) {
        this.usernam = usernam;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public splitData() {

    }

    public splitData(String usernam, int amount) {

        this.usernam = usernam;
        this.amount = amount;
    }
}
