package com.example.browseral.mappers;


import com.example.browseral.models.Bookmark;
import com.example.browseral.models.History;
import com.example.browseral.models.User;
import com.example.browseral.services.BookmarkService;
import com.example.browseral.services.HistoryService;
import com.example.browseral.services.impl.BookmarkServiceImpl;
import com.example.browseral.services.impl.HistoryServiceImp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserMapper implements RowMapper<User> {
    private HistoryService historyService = new HistoryServiceImp();
    private BookmarkService bookmarkService = new BookmarkServiceImpl();

    @Override
    public User mapRow(ResultSet resultSet) {
        try {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setFullName(resultSet.getString("fullName"));
            user.setCreatedDate(resultSet.getTimestamp("createdDate"));
            user.setUpdatedDate(resultSet.getTimestamp("updatedDate"));
            List<History> histories = historyService.fetchHistoriesByUser(user);
            user.setHistories(histories);
            List<Bookmark> bookmarks = bookmarkService.fetchBookmarksByUser(user);
            user.setBookmarks(bookmarks);
            return user;
        } catch (SQLException e) {
            return null;
        }
    }
}
