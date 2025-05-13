<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<jsp:include page="/WEB-INF/views/header.jsp"/>

<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card">
            <div class="card-header">
                <h4>Register</h4>
            </div>
            <div class="card-body">
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger" role="alert">
                        ${errorMessage}
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/register" method="post">
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" class="form-control" id="username" name="username" value="${param.username}" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" class="form-control" id="password" name="password" required>
                    </div>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" class="form-control" id="email" name="email" value="${param.email}" required>
                    </div>
                    <div class="form-group">
                        <label for="fullName">Full Name</label>
                        <input type="text" class="form-control" id="fullName" name="fullName" value="${param.fullName}" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Register</button>
                </form>

                <div class="mt-3">
                                    <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Login here</a></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <jsp:include page="/WEB-INF/views/footer.jsp"/>