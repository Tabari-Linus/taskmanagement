package com.novatech.dao;

import com.novatech.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User findById(int id) throws SQLException;
    User findByUsername(String username) throws SQLException;
    List<User> findAll() throws SQLException;
    int create(User user) throws SQLException;
    boolean update(User user) throws SQLException;
    boolean delete(int id) throws SQLException;
}