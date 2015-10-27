<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Spring4 MVC - Base WebApp</title>
</head>
<body>
<h1>Gracias ${name} !</h1>
<br /><br />
<a href="/mywebapp/" >Welcome Page</a>
<br /><br />
<table border="0">
<tr><td colspan="2">Powered by:</td></tr>
<tr><td><img src="<c:url value="/resources/Java_logo.png" />" height="50" /></td><td><img src="<c:url value="/resources/Spring_Framework_logo.png" />" height="50" /></td></tr>
</table>
</body>
</html>