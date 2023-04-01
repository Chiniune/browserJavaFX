package com.example.browseral.services;

import com.example.browseral.models.User;

import java.util.List;

public interface UserService {
    List<User> fetchUsers();
    List<User> fetchUsersByMacAddress(String address);
    User getUserById(Integer userId);
    User getUserByEmail(String email);
    User createUser(User user);
    User updateUser(Integer userId, User user);
}
