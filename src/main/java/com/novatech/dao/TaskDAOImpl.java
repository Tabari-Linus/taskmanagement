package com.novatech.dao;

import com.novatech.model.Task;
import com.novatech.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAOImpl implements TaskDAO {

    @Override
    public Task findById(int id) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Task task = null;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                task = extractTaskFromResultSet(rs);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBUtil.closeConnection(conn);
        }

        return task;
    }

    @Override
    public List<Task> findAll() throws SQLException {
        String sql = "SELECT * FROM tasks ORDER BY due_date";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Task> tasks = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBUtil.closeConnection(conn);
        }

        return tasks;
    }

    @Override
    public List<Task> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE user_id = ? ORDER BY due_date";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Task> tasks = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBUtil.closeConnection(conn);
        }

        return tasks;
    }

    @Override
    public List<Task> findByStatus(int userId, String status) throws SQLException {
        String sql = "SELECT * FROM tasks WHERE user_id = ? AND status = ? ORDER BY due_date";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Task> tasks = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setString(2, status);
            rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBUtil.closeConnection(conn);
        }

        return tasks;
    }

    @Override
    public int create(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (title, description, due_date, status, priority, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, task.getDueDate());
            stmt.setString(4, task.getStatus());
            stmt.setString(5, task.getPriority());
            stmt.setInt(6, task.getUserId());
            rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
                task.setId(id);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBUtil.closeConnection(conn);
        }

        return id;
    }

    @Override
    public boolean update(Task task) throws SQLException {
        String sql = "UPDATE tasks SET title = ?, description = ?, due_date = ?, status = ?, " +
                "priority = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean updated = false;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, task.getTitle());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, task.getDueDate());
            stmt.setString(4, task.getStatus());
            stmt.setString(5, task.getPriority());
            stmt.setInt(6, task.getId());

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
        String sql = "DELETE FROM tasks WHERE id = ?";
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

    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setDueDate(rs.getDate("due_date"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getString("priority"));
        task.setUserId(rs.getInt("user_id"));
        task.setCreatedAt(rs.getTimestamp("created_at"));
        task.setUpdatedAt(rs.getTimestamp("updated_at"));
        return task;
    }
}