package com.example.capstondesign_team_cs;

public class UserInfo {
    Boolean state;
    String name;
    String email;
    String phone;

    public UserInfo() {

    }

    public void setUserInfo(Boolean state, String name, String email, String phone) {
        this. state = state;
        this. name = name;
        this.email = email;
        this.phone = phone;
    }

    public Boolean getUserState() {
        return state;
    }

    public String getUserName() {
        return name;
    }

    public String getUserEmail() {
        return email;
    }

    public String getUserPhone() {
        return phone;
    }

}

