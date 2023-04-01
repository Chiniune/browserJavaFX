package com.example.browseral.dao;


import com.example.browseral.models.Category;
import com.example.browseral.models.User;

import java.util.List;

public interface CategoryDAO {
    List<Category> findAll();
    List<Category> findAllByUser(User user);
    Category findById(Integer categoryId);
    Category findByCode(String code);
    Category findByName(String name);
    Category saveCategory(Category category);
    Boolean existByCode(String code);
    Boolean existByName(String name);
}
