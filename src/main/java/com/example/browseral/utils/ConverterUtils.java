package com.example.browseral.utils;

import com.google.gson.Gson;

import java.util.Map;

public class ConverterUtils {
    public Map convertStringIntoMap(String str) {
        Gson gson = new Gson();
        return gson.fromJson(str, Map.class);
    }
}
