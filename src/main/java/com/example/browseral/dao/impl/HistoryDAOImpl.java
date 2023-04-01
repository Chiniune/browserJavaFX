package com.example.browseral.dao.impl;

import com.example.browseral.dao.HistoryDAO;
import com.example.browseral.mappers.HistoryMapper;
import com.example.browseral.models.History;
import com.example.browseral.models.User;

import java.sql.Timestamp;
import java.util.List;

public class HistoryDAOImpl extends GenericDAOImpl<History> implements HistoryDAO {
    @Override
    public List<History> findAllByUser(User user) {
        String sql = "SELECT * FROM histories WHERE user_id = ?";
        return query(sql, new HistoryMapper(), user.getId());
    }

    @Override
    public History findById(Integer historyId) {
        String sql = "SELECT * FROM histories WHERE id = ?";
        List<History> histories = query(sql, new HistoryMapper(), historyId);
        return histories == null || histories.size() == 0 ? null : histories.get(0);
    }

    @Override
    public History saveHistory(History history) {
        return createHistory(history);
    }

    @Override
    public void deleteHistory(Integer historyId) {
        String sql = "DELETE FROM histories WHERE id = ?";
        delete(sql, historyId);
    }

    @Override
    public void addUserHistoryItem(User user, History history) {
        String sql = "UPDATE histories SET user_id = ? WHERE id = ?";
        update(sql, user.getId(), history.getId());
        user.addHistory(history);
    }

    @Override
    public void removeUserHistoryItem(User user, Integer historyId) {
        user.removeHistory(historyId);
        deleteHistory(historyId);
    }

    private History createHistory(History history) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String sql = "INSERT INTO histories (url, createdDate) VALUES (?, ?)";
        Integer historyId = create(sql, history.getUrl(), currentTime);
        return findById(historyId);
    }
}
