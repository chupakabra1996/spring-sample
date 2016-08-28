<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%--taglibs--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <meta content="text/html" charset="utf-8">
    <title>Resend account activation token</title>
</head>
<body>

    <c:if test="${message != null}">
        <p>${message}</p>
    </c:if>

    <p>Resend token</p>

    <c:url value="/user/account/resendToken" var="resendUrl" />

    <form:form id="resendForm" action="${resendUrl}" method="post" acceptCharset="utf-8">
        <input name="email" id="email" type="text" placeholder="Your email address ...">
        <button type="submit">Resend token</button>
    </form:form>

</body>
</html>
