package com.example.browseral.api;

import com.example.browseral.utils.ConverterUtils;
import com.example.browseral.utils.IPNetworkUtils;
import com.example.browseral.utils.ReadAPIUtils;
import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GoogleAPIFetching {
    private static final String SEARCH_ENGINE = "google";
    private static final String HTTPS_PREFIX = "https://";
    private static final String HTTP_PREFIX = "http://";
    private static final String SECRET_TOKEN_LOCATION_API = "3e8de0732c6730";
    private static final String SECRET_API_KEY_WEATHER = "28bcba5ac95a61f8c2c46ac449fd735e";

    private final IPNetworkUtils ipNetworkUtils = new IPNetworkUtils();
    private final ConverterUtils converterUtils = new ConverterUtils();
    private final ReadAPIUtils readAPIUtils = new ReadAPIUtils();

    public String[] suggestionSearching(String term) {
        try {
            JSONParser jsonParser = new JSONParser();
            term = term.replace(" ", "+");
            String fetchingUrl = "https://suggestqueries.google.com/complete/search?q=" + term + "&client=chrome&hl=en&ie=UTF-8";
            URL url = new URL(fetchingUrl);
            URLConnection urlConnection = url.openConnection();
            Reader reader = new InputStreamReader(urlConnection.getInputStream(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String inputLine;
            String value = "";
            while ((inputLine = bufferedReader.readLine()) != null) {
                JSONArray array = (JSONArray) jsonParser.parse(inputLine);
                value = array.get(1).toString();
            }
            bufferedReader.close();
            String[] result = value.replace("[", "").replace("]", "").replace("\"", "").replace("\\","").split(",");
            if (result.length == 1 && result[0].equals("")) {
                return new String[0];
            } else {
                return result;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String searchUrlByTerm(String term) {
        String url;
        if (term != null && !term.isEmpty()) {
            if (!term.contains(".")) {
                term = term.replace(" ", "+");
                url = HTTPS_PREFIX + "www." + SEARCH_ENGINE + ".com/search?q=" + term + "&sourceid=chrome&ie=UTF-8&aq=" + term;
            } else {
                if (term.startsWith(HTTPS_PREFIX)) {
                    url = term;
                } else if (term.startsWith(HTTP_PREFIX)) {
                    url = term.replace(HTTP_PREFIX, HTTPS_PREFIX);
                } else {
                    url = HTTPS_PREFIX + term;
                }
            }
        } else {
            url = "";
        }
        return url;
    }

    public Map<String, Object> getLivingDetails() {
        try {
            IPinfo ipInfo = new IPinfo.Builder().setToken(SECRET_TOKEN_LOCATION_API).build();
            IPResponse ipResponse = ipInfo.lookupIP(ipNetworkUtils.getNetworkIP());
            Map<String, Object> result = new HashMap<>();

            Map<String, Object> locationMap = new HashMap<>();
            locationMap.put("state", ipResponse.getCity());
            locationMap.put("country", ipResponse.getCountryCode());
            result.put("location", locationMap);

            String fetchWeatherUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + locationMap.get("state").toString().replace(" ", "+") + "," + locationMap.get("country").toString() + "&appid" + "=" + SECRET_API_KEY_WEATHER + "&units=imperial";
            StringBuilder stringBuilder = readAPIUtils.readResponseApiByString(fetchWeatherUrl);

            Map reuseMap = converterUtils.convertStringIntoMap(String.valueOf(stringBuilder));
            Map<String, Object> weatherMap = new HashMap<>();

            String tempStr = converterUtils.convertStringIntoMap(reuseMap.get("main").toString()).get("temp").toString();
            Float tempVal = Float.parseFloat(tempStr);
            tempVal = (tempVal - 32) * 5 / 9;
            tempVal = (float) Math.round(tempVal * 10) / 10;
            weatherMap.put("temp", tempVal);

            String weatherVal[] = ((ArrayList) reuseMap.get("weather")).get(0).toString().replace("id", "").replace("main", "").replace("description", "").replace("icon", "").replace("=", "").replace("{", "").replace("}", "").split("," + " ");
            weatherMap.put("weather", weatherVal[1]);
            weatherMap.put("description", weatherVal[2]);
            weatherMap.put("icon", weatherVal[3]);
            result.put("weather", weatherMap);

            return result;
        } catch (RateLimitedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> getTopHeadlines() {
        return null;
    }
}
