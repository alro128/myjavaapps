<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Bootstrap 3, from LayoutIt!</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">

	<!--link rel="stylesheet/less" href="<c:url value="/resources/less/bootstrap.less"/>" type="text/css" /-->
	<!--link rel="stylesheet/less" href="<c:url value="/resources/less/responsive.less"/>" type="text/css" /-->
	<!--script src="<c:url value="/resources/js/less-1.3.3.min.js"/>"></script-->
	<!--append ‘#!watch’ to the browser URL, then refresh the page. -->
	
	<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet">
	<link href="<c:url value="/resources/css/style.css"/>" rel="stylesheet">

  <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
    <script src="<c:url value="/resources/js/html5shiv.js"/>"></script>
  <![endif]-->

  <!-- Fav and touch icons -->
  <link rel="apple-touch-icon-precomposed" sizes="144x144" href="<c:url value="/resources/img/apple-touch-icon-144-precomposed.png"/>">
  <link rel="apple-touch-icon-precomposed" sizes="114x114" href="<c:url value="/resources/img/apple-touch-icon-114-precomposed.png"/>">
  <link rel="apple-touch-icon-precomposed" sizes="72x72" href="<c:url value="/resources/img/apple-touch-icon-72-precomposed.png"/>">
  <link rel="apple-touch-icon-precomposed" href="<c:url value="/resources/img/apple-touch-icon-57-precomposed.png"/>">
  <link rel="shortcut icon" href="<c:url value="/resources/img/favicon.png"/>">
  
	<script type="text/javascript" src="<c:url value="/resources/js/jquery.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/js/scripts.js"/>"></script>
</head>

<body>
<div class="container">
	<div class="row clearfix">
		<div class="col-md-12 column">
			<div class="jumbotron">
				<h1>
					Campaña 1 <img src="<c:url value="/resources/Java_logo.png"/>" height="80" />
				</h1>
				<p>
					This is a template for a simple marketing ad for company 1. It includes a large callout called the hero unit and three supporting pieces of content. Use it as a starting point to create something more unique.
				</p>
        <form:form method="POST" action="/mywebapp/modulo" modelAttribute="moduloForm">
        <input type="hidden" name="company" value="${company}"/>
        <input type="hidden" name="campaign" value="${campaign}"/>
				<div class="form-group">
					 <label for="inputEmail3" class="col-sm-2 control-label">Email</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="email" />
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						 <button type="submit" class="btn btn-default">Sign in</button>&nbsp;<a class="btn btn-primary btn-large" href="#">Learn more</a>
					</div>
				</div>
        </form:form>		
			</div>

		</div>
	</div>
</div>
</body>
</html>