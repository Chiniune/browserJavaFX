package com.example.browseral.mappers;

import com.example.browseral.models.Category;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryMapper implements RowMapper<Category> {
    @Override
    public Category mapRow(ResultSet resultSet) {
        try {
            Category category = new Category();
            category.setId(resultSet.getInt("id"));
            category.setCode(resultSet.getString("code"));
            category.setName(resultSet.getString("name"));
            category.setCreatedDate(resultSet.getTimestamp("createdDate"));
            return category;
        } catch (SQLException e) {
            return null;
        }
    }
}
