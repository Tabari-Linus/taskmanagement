<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/views/header.jsp"/>

<h2>My Tasks</h2>

<div class="row mb-3">
    <div class="col-md-6">
        <div class="btn-group" role="group">
            <a href="${pageContext.request.contextPath}/tasks" class="btn ${empty statusFilter ? 'btn-primary' : 'btn-outline-primary'}">All</a>
            <a href="${pageContext.request.contextPath}/tasks?status=PENDING" class="btn ${statusFilter eq 'PENDING' ? 'btn-primary' : 'btn-outline-primary'}">Pending</a>
            <a href="${pageContext.request.contextPath}/tasks?status=IN_PROGRESS" class="btn ${statusFilter eq 'IN_PROGRESS' ? 'btn-primary' : 'btn-outline-primary'}">In Progress</a>
            <a href="${pageContext.request.contextPath}/tasks?status=COMPLETED" class="btn ${statusFilter eq 'COMPLETED' ? 'btn-primary' : 'btn-outline-primary'}">Completed</a>
        </div>
    </div>
    <div class="col-md-6 text-right">
        <a href="${pageContext.request.contextPath}/tasks/new" class="btn btn-success">Add New Task</a>
    </div>
</div>

<c:choose>
    <c:when test="${empty tasks}">
        <div class="alert alert-info">
            No tasks found. Click the "Add New Task" button to create one.
        </div>
    </c:when>
    <c:otherwise>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="thead-dark">
                    <tr>
                        <th>Title</th>
                        <th>Due Date</th>
                        <th>Status</th>
                        <th>Priority</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="task" items="${tasks}">
                        <tr>
                            <td>${task.title}</td>
                            <td><fmt:formatDate value="${task.dueDate}" pattern="MM/dd/yyyy" /></td>
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
                                        <span class="badge badge-secondary">Low</span>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>
                                <div class="btn-group" role="group">
                                    <a href="${pageContext.request.contextPath}/tasks/edit/${task.id}" class="btn btn-sm btn-primary">Edit</a>
                                    <a href="${pageContext.request.contextPath}/tasks/delete/${task.id}" class="btn btn-sm btn-danger"
                                       onclick="return confirm('Are you sure you want to delete this task?')">Delete</a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/views/footer.jsp"/>