package com.example.browseral.dao.impl;


import com.example.browseral.dao.CategoryDAO;
import com.example.browseral.mappers.CategoryMapper;
import com.example.browseral.models.Category;
import com.example.browseral.models.User;

import java.util.List;

public class CategoryDAOImpl extends GenericDAOImpl<Category> implements CategoryDAO {
    @Override
    public List<Category> findAll() {
        String sql = "SELECT * FROM categories";
        return query(sql, new CategoryMapper());
    }

    @Override
    public List<Category> findAllByUser(User user) {
        String sql = "SELECT c.id, c.code, c.name, c.createdDate FROM categories c JOIN users_categories uc ON c.id = uc.category_id JOIN users u ON uc.user_id = u.id AND u.id = ?";
        return query(sql, new CategoryMapper(), user.getId());
    }

    @Override
    public Category findById(Integer categoryId) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        List<Category> categories = query(sql, new CategoryMapper(), categoryId);
        return categories == null || categories.size() == 0 ? null : categories.get(0);
    }

    @Override
    public Category findByCode(String code) {
        String sql = "SELECT * FROM categories WHERE code = ?";
        List<Category> categories = query(sql, new CategoryMapper(), code);
        return categories == null || categories.size() == 0 ? null : categories.get(0);
    }

    @Override
    public Category findByName(String name) {
        String sql = "SELECT * FROM categories WHERE name = ?";
        List<Category> categories = query(sql, new CategoryMapper(), name);
        return categories == null || categories.size() == 0 ? null : categories.get(0);
    }

    @Override
    public Boolean existByCode(String code) {
        return findByCode(code) != null;
    }

    @Override
    public Boolean existByName(String name) {
        return findByName(name) != null;
    }

    @Override
    public Category saveCategory(Category category) {
        return null;
    }
}
