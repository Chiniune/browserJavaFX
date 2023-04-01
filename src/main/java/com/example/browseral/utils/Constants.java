package com.example.browseral.utils;

public class Constants {
    // Database constants
    private static final String HOST = "localhost";
    private static final Integer PORT = 3306;
    private static final String DATABASE_NAME = "browser_db";
    public static final String DATABASE_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE_NAME;
    public static final String USER = "root";
    public static final String PASS = "";
    // JWT Constants
    public static final String JWT_TOKEN_SECRET = "TLBrowserMakeByToanAndViet";
    public static final long JWT_TOKEN_VALIDITY = 60 * 60 * 1000;
    // Storing Constants
    public static final String STORE_TOKEN_KEY = "TLBrowserTokenKey";
}
