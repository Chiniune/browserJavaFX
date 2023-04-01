package com.example.browseral.services;

import com.example.browseral.models.MacAddress;

public interface MacAddressService {
    MacAddress createMacAddress(MacAddress macAddress);
    MacAddress getMacAddressByAddress(String address);
}
