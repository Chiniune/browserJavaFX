package com.example.browseral.dao;

import com.example.browseral.models.Bookmark;
import com.example.browseral.models.User;

import java.util.List;

public interface BookmarkDAO {
    List<Bookmark> findAllByUser(User user);
    Bookmark saveBookmark(Bookmark bookmark);
    Bookmark findById(Integer bookmarkId);

    void addBookmarkForUser(User user, Bookmark bookmark);

}
