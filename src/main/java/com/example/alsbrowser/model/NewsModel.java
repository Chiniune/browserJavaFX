package com.example.alsbrowser.model;

public class NewsModel {

    private String image;
    private String date;
    private String title;
    private String content;

    public NewsModel(String image, String date, String title, String content) {
        this.image = image;
        this.date = date;
        this.title = title;
        this.content = content;
    }
    public NewsModel() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
