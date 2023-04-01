package com.example.browseral.services;

import com.example.browseral.models.Bookmark;
import com.example.browseral.models.User;

import java.util.List;

public interface BookmarkService {
    List<Bookmark> fetchBookmarksByUser(User user);

    Bookmark createBookmark(User user, Bookmark bookmark);
}
