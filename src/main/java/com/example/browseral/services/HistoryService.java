package com.example.browseral.services;

import com.example.browseral.models.History;
import com.example.browseral.models.User;

import java.util.List;

public interface HistoryService {
    List<History> fetchHistoriesByUser(User user);
    History getHistoryById(Integer historyId);
    History createHistory(User user, History history);
    void deleteHistory(User user, Integer historyId);
}
