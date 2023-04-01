package com.example.browseral.dao.impl;


import com.example.browseral.dao.BookmarkDAO;
import com.example.browseral.mappers.BookmarkMapper;
import com.example.browseral.models.Bookmark;
import com.example.browseral.models.User;

import java.sql.Timestamp;
import java.util.List;

public class BookmarkDAOImpl extends GenericDAOImpl<Bookmark> implements BookmarkDAO {

    @Override
    public List<Bookmark> findAllByUser(User user) {
        String sql = "SELECT * FROM bookmarks WHERE user_id = ?";
        return query(sql, new BookmarkMapper(), user.getId());
    }

    @Override
    public Bookmark saveBookmark(Bookmark bookmark) {
        if (bookmark.getId() == null) {
            return createBookmark(bookmark);
        } else {
            updateBookmark(bookmark);
            return findById(bookmark.getId());
        }
    }

    @Override
    public Bookmark findById(Integer bookmarkId) {
        String sql = "SELECT * FROM bookmarks WHERE id = ?";
        List<Bookmark> bookmarks = query(sql, new BookmarkMapper(), bookmarkId);
        return bookmarks == null || bookmarks.size() == 0 ? null : bookmarks.get(0);
    }

    @Override
    public void addBookmarkForUser(User user, Bookmark bookmark) {
        bookmark = saveBookmark(bookmark);
        String sql = "UPDATE bookmarks SET user_id = ? WHERE id = ?";
        update(sql, user.getId(), bookmark.getId());
        user.addBookmark(bookmark);
    }

    private Bookmark createBookmark(Bookmark bookmark) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String sql = "INSERT INTO bookmarks (name, url, createdDate) VALUES (?, ?, ?)";
        Integer userId = create(sql, bookmark.getName(), bookmark.getUrl(), currentTime);
        return findById(userId);
    }

    public Bookmark updateBookmark(Bookmark bookmark) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String sql = "UPDATE bookmarks SET name = ?, url = ? WHERE id = ?";
        update(sql, bookmark.getName(), bookmark.getUrl(), bookmark.getId());
        return findById(bookmark.getId());
    }
}
