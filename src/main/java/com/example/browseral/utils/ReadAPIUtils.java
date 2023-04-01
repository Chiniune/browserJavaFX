package com.example.browseral.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ReadAPIUtils {
    public StringBuilder readResponseApiByString(String fetchUrl) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            URL url = new URL(fetchUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            return stringBuilder;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
