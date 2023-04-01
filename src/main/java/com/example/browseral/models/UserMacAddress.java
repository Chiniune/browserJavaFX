package com.example.browseral.models;

public class UserMacAddress extends AbstractModel {
    private String user_id;
    private String mac_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMac_id() {
        return mac_id;
    }

    public void setMac_id(String mac_id) {
        this.mac_id = mac_id;
    }

    public UserMacAddress(String user_id, String mac_id) {
        this.user_id = user_id;
        this.mac_id = mac_id;
    }

    @Override
    public String toString() {
        return "UserMacAddress{" +
                "user_id='" + user_id + '\'' +
                ", mac_id='" + mac_id + '\'' +
                '}';
    }
}
