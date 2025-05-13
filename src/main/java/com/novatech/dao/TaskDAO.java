package com.novatech.dao;

import com.novatech.model.Task;
import java.sql.SQLException;
import java.util.List;

public interface TaskDAO {
    Task findById(int id) throws SQLException;
    List<Task> findAll() throws SQLException;
    List<Task> findByUserId(int userId) throws SQLException;
    List<Task> findByStatus(int userId, String status) throws SQLException;
    int create(Task task) throws SQLException;
    boolean update(Task task) throws SQLException;
    boolean delete(int id) throws SQLException;
}