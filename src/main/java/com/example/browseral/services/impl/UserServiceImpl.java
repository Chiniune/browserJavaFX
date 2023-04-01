package com.example.browseral.services.impl;

import com.example.browseral.dao.UserDAO;
import com.example.browseral.dao.impl.UserDAOImpl;
import com.example.browseral.models.MacAddress;
import com.example.browseral.models.User;
import com.example.browseral.services.MacAddressService;
import com.example.browseral.services.UserService;
import com.example.browseral.utils.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDAO userDAO = (UserDAO) new UserDAOImpl();
    private PasswordEncoder passwordEncoder = new PasswordEncoder();
    private MacAddressService macAddressService = new MacAddressServiceImpl();

    @Override
    public List<User> fetchUsers() {
        return userDAO.findAll();
    }

    @Override
    public List<User> fetchUsersByMacAddress(String address) {
        MacAddress macAddress = macAddressService.getMacAddressByAddress(address);
        if (macAddress == null) {
            MacAddress newMacAddress = new MacAddress();
            newMacAddress.setMacAddress(address);
            macAddressService.createMacAddress(newMacAddress);
            return new ArrayList<>();
        }
        return userDAO.findAllByMacAddress(address);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDAO.findById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public User createUser(User user) {
        if (userDAO.existsByEmail(user.getEmail())) {
            return null;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDAO.saveUser(user);
    }

    @Override
    public User updateUser(Integer userId, User user) {
        return null;
    }
}
