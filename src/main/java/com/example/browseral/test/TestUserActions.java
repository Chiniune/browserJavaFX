package com.example.browseral.test;

import com.example.browseral.dao.impl.MacAddressDAOImpl;

public class TestUserActions {
    public static void main(String[] args) {
        System.out.println(new MacAddressDAOImpl().findAllUserByAddress("40:EC:99:E2:38:A9"));
    }
}
