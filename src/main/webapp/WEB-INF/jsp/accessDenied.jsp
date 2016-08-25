<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%--taglibs--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta content="text/html" charset="utf-8">
    <title>Access Denied 403</title>
</head>
<body>
        <p style="color:darkred; font-size: 18pt; text-align: center; margin-top: 200px">
            Access Denied
        </p>

        <p>Exception : ${param.error}</p>
</body>
</html>
