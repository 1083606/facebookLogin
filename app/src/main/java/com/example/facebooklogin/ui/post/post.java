package com.example.facebooklogin.ui.post;

public class post {
    private int UserImage;
    private String Name;
    private String Content;
    private int Image;

    public post() {
    }

    public post(int userImage, String name,String content, int image) {
        UserImage = userImage;
        Name = name;
        Content=content;
        Image = image;
    }

    //getter
    public int getUserImage() {return UserImage;}

    public String getName() { return Name; }

    public String getContent() { return Content; }

    public int getImage() {
        return Image;
    }

    //setter
    public void setUserImage(int userImage) {
        Image = userImage;
    }

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
