package com.novatech.service;

import com.novatech.dao.UserDAO;
import com.novatech.dao.UserDAOImpl;
import com.novatech.model.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public User authenticate(String username, String password) throws SQLException {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User findById(int id) throws SQLException {
        return userDAO.findById(id);
    }

    public User findByUsername(String username) throws SQLException {
        return userDAO.findByUsername(username);
    }

    public List<User> findAll() throws SQLException {
        return userDAO.findAll();
    }

    public int create(User user) throws SQLException {
        return userDAO.create(user);
    }

    public boolean update(User user) throws SQLException {
        return userDAO.update(user);
    }

    public boolean delete(int id) throws SQLException {
        return userDAO.delete(id);
    }
}