package com.mrcoder.model;

public class CallList {
    String userId;
    String userName;
    String userProfileUrl;
    String date;
    String callType;

    public CallList() {
    }

    public CallList(String userId, String userName, String userProfileUrl, String date, String callType) {
        this.userId = userId;
        this.userName = userName;
        this.userProfileUrl = userProfileUrl;
        this.date = date;
        this.callType = callType;
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

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }
}
