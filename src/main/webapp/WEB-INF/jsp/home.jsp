<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%--taglibs--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <meta content="text/html" charset="utf-8">
    <title>Home</title>
</head>
<body>
    <c:if test="${user != null}">
        <p>Welcome, ${user}</p>
        <c:url value="/logout" var="logoutUrl"/>
        <form:form action="${logoutUrl}" method="post">
            <button type="submit">log out</button>
        </form:form>
    </c:if>
</body>
</html>
