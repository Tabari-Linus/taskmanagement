<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<jsp:include page="/WEB-INF/views/header.jsp"/>

<h2>${empty task.id ? 'Add New Task' : 'Edit Task'}</h2>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger" role="alert">
        ${errorMessage}
    </div>
</c:if>

<div class="card">
    <div class="card-body">
        <form action="${pageContext.request.contextPath}/tasks" method="post">
            <c:if test="${not empty task.id}">
                <input type="hidden" name="id" value="${task.id}">
            </c:if>

            <div class="form-group">
                <label for="title">Title</label>
                <input type="text" class="form-control" id="title" name="title" value="${task.title}" required>
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea class="form-control" id="description" name="description" rows="3">${task.description}</textarea>
            </div>

            <div class="form-group">
                <label for="dueDate">Due Date</label>
                <input type="date" class="form-control" id="dueDate" name="dueDate"
                       value="<fmt:formatDate value="${task.dueDate}" pattern="yyyy-MM-dd" />" required>
            </div>

            <div class="form-group">
                <label for="status">Status</label>
                <select class="form-control" id="status" name="status" required>
                    <option value="PENDING" ${task.status eq 'PENDING' || empty task.status ? 'selected' : ''}>Pending</option>
                    <option value="IN_PROGRESS" ${task.status eq 'IN_PROGRESS' ? 'selected' : ''}>In Progress</option>
                    <option value="COMPLETED" ${task.status eq 'COMPLETED' ? 'selected' : ''}>Completed</option>
                </select>
            </div>

            <div class="form-group">
                <label for="priority">Priority</label>
                <select class="form-control" id="priority" name="priority" required>
                    <option value="LOW" ${task.priority eq 'LOW' ? 'selected' : ''}>Low</option>
                    <option value="MEDIUM" ${(task.priority eq 'MEDIUM' || empty task.priority) ? 'selected' : ''}>Medium</option>
                    <option value="HIGH" ${task.priority eq 'HIGH' ? 'selected' : ''}>High</option>
                </select>
            </div>

            <div class="form-group">
                <button type="submit" class="btn btn-primary">Save Task</button>
                <a href="${pageContext.request.contextPath}/tasks" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp"/>