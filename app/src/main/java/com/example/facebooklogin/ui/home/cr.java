package com.example.facebooklogin.ui.home;

public class cr {
    String chatroom_id;
    String user_id;
    String nick_name;
    String role_id;
    String role_name;
    String role_photo;
    String habbit_id;
    String habbit_name;
    String habbit_status;
    String signed_time;
    String completion;
    String original_intention;
    String goodness;
    String badness;
    String days;
    String created_at;
    String updated_at;

    public cr() {
    }


    public cr(String chatroom_id, String user_id, String nick_name, String role_id, String role_name, String role_photo, String habbit_id, String habbit_name, String habbit_status, String signed_time, String completion, String original_intention, String goodness, String badness, String days, String created_at, String updated_at) {
        this.chatroom_id = chatroom_id;
        this.user_id = user_id;
        this.nick_name = nick_name;
        this.role_id = role_id;
        this.role_name = role_name;
        this.role_photo = role_photo;
        this.habbit_id = habbit_id;
        this.habbit_name = habbit_name;
        this.habbit_status = habbit_status;
        this.signed_time = signed_time;
        this.completion = completion;
        this.original_intention = original_intention;
        this.goodness = goodness;
        this.badness = badness;
        this.days = days;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    //getter------------------------

    public String getChatroom_id() {
        return chatroom_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public String getRole_id() {
        return role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public String getRole_photo() {
        return role_photo;
    }

    public String getHabbit_id() {
        return habbit_id;
    }

    public String getHabbit_name() {
        return habbit_name;
    }

    public String getHabbit_status() {
        return habbit_status;
    }

    public String getSigned_time() {
        return signed_time;
    }

    public String getCompletion() {
        return completion;
    }

    public String getOriginal_intention() {
        return original_intention;
    }

    public String getGoodness() {
        return goodness;
    }

    public String getBadness() {
        return badness;
    }

    public String getDays() {
        return days;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }


    //setter------------------------


    public void setChatroom_id(String chatroom_id) {
        this.chatroom_id = chatroom_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public void setRole_photo(String role_photo) {
        this.role_photo = role_photo;
    }

    public void setHabbit_id(String habbit_id) {
        this.habbit_id = habbit_id;
    }

    public void setHabbit_name(String habbit_name) {
        this.habbit_name = habbit_name;
    }

    public void setHabbit_status(String habbit_status) {
        this.habbit_status = habbit_status;
    }

    public void setSigned_time(String signed_time) {
        this.signed_time = signed_time;
    }

    public void setCompletion(String completion) {
        this.completion = completion;
    }

    public void setOriginal_intention(String original_intention) {
        this.original_intention = original_intention;
    }

    public void setGoodness(String goodness) {
        this.goodness = goodness;
    }

    public void setBadness(String badness) {
        this.badness = badness;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
