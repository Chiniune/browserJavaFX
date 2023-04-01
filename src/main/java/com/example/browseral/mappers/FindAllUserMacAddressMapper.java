package com.example.browseral.mappers;

import com.example.browseral.models.MacAddress;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FindAllUserMacAddressMapper implements RowMapper<String> {
    @Override
    public String mapRow(ResultSet resultSet) throws SQLException {
        return resultSet.getString("fullName");
    }
}
