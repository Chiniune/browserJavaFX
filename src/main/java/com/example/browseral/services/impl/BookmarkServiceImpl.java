package com.example.browseral.services.impl;

import com.example.browseral.dao.BookmarkDAO;
import com.example.browseral.dao.impl.BookmarkDAOImpl;
import com.example.browseral.models.Bookmark;
import com.example.browseral.models.User;
import com.example.browseral.services.BookmarkService;

import java.util.List;

public class BookmarkServiceImpl implements BookmarkService {
    private BookmarkDAO bookmarkDAO = (BookmarkDAO) new BookmarkDAOImpl();

    @Override
    public List<Bookmark> fetchBookmarksByUser(User user) {
        return bookmarkDAO.findAllByUser(user);
    }

    @Override
    public Bookmark createBookmark(User user, Bookmark bookmark) {
        bookmark = bookmarkDAO.saveBookmark(bookmark);
        bookmarkDAO.addBookmarkForUser(user, bookmark);
        return bookmark;
    }
}
