package com.example.browseral.models;

public class Bookmark extends AbstractModel {
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{{Bookmark: ID = " + this.getId()
                + ", Name = " + this.getName()
                + ", URL = " + this.getUrl()
                + ", Created Date = " + this.getCreatedDate() + "}}";
    }
}
