package com.example.SDA1.firechatapp;

public class SetChatItem {

    private String userName;
    private String chatContent;
    private String photoUrl;

    public SetChatItem(String userName, String chatContent, String photoUrl) {
        this.userName = userName;
        this.chatContent = chatContent;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return userName;
    }
    public  void setName(String userName) {
        this.userName = userName;
    }
    public String getChatContent() {
        return chatContent;
    }
    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}