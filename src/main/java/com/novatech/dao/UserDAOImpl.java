package com.novatech.dao;

import com.novatech.model.User;
import com.novatech.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public User findById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = extractUserFromResultSet(rs);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBUtil.closeConnection(conn);
        }

        return user;
    }

    @Override
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                user = extractUserFromResultSet(rs);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBUtil.closeConnection(conn);
        }

        return user;
    }

    @Override
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBUtil.closeConnection(conn);
        }

        return users;
    }

    @Override
    public int create(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, email, full_name) VALUES (?, ?, ?, ?) RETURNING id";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFullName());
            rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
                user.setId(id);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBUtil.closeConnection(conn);
        }

        return id;
    }

    @Override
    public boolean update(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, full_name = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean updated = false;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFullName());
            stmt.setInt(5, user.getId());

            int rowsAffected = stmt.executeUpdate();
            updated = rowsAffected > 0;
        } finally {
            if (stmt != null) stmt.close();
            DBUtil.closeConnection(conn);
        }

        return updated;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean deleted = false;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();
            deleted = rowsAffected > 0;
        } finally {
            if (stmt != null) stmt.close();
            DBUtil.closeConnection(conn);
        }

        return deleted;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}