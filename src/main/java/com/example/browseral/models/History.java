package com.example.browseral.models;

public class History extends AbstractModel {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{{History: ID = " + this.getId()
                + ", URL = " + this.getUrl()
                + ", Created Date" + this.getCreatedDate() + "}}";
    }
}
