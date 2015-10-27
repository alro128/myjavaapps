<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
    </head>
    <body>
        <h3>Enter Details</h3>
        <form:form method="POST" action="/mywebapp/modulo" modelAttribute="moduloForm">
        <input type="hidden" name="company" value="${company}"/>
        <input type="hidden" name="campaign" value="${campaign}"/>
             <table>
                <tr>
                    <td>Email</td>
                    <td><input type="text" name="email" /></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Recibir ofertas"/></td>
                </tr>
            </table>
        </form:form>
    </body>
</html>