package com.example.browseral.mappers;


import com.example.browseral.models.Bookmark;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookmarkMapper implements RowMapper<Bookmark> {
    @Override
    public Bookmark mapRow(ResultSet resultSet) {
        try {
            Bookmark bookmark = new Bookmark();
            bookmark.setId(resultSet.getInt("id"));
            bookmark.setName(resultSet.getString("name"));
            bookmark.setUrl(resultSet.getString("url"));
            bookmark.setCreatedDate(resultSet.getTimestamp("createdDate"));
            return bookmark;
        } catch (SQLException e) {
            return null;
        }
    }
}
