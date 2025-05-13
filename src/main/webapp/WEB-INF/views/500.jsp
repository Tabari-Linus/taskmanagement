<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<jsp:include page="/WEB-INF/views/header.jsp"/>

<div class="text-center mt-5">
    <h1 class="display-1">500</h1>
    <h2>Internal Server Error</h2>
    <p>Oops! Something went wrong on our end.</p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Go to Home</a>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp"/>