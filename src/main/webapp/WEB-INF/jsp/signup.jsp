<%@ page import="org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%--taglibs--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <meta content="text/html" charset="utf-8">
    <title>User::signup</title>
</head>
<body>
<p>Registration form</p>

<c:if test="${exception != null}">
    <p>${exception}</p>
</c:if>

<c:if test="${message!= null}">
    <p>${message}</p>
</c:if>

<form:form modelAttribute="user" method="POST" acceptCharset="utf8">
    <table>
        <tr>
            <td>
                <label>firstName</label>
            </td>

            <td>
                <form:input path="firstName"/>
            </td>

            <form:errors path="firstName"/>
        </tr>

        <tr>
            <td>
                <label>lastName</label>
            </td>
            <td>
                <form:input path="lastName"/>
            </td>
            <form:errors path="lastName"/>
        </tr>

        <tr>
            <td>
                <label>Email</label>
            </td>
            <td>
                <form:input path="email"/>
            </td>
            <form:errors path="email"/>
        </tr>

        <tr>
            <td>
                <label>Password</label>
            </td>
            <td>
                <form:input path="password" type="password"/>
            </td>
            <form:errors path="password"/>
        </tr>

        <tr>
            <td>
                <label>Confirm password</label>
            </td>
            <td>
                <form:input path="matchingPassword" type="password"/>
            </td>
            <form:errors/>
        </tr>

    </table>

    <button type="submit">Create account</button>
</form:form>

<br>

<a href="<c:url value="/login" />">Log in</a>

</body>
</html>
