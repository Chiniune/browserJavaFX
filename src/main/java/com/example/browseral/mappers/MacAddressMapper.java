package com.example.browseral.mappers;

import com.example.browseral.models.MacAddress;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MacAddressMapper implements RowMapper<MacAddress> {
    @Override
    public MacAddress mapRow(ResultSet resultSet) {
        try {
            MacAddress macAddress = new MacAddress();
            macAddress.setId(resultSet.getInt("id"));
            macAddress.setMacAddress(resultSet.getString("mac_address"));
            macAddress.setCreatedDate(resultSet.getTimestamp("createdDate"));
            return macAddress;
        } catch (SQLException e) {
            return null;
        }
    }
}
