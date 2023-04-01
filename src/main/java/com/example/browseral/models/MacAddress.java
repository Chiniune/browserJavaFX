package com.example.browseral.models;

public class MacAddress extends AbstractModel {
    private String macAddress;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public String toString() {
        return "{{Mac Address: ID = " + this.getId()
                + ", Address: " + this.getMacAddress()
                + ", Created Date: " + this.getCreatedDate() + "}}";
    }
}
