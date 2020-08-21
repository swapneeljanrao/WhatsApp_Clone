package com.mrcoder.model;

public class ChatList {
    private String userName;
    private String userId;
    private String date;
    private String userProfileUrl;
    private String Description;

    public ChatList() {
    }

    public ChatList(String userId, String userName, String description, String date, String userProfileUrl) {
        this.userId = userId;
        this.userName = userName;
        this.date = date;
        this.userProfileUrl = userProfileUrl;
        Description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }


}
