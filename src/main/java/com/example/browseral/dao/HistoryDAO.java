package com.example.browseral.dao;

import com.example.browseral.models.History;
import com.example.browseral.models.User;

import java.util.List;

public interface HistoryDAO {
    List<History> findAllByUser(User user);
    History findById(Integer historyId);
    History saveHistory(History history);
    void deleteHistory(Integer historyId);
    void addUserHistoryItem(User user, History history);
    void removeUserHistoryItem(User user, Integer historyId);
}
