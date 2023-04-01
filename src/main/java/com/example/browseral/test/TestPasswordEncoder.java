package com.example.browseral.test;

import com.example.browseral.utils.PasswordEncoder;

public class TestPasswordEncoder {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        System.out.println(passwordEncoder.encode("123456"));
        System.out.println(passwordEncoder.matches("12345", "e10adc3949ba59abbe56e057f20f883e"));
        System.out.println(passwordEncoder.matches("123456", "e10adc3949ba59abbe56e057f20f883e"));
        System.out.println(passwordEncoder.matches("123456", "e10adc3949ba59abbe56e057f20f883"));
    }
}
