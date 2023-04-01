package com.example.browseral.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FindIdUser implements RowMapper<String> {
    @Override
    public String mapRow(ResultSet resultSet) throws SQLException {
        return resultSet.getString("id");
    }
}
