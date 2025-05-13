package com.novatech.controller;

import com.novatech.model.Task;
import com.novatech.model.User;
import com.novatech.service.TaskService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "TaskServlet", urlPatterns = {"/tasks", "/tasks/*"})
public class TaskServlet extends HttpServlet {
    private TaskService taskService;

    @Override
    public void init() throws ServletException {
        super.init();
        taskService = new TaskService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        User user = (User) session.getAttribute("user");
        int userId = user.getId();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {

                String statusFilter = request.getParameter("status");
                List<Task> tasks;

                if (statusFilter != null && !statusFilter.isEmpty()) {
                    tasks = taskService.findByStatus(userId, statusFilter);
                    request.setAttribute("statusFilter", statusFilter);
                } else {
                    tasks = taskService.findByUserId(userId);
                }

                request.setAttribute("tasks", tasks);
                request.getRequestDispatcher("/WEB-INF/views/task-list.jsp").forward(request, response);
            } else if (pathInfo.equals("/new")) {

                request.getRequestDispatcher("/WEB-INF/views/task-form.jsp").forward(request, response);
            } else if (pathInfo.matches("/edit/\\d+")) {

                int taskId = Integer.parseInt(pathInfo.substring(pathInfo.lastIndexOf("/") + 1));
                Task task = taskService.findById(taskId);

                if (task != null && task.getUserId() == userId) {
                    request.setAttribute("task", task);
                    request.getRequestDispatcher("/WEB-INF/views/task-form.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else if (pathInfo.matches("/delete/\\d+")) {

                int taskId = Integer.parseInt(pathInfo.substring(pathInfo.lastIndexOf("/") + 1));
                Task task = taskService.findById(taskId);

                if (task != null && task.getUserId() == userId) {
                    taskService.delete(taskId);
                }

                response.sendRedirect(request.getContextPath() + "/tasks");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getId();

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String dueDateStr = request.getParameter("dueDate");
        String status = request.getParameter("status");
        String priority = request.getParameter("priority");
        String taskIdStr = request.getParameter("id");


        if (title == null || title.trim().isEmpty() ||
                dueDateStr == null || dueDateStr.trim().isEmpty() ||
                status == null || status.trim().isEmpty()) {

            request.setAttribute("errorMessage", "Title, due date, and status are required");


            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setStatus(status);
            task.setPriority(priority != null ? priority : Task.PRIORITY_MEDIUM);

            if (taskIdStr != null && !taskIdStr.isEmpty()) {
                task.setId(Integer.parseInt(taskIdStr));
            }

            request.setAttribute("task", task);
            request.getRequestDispatcher("/WEB-INF/views/task-form.jsp").forward(request, response);
            return;
        }

        try {
            Task task = new Task();
            task.setTitle(title);
            task.setDescription(description);
            task.setDueDate(Date.valueOf(dueDateStr));
            task.setStatus(status);
            task.setPriority(priority != null ? priority : Task.PRIORITY_MEDIUM);
            task.setUserId(userId);

            if (taskIdStr != null && !taskIdStr.isEmpty()) {

                int taskId = Integer.parseInt(taskIdStr);
                task.setId(taskId);


                Task existingTask = taskService.findById(taskId);
                if (existingTask == null || existingTask.getUserId() != userId) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                taskService.update(task);
            } else {
                taskService.create(task);
            }

            response.sendRedirect(request.getContextPath() + "/tasks");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error", e);
        } catch (IllegalArgumentException e) {

            request.setAttribute("errorMessage", "Invalid date format");
            request.getRequestDispatcher("/WEB-INF/views/task-form.jsp").forward(request, response);
        }
    }
}