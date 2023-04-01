package com.example.browseral.dao;


import com.example.browseral.models.User;

import java.util.List;

public interface UserDAO {
    List<User> findAll();
    List<User> findAllByMacAddress(String address);
    User findById(Integer userId);
    User findByEmail(String email);
    User saveUser(User user);
    boolean existsByEmail(String email);

    boolean checkUser(String email);


}
