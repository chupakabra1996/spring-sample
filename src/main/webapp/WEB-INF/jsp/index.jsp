<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page session="false" %>

<%--taglibs--%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Welcome</title>
</head>
<body>
<p>Welcome, ${user}</p>

<c:choose>

    <c:when test="${user.equals('Guest')}">
        <c:url value="/login" var="loginUrl"/>
        <p>Please, log in <a href="${loginUrl}">here</a></p>

    </c:when>

    <c:otherwise>
        <c:url value="/logout" var="logoutUrl"/>
        <form:form action="${logoutUrl}" method="post">
            <button type="submit">log out</button>
        </form:form>
    </c:otherwise>
</c:choose>

</body>
</html>
