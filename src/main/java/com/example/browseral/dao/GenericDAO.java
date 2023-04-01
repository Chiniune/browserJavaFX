package com.example.browseral.dao;

import com.example.browseral.mappers.RowMapper;

import java.util.List;

public interface GenericDAO<T> {
    <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... parameters);
    Integer create(String sql, Object... parameters);
    void update(String sql, Object... parameters);
    void delete(String sql, Object... parameters);
}
