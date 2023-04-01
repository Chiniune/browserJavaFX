package com.example.browseral.models;

public class Category extends AbstractModel{
    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{{Category: ID = " + this.getId()
                + ", Name = " + this.getName()
                + ", Code = " + this.getCode()
                + ", Created Date = " + this.getCreatedDate() + "}}";
    }
}
