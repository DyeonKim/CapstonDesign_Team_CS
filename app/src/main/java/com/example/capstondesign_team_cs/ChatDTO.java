package com.example.capstondesign_team_cs;

public class ChatDTO {


    private String userName;
    private String message;

    public ChatDTO() { }
    public ChatDTO(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
