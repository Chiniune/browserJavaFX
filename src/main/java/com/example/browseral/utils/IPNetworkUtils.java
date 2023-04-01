package com.example.browseral.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class IPNetworkUtils {
    public String getNetworkIP() {
        String urlString = "https://checkip.amazonaws.com/";
        URL url = null;
        String IP = null;
        try {
            url = new URL(urlString);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            IP = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return IP;
    }

    public String getMacAddress() {
        String macAddress = null;
        try {
            InetAddress ipAddress = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddress);
            byte[] macAddressBytes = networkInterface.getHardwareAddress();
            StringBuilder macAddressBuilder = new StringBuilder();
            for (int macAddressByteIndex = 0; macAddressByteIndex < macAddressBytes.length; macAddressByteIndex++) {
                String macAddressHexByte = String.format("%02X", macAddressBytes[macAddressByteIndex]);
                macAddressBuilder.append(macAddressHexByte);
                if (macAddressByteIndex != macAddressBytes.length - 1) {
                    macAddressBuilder.append(":");
                }
            }
            macAddress = macAddressBuilder.toString();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return macAddress;
    }
}
