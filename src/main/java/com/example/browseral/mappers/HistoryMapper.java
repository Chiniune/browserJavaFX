package com.example.browseral.mappers;

import com.example.browseral.models.History;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryMapper implements RowMapper<History> {
    @Override
    public History mapRow(ResultSet resultSet) {
        try {
            History history = new History();
            history.setId(resultSet.getInt("id"));
            history.setUrl(resultSet.getString("url"));
            history.setCreatedDate(resultSet.getTimestamp("createdDate"));
            return history;
        } catch (SQLException e) {
            return null;
        }
    }
}
