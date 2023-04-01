package com.example.browseral.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {
    public String encode(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            messageDigest.update(str.getBytes());
            byte[] bytes = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (Byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean matches(String rawStr, String encodedStr) {
        String md5 = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            messageDigest.update(rawStr.getBytes());
            byte[] digest = messageDigest.digest();
            md5 = new BigInteger(1, digest).toString(16);
            return md5.equals(encodedStr);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }
}
