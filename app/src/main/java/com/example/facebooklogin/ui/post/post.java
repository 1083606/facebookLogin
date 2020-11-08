package com.example.facebooklogin.ui.post;

public class post {
    String post_id;
    String habbit_cat_name;
    String user_id;
    String user_name;
    String content;
    String created_at;
    String updated_at;

    public post() {
    }

    public post(String post_id, String habbit_cat_name, String user_id, String user_name, String content, String created_at, String updated_at) {
        this.post_id = post_id;
        this.habbit_cat_name = habbit_cat_name;
        this.user_id = user_id;
        this.user_name = user_name;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    //getter------------------------
    public String getPost_id() {
        return post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
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

    public String getHabbit_cat_name() {
        return habbit_cat_name;
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
