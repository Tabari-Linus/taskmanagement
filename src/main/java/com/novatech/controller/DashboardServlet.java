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
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
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

        User user = (User) session.getAttribute("user");
        int userId = user.getId();

        try {

            List<Task> allTasks = taskService.findByUserId(userId);


            Map<String, Integer> statusCounts = new HashMap<>();
            statusCounts.put("PENDING", 0);
            statusCounts.put("IN_PROGRESS", 0);
            statusCounts.put("COMPLETED", 0);

            Map<String, Integer> priorityCounts = new HashMap<>();
            priorityCounts.put("HIGH", 0);
            priorityCounts.put("MEDIUM", 0);
            priorityCounts.put("LOW", 0);

            for (Task task : allTasks) {

                String status = task.getStatus();
                statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);


                String priority = task.getPriority();
                priorityCounts.put(priority, priorityCounts.getOrDefault(priority, 0) + 1);
            }


            List<Task> upcomingTasks = allTasks.stream()
                    .filter(task -> !task.getStatus().equals("COMPLETED"))
                    .sorted((t1, t2) -> {
                        // Handle null dates in sorting
                        if (t1.getDueDate() == null && t2.getDueDate() == null) return 0;
                        if (t1.getDueDate() == null) return 1; // Null dates go at the end
                        if (t2.getDueDate() == null) return -1;
                        return t1.getDueDate().compareTo(t2.getDueDate());
                    })
                    .limit(5)
                    .collect(Collectors.toList());


            Date today = new Date();
            long overdueTasks = allTasks.stream()
                    .filter(task -> !task.getStatus().equals("COMPLETED"))
                    .filter(task -> task.getDueDate() != null && task.getDueDate().before(today))
                    .count();


            request.setAttribute("statusCounts", statusCounts);
            request.setAttribute("priorityCounts", priorityCounts);
            request.setAttribute("upcomingTasks", upcomingTasks);
            request.setAttribute("totalTasks", allTasks.size());
            request.setAttribute("overdueTasks", overdueTasks);


            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error", e);
        }
    }
}