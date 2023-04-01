package com.example.browseral.models;

public class AccountModel {

    int id;
    String emailName;
    String avt;

    public AccountModel(int id, String emailAdress, String avt) {
        this.id = id;
        this.emailName = emailAdress;
        this.avt = avt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmailName() {
        return emailName;
    }

    public void setEmailName(String emailAdress) {
        this.emailName = emailAdress;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }
}
