package com.example.facebooklogin.ui.post;

public class post {
    private String Name;
    private String Content;
    private int Image;

    public post() {
    }

    public post(String name,String content, int image) {
        Name = name;
        Content=content;
        Image = image;
    }

    //getter
    public String getName() { return Name; }

    public String getContent() { return Content; }

    public int getImage() {
        return Image;
    }

    //setter

    public void setName(String name) {
        Name = name;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setImage(int image) {
        Image = image;
    }
}
