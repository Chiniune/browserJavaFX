package com.example.browseral.services.impl;


import com.example.browseral.dao.HistoryDAO;
import com.example.browseral.dao.UserDAO;
import com.example.browseral.dao.impl.HistoryDAOImpl;
import com.example.browseral.dao.impl.UserDAOImpl;
import com.example.browseral.models.History;
import com.example.browseral.models.User;
import com.example.browseral.services.HistoryService;

import java.util.List;

public class HistoryServiceImp implements HistoryService {
    private HistoryDAO historyDAO = (HistoryDAO) new HistoryDAOImpl();
    private UserDAO userDAO = (UserDAO) new UserDAOImpl();

    @Override
    public List<History> fetchHistoriesByUser(User user) {
        return historyDAO.findAllByUser(user);
    }

    @Override
    public History getHistoryById(Integer historyId) {
        return historyDAO.findById(historyId);
    }

    @Override
    public History createHistory(User user, History history) {
        history = historyDAO.saveHistory(history);
        historyDAO.addUserHistoryItem(user, history);
        return history;
    }

    @Override
    public void deleteHistory(User user, Integer historyId) {
        historyDAO.removeUserHistoryItem(user, historyId);
    }
}
