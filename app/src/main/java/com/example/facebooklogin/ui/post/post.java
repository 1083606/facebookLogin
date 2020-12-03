package com.example.facebooklogin.ui.post;

public class post {
    String post_id;
    String habbit_cat_name;
    String user_id;
    String user_name;
    String post_photo;
    String content;
    String created_at;
    String updated_at;
    String likesNum;

    public post() {
    }


    public post(String post_id, String habbit_cat_name, String user_id, String user_name, String post_photo, String content, String created_at, String updated_at, String likesNum) {
        this.post_id = post_id;
        this.habbit_cat_name = habbit_cat_name;
        this.user_id = user_id;
        this.user_name = user_name;
        this.post_photo = post_photo;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.likesNum = likesNum;
    }

    //getter------------------------

    public String getPost_id() {
        return post_id;
    }

    public String getHabbit_cat_name() {
        return habbit_cat_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPost_photo() {
        return post_photo;
    }

    public String getContent() {
        return content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getLikesNum() {
        return likesNum;
    }


    //setter------------------------


    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setHabbit_cat_name(String habbit_cat_name) {
        this.habbit_cat_name = habbit_cat_name;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPost_photo(String post_photo) {
        this.post_photo = post_photo;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setLikesNum(String likesNum) {
        this.likesNum = likesNum;
    }
}
