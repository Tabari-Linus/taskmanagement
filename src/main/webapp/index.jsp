<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:if test="${not empty sessionScope.user}">
    <c:redirect url="/tasks"/>
</c:if>

<jsp:include page="/WEB-INF/views/header.jsp"/>

<div class="container d-flex justify-content-center align-items-center" style="min-height: 75vh;">
    <div class="jumbotron text-center shadow-lg" style="width: 100%; max-width: 800px; transition: transform 0.3s ease, box-shadow 0.3s ease;">
        <h1 class="display-4 font-weight-bold">Welcome to NovaTech Mgt System</h1>
        <p class="lead">A simple and efficient way to manage your tasks</p>
        <hr class="my-4">
        <p>Get started.</p>
        <div class="mt-4">
            <a class="btn btn-primary btn-lg mx-2 hover-btn" href="${pageContext.request.contextPath}/login" role="button"
               style="transition: transform 0.2s, box-shadow 0.2s;">Login</a>
            <a class="btn btn-secondary btn-lg mx-2 hover-btn" href="${pageContext.request.contextPath}/register" role="button"
               style="transition: transform 0.2s, box-shadow 0.2s;">Register</a>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/footer.jsp"/>