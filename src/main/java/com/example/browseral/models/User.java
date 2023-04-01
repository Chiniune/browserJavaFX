package com.example.browseral.models;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class User extends AbstractModel {
    private String email;
    private String password;
    private String fullName;
    private Timestamp updatedDate;
    private List<History> histories = new ArrayList<>();
    private List<Bookmark> bookmarks = new ArrayList<>();



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<History> getHistories() {
        return histories;
    }

    public void setHistories(List<History> histories) {
        this.histories = histories;
    }

    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public void addHistory(History history) {
        this.histories.add(history);
    }

    public void removeHistory(Integer historyId) {
        this.histories.forEach(h -> {
            if (h.getId() == historyId) {
                this.histories.remove(h);
            }
        });
    }
    public void addBookmark(Bookmark bookmark) {
        this.bookmarks.add(bookmark);
    }

    public void removeBookmark(Integer bookmarkId) {
        this.bookmarks.forEach(b -> {
            if (b.getId() == bookmarkId) {
                this.bookmarks.remove(b);
            }
        });
    }

    @Override
    public String toString() {
        return "{{User: ID = " + this.getId() + ", Email = " + this.getEmail()
                + ", Password = " + this.getPassword() + ", Full name = " + this.getFullName()
                + ", Created Date = " + this.getCreatedDate()
                + ", Updated Date = " + this.getUpdatedDate()
                + ", Histories = " + this.getHistories()
                + ", Bookmarks = " + this.getBookmarks() + "}}";
    }
}
