package com.example.browseral.test;

import com.example.browseral.api.GoogleAPIFetching;

import java.util.Map;
import java.util.Objects;

public class TestAPI {
    public static void main(String[] args) {
        GoogleAPIFetching googleAPIFetching = new GoogleAPIFetching();
        // Location by IP
        Map locationMap = (Map) googleAPIFetching.getLivingDetails().get("location");
        Map weatherMap = (Map) googleAPIFetching.getLivingDetails().get("weather");
        System.out.println("State: " + locationMap.get("state"));
        System.out.println("Country: " + locationMap.get("country"));
        System.out.println("Temperature: " + weatherMap.get("temp"));
        System.out.println("Weather: " + weatherMap.get("weather"));
        System.out.println("Description: " + weatherMap.get("description"));
        System.out.println("Icon: " + weatherMap.get("icon"));
        // Search URL
        System.out.println("===== SEARCH URL =====");
        System.out.println(googleAPIFetching.searchUrlByTerm("facebook.com"));
        System.out.println(googleAPIFetching.searchUrlByTerm("http://facebook.com"));
        System.out.println(googleAPIFetching.searchUrlByTerm("https://facebook.com"));
        System.out.println(googleAPIFetching.searchUrlByTerm("facebook"));

        // Search Suggestion
        System.out.println("===== SEARCH SUGGESTION =====");
        for (String s : Objects.requireNonNull(googleAPIFetching.suggestionSearching("dang cap no 1 vip pro"))) {
            System.out.println(s);
        }
    }
}
