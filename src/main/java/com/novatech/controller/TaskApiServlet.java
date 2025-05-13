package com.novatech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novatech.model.Task;
import com.novatech.model.User;
import com.novatech.service.TaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "TaskApiServlet", urlPatterns = {"/api/tasks", "/api/tasks/*"})
public class TaskApiServlet extends HttpServlet {
    private TaskService taskService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        taskService = new TaskService();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            sendUnauthorized(response);
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getId();
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {

                String statusFilter = request.getParameter("status");
                List<Task> tasks;

                if (statusFilter != null && !statusFilter.isEmpty()) {
                    tasks = taskService.findByStatus(userId, statusFilter);
                } else {
                    tasks = taskService.findByUserId(userId);
                }

                sendJsonResponse(response, tasks);
            } else {
                // Get single task
                int taskId = Integer.parseInt(pathInfo.substring(1));
                Task task = taskService.findById(taskId);

                if (task != null && task.getUserId() == userId) {
                    sendJsonResponse(response, task);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Task not found");
                    sendJsonResponse(response, error);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorResponse(response, e.getMessage());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid task ID");
            sendJsonResponse(response, error);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            sendUnauthorized(response);
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getId();

        try {
            // Parse JSON request body
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Task task = objectMapper.readValue(sb.toString(), Task.class);
            task.setUserId(userId);

            // Validate task
            if (task.getTitle() == null || task.getTitle().trim().isEmpty() ||
                    task.getDueDate() == null ||
                    task.getStatus() == null || task.getStatus().trim().isEmpty()) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Title, due date, and status are required");
                sendJsonResponse(response, error);
                return;
            }

            // Create task
            int taskId = taskService.create(task);
            task.setId(taskId);

            response.setStatus(HttpServletResponse.SC_CREATED);
            sendJsonResponse(response, task);
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorResponse(response, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid JSON format: " + e.getMessage());
            sendJsonResponse(response, error);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            sendUnauthorized(response);
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getId();
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Task ID is required");
            sendJsonResponse(response, error);
            return;
        }

        try {
            int taskId = Integer.parseInt(pathInfo.substring(1));

            // Verify ownership
            Task existingTask = taskService.findById(taskId);
            if (existingTask == null || existingTask.getUserId() != userId) {
                sendUnauthorized(response);
                return;
            }

            // Parse JSON request body
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            Task task = objectMapper.readValue(sb.toString(), Task.class);
            task.setId(taskId);
            task.setUserId(userId);

            // Validate task
            if (task.getTitle() == null || task.getTitle().trim().isEmpty() ||
                    task.getDueDate() == null ||
                    task.getStatus() == null || task.getStatus().trim().isEmpty()) {

                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Title, due date, and status are required");
                sendJsonResponse(response, error);
                return;
            }

            // Update task
            boolean updated = taskService.update(task);
            if (updated) {
                sendJsonResponse(response, task);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Task not found");
                sendJsonResponse(response, error);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorResponse(response, e.getMessage());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid task ID");
            sendJsonResponse(response, error);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid JSON format: " + e.getMessage());
            sendJsonResponse(response, error);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            sendUnauthorized(response);
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getId();
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Task ID is required");
            sendJsonResponse(response, error);
            return;
        }

        try {
            int taskId = Integer.parseInt(pathInfo.substring(1));

            // Verify ownership
            Task existingTask = taskService.findById(taskId);
            if (existingTask == null || existingTask.getUserId() != userId) {
                sendUnauthorized(response);
                return;
            }

            // Delete task
            boolean deleted = taskService.delete(taskId);
            if (deleted) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Map<String, String> error = new HashMap<>();
                error.put("error", "Task not found");
                sendJsonResponse(response, error);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorResponse(response, e.getMessage());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid task ID");
            sendJsonResponse(response, error);
        }
    }

    private void sendJsonResponse(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(data));
        out.flush();
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        sendJsonResponse(response, error);
    }

    private void sendUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Unauthorized");
        sendJsonResponse(response, error);
    }
}