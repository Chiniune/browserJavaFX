package com.example.browseral.services.impl;

import com.example.browseral.dao.MacAddressDAO;
import com.example.browseral.dao.impl.MacAddressDAOImpl;
import com.example.browseral.models.MacAddress;
import com.example.browseral.services.MacAddressService;

public class MacAddressServiceImpl implements MacAddressService {
    private MacAddressDAO macAddressDAO = (MacAddressDAO) new MacAddressDAOImpl();

    @Override
    public MacAddress createMacAddress(MacAddress macAddress) {
        if (macAddressDAO.existsByAddress(macAddress.getMacAddress())) {
            return null;
        }
        return macAddressDAO.saveMacAddress(macAddress);
    }

    @Override
    public MacAddress getMacAddressByAddress(String address) {
        return macAddressDAO.findByAddress(address);
    }
}
