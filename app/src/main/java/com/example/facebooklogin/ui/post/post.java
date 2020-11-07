package com.example.facebooklogin.ui.post;

public class post {
    /*
    private int UserImage;
    private String Name;
    private String Content;
    private int Image;
    */

    String post_id;
    String user_id;
    String title;
    String content;
    String created_at;
    String updated_at;

    public post() {
    }

    public post(String post_id, String user_id, String title, String content, String created_at, String updated_at) {
        this.post_id = post_id;
        this.user_id = user_id;
        this.title = title;
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

    public String getTitle() {
        return title;
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

    //setter------------------------

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setTitle(String title) {
        this.title = title;
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
