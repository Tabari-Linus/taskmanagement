<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<jsp:include page="/WEB-INF/views/header.jsp"/>

<div class="text-center mt-5">
    <h1 class="display-1">404</h1>
    <h2>Page Not Found</h2>
    <p>The page you are looking for does not exist.</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go to Home</a>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp"/>