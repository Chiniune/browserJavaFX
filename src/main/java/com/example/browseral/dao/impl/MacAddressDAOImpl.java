package com.example.browseral.dao.impl;


import com.example.browseral.dao.MacAddressDAO;
import com.example.browseral.mappers.FindAllUserMacAddressMapper;
import com.example.browseral.mappers.FindIdUser;
import com.example.browseral.mappers.MacAddressMapper;
import com.example.browseral.mappers.UserMapper;
import com.example.browseral.models.MacAddress;
import com.example.browseral.models.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MacAddressDAOImpl extends GenericDAOImpl<MacAddress> implements MacAddressDAO {
    @Override
    public MacAddress saveMacAddress(MacAddress macAddress) {
        return createMacAddress(macAddress);
    }

    @Override
    public MacAddress findById(Integer macAddressId) {
        String sql = "SELECT * FROM mac_addresses WHERE id = ?";
        List<MacAddress> macAddresses = query(sql, new MacAddressMapper(), macAddressId);
        return macAddresses == null || macAddresses.size() == 0 ? null : macAddresses.get(0);
    }

    @Override
    public MacAddress findByAddress(String address) {
        String sql = "SELECT * FROM mac_addresses WHERE mac_address = ?";
        List<MacAddress> macAddresses = query(sql, new MacAddressMapper(), address);
        return macAddresses == null || macAddresses.size() == 0 ? null : macAddresses.get(0);
    }

    @Override
    public Boolean existsByAddress(String address) {
        String sql = "SELECT * FROM mac_addresses WHERE mac_address = ?";
        List<MacAddress> macAddresses = query(sql, new MacAddressMapper(), address);
        return macAddresses != null && macAddresses.size() != 0;
    }

    public MacAddress createMacAddress(MacAddress macAddress) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String sql = "INSERT INTO mac_addresses (mac_address, createdDate) VALUES (?, ?)";
        Integer id = create(sql, macAddress.getMacAddress(), currentTime);
        return findById(id);
    }

    @Override
    public List<String> findAllUserByAddress(String address) {
        String sql = "select users.fullName from browser_db.users\n" +
                "inner join browser_db.users_mac_addresses on users.id = users_mac_addresses.user_id \n" +
                "inner join browser_db.mac_addresses on users_mac_addresses.mac_id = mac_addresses.id\n" +
                "where mac_addresses.mac_address = ?";
        List<String> listUser = query(sql, new FindAllUserMacAddressMapper(), address);

    return listUser;
    }

    public void get2(String email, String mac) {
        String sql1 = "select id from browser_db.users \n" +
                "where email = ?";
        String id1 = query(sql1, new FindIdUser(), email).get(0).toString();

        String sql2 = "select id from browser_db.mac_addresses\n" +
                "where mac_address = ?";
        String id2 = query(sql2, new FindIdUser(), mac).get(0).toString();
        System.out.println(id1+" "+id2);

        String sql = "INSERT INTO users_mac_addresses VALUES (?, ?)";
        create(sql, id1, id2);

    }
}
