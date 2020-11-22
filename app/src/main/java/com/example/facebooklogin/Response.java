package com.example.facebooklogin;

public class Response {

    String text;
    boolean isMe;
    boolean isBot;

    public Response(String text, boolean isMe,boolean isBot) {
        this.text = text;
        this.isMe = isMe;
        this.isBot = isBot;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public boolean isBot() {
        return isBot;
    }
}

