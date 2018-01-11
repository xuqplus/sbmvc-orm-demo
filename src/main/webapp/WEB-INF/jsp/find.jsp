<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/9/1
  Time: 16:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>find</title>
</head>
<body>

<%
    out.print("hello users<br>");
%>

<c:forEach items="${users}" var="i">
    id: ${i.id}, name: ${i.name}, pwd: ${i.pwd}<br>
</c:forEach>

<%
    out.print("hello user0<br>");
%>

<c:out value="${user0}"></c:out>
</body>
</html>
