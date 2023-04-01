package com.example.browseral.dao;


import com.example.browseral.models.MacAddress;
import com.example.browseral.models.User;

import java.util.List;

public interface MacAddressDAO {
    MacAddress saveMacAddress(MacAddress macAddress);
    MacAddress findById(Integer macAddressId);
    MacAddress findByAddress(String address);
    Boolean existsByAddress(String address);

    List<String> findAllUserByAddress(String address);
}
