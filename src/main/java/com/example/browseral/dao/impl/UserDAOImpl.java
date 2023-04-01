package com.example.browseral.dao.impl;

import com.example.browseral.dao.UserDAO;
import com.example.browseral.mappers.UserMapper;
import com.example.browseral.models.User;

import java.sql.Timestamp;
import java.util.List;

public class UserDAOImpl extends GenericDAOImpl<User> implements UserDAO {
    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return query(sql, new UserMapper());
    }

    @Override
    public List<User> findAllByMacAddress(String address) {
        String sql = "SELECT * FROM users u JOIN users_mac_addresses uma ON uma.user_id = u.id JOIN mac_addresses m ON m.id = uma.mac_id AND m.mac_address = ?";
        return query(sql, new UserMapper(), address);
    }

    @Override
    public User findById(Integer userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<User> users = query(sql, new UserMapper(), userId);
        return users == null || users.size() == 0 ? null : users.get(0);
    }

    public User findByActive() {
        String sql = "SELECT * FROM users WHERE active = ?";
        List<User> users = query(sql, new UserMapper(), 1);
        return users == null || users.size() == 0 ? null : users.get(0);
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> users = query(sql, new UserMapper(), email);
        return users == null || users.size() == 0 ? null : users.get(0);
    }

    @Override
    public User saveUser(User user) {
        if (user.getId() == null) {
            return createUser(user);
        } else {
//            updateUser(user);
            return findById(user.getId());
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    private User createUser(User user) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        String sql = "INSERT INTO users (email, password, fullName, createdDate, updatedDate) VALUES (?, ?, ?, ?, ?)";
        Integer userId = create(sql, user.getEmail(), user.getPassword(), user.getFullName(), currentTime, currentTime);
        return findById(userId);
    }

    public void updateUser(int i, String fullname) {
        String sql = "UPDATE users SET active = ? where fullName = ?";
        update(sql,i,fullname);
    }

    public void updateUser2(int i, String email) {
        String sql = "UPDATE users SET active = ? where email = ?";
        update(sql,i,email);
    }
    public void updateUserall() {
        String sql = "UPDATE users SET active = 0";
        update(sql);
    }
    @Override
    public boolean checkUser(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> users = query(sql, new UserMapper(), email);
        return users == null || users.size() == 0 ? false : true;
    }
}
