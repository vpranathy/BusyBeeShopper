package com.example.mobileapp.busybeeshopper;

/**
 * Created by anike on 08-12-2018.
 */

public class Users {
    private String email , username, group;
    private  int type;
    public Users() {
    }

    public String getEmail() {

        return email;
    }

    public String getUsername() {
        return username;
    }

    public int getType() {
        return type;
    }

    public String getGroup() {
        return group;
    }

    public Users(String email, String username, int type, String group) {

        this.email = email;
        this.username = username;
        this.type = type;
        this.group = group;
    }
}
