package com.example.browseral.test;

import com.example.browseral.models.Bookmark;
import com.example.browseral.services.BookmarkService;
import com.example.browseral.services.impl.BookmarkServiceImpl;
import com.example.browseral.services.impl.UserServiceImpl;
import com.example.browseral.utils.IPNetworkUtils;

public class TestDatabase {
    public static void main(String[] args) {
        BookmarkService bookmarkService = new BookmarkServiceImpl();

        Bookmark bookmark = new Bookmark();
        bookmark.setName("testhahaaha");
        bookmark.setUrl("youtube.com");

        System.out.println(bookmarkService.createBookmark(new UserServiceImpl().getUserById(12), bookmark));
    }
}
