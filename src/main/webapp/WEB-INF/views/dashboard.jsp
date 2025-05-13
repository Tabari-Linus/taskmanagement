<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<jsp:useBean id="now" class="java.util.Date" />
<jsp:include page="/WEB-INF/views/header.jsp"/>

<div class="dashboard-container">
    <div class="row mb-4">
        <div class="col-md-12">
            <h2>Dashboard</h2>
            <p class="text-muted">Welcome back, ${sessionScope.user.fullName}! Here's an overview of your tasks.</p>
        </div>
    </div>

    <!-- Statistics Cards -->
    <div class="row mb-4">
        <div class="col-md-3">
            <div class="card stat-card">
                <div class="card-body">
                    <h5 class="card-title">Total Tasks</h5>
                    <p class="stat-number">${totalTasks}</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card stat-card pending-card">
                <div class="card-body">
                    <h5 class="card-title">Pending</h5>
                    <p class="stat-number">${statusCounts['PENDING']}</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card stat-card progress-card">
                <div class="card-body">
                    <h5 class="card-title">In Progress</h5>
                    <p class="stat-number">${statusCounts['IN_PROGRESS']}</p>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card stat-card completed-card">
                <div class="card-body">
                    <h5 class="card-title">Completed</h5>
                    <p class="stat-number">${statusCounts['COMPLETED']}</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Quick Actions and Upcoming Tasks -->
    <div class="row">
        <!-- Quick Actions -->
        <div class="col-md-4">
            <div class="card">
                <div class="card-header">
                    <h5>Quick Actions</h5>
                </div>
                <div class="card-body">
                    <div class="list-group">
                        <a href="${pageContext.request.contextPath}/tasks/new" class="list-group-item list-group-item-action">
                            <i class="fas fa-plus-circle"></i> Create New Task
                        </a>
                        <a href="${pageContext.request.contextPath}/tasks?status=PENDING" class="list-group-item list-group-item-action">
                            <i class="fas fa-clock"></i> View Pending Tasks
                        </a>
                        <a href="${pageContext.request.contextPath}/tasks?status=IN_PROGRESS" class="list-group-item list-group-item-action">
                            <i class="fas fa-spinner"></i> View In-Progress Tasks
                        </a>
                        <a href="${pageContext.request.contextPath}/tasks?status=COMPLETED" class="list-group-item list-group-item-action">
                            <i class="fas fa-check-circle"></i> View Completed Tasks
                        </a>
                    </div>
                </div>
            </div>

            <!-- Priority Distribution -->
            <div class="card mt-4">
                <div class="card-header">
                    <h5>Priority Distribution</h5>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span><span class="badge badge-danger mr-2">High</span> Priority</span>
                            <span class="badge badge-pill badge-secondary">${priorityCounts['HIGH']}</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span><span class="badge badge-warning mr-2">Medium</span> Priority</span>
                            <span class="badge badge-pill badge-secondary">${priorityCounts['MEDIUM']}</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <span><span class="badge badge-success mr-2">Low</span> Priority</span>
                            <span class="badge badge-pill badge-secondary">${priorityCounts['LOW']}</span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <!-- Upcoming Tasks -->
        <div class="col-md-8">
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5>Upcoming Tasks</h5>
                    <c:if test="${overdueTasks > 0}">
                        <span class="badge badge-danger">
                            ${overdueTasks} Overdue Task(s)
                        </span>
                    </c:if>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty upcomingTasks}">
                            <div class="alert alert-info">
                                No upcoming tasks. Click "Create New Task" to add one.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Title</th>
                                            <th>Due Date</th>
                                            <th>Status</th>
                                            <th>Priority</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="task" items="${upcomingTasks}">
                                            <tr class="${(task.dueDate != null && task.dueDate.before(now)) ? 'table-danger' : ''}">
                                                <td>${task.title}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${task.dueDate != null}">
                                                            <fmt:formatDate value="${task.dueDate}" pattern="MMM dd, yyyy" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">No due date</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${task.status eq 'PENDING'}">
                                                            <span class="badge badge-warning">Pending</span>
                                                        </c:when>
                                                        <c:when test="${task.status eq 'IN_PROGRESS'}">
                                                            <span class="badge badge-info">In Progress</span>
                                                        </c:when>
                                                        <c:when test="${task.status eq 'COMPLETED'}">
                                                            <span class="badge badge-success">Completed</span>
                                                        </c:when>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${task.priority eq 'HIGH'}">
                                                            <span class="badge badge-danger">High</span>
                                                        </c:when>
                                                        <c:when test="${task.priority eq 'MEDIUM'}">
                                                            <span class="badge badge-warning">Medium</span>
                                                        </c:when>
                                                        <c:when test="${task.priority eq 'LOW'}">
                                                            <span class="badge badge-success">Low</span>
                                                        </c:when>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <div class="btn-group" role="group">
                                                        <a href="${pageContext.request.contextPath}/tasks/edit/${task.id}" class="btn btn-sm btn-primary">Edit</a>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            <div class="text-right mt-2">
                                <a href="${pageContext.request.contextPath}/tasks" class="btn btn-outline-primary">View All Tasks</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp"/>