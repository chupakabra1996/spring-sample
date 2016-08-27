<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%--taglibs--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta content="text/html" charset="utf-8">
    <title>Confirm your email address</title>
</head>
<body>
    <c:if test="${message}">
        <p>${message}</p>
    </c:if>
    <a href="<c:url value='/login'/>">Log in</a>
</body>
</html>
