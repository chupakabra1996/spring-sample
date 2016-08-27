<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%--taglibs--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <meta content="text/html" charset="utf-8">
    <title>User::Login</title>
</head>
<body>

    <c:if test="${message != null}">
        ${message}
    </c:if>


    <c:url value="/login" var="loginUrl"/>

    <form:form name="f" action="${loginUrl}" method="post">
        <fieldset>
            <legend>Please Login</legend>

            <c:if test="${param.error != null}">
                ${SPRING_SECURITY_LAST_EXCEPTION}
            </c:if>

            <br/>

            <c:if test="${param.logout != null}">
                <div class="alert alert-success">
                    You have been logged out.
                </div>
            </c:if>

            <label for="username">Username</label>
            <input type="text" id="username" name="username"/>
            <label for="password">Password</label>
            <input type="password" id="password" name="password"/>

            <label>
                Remember me
                <input type="checkbox" name="remember-me"/>
            </label>

            <div class="form-actions">
                <button type="submit" class="btn">Log in</button>
            </div>
        </fieldset>
    </form:form>
</body>
</html>
