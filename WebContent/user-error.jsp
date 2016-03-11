<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe User Page Error</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
</head>

<body>
	<jsp:include page="header.jsp"/>
	<% String username = (String) request.getAttribute("pageUsername");  %>	
	<div class='jumbotron'>
      	<div class='container text-center'> 
      	<br>
      	<div class="alert alert-danger" role="alert">
		  <span class="sr-only">Error:</span>
		  <h2>The user page for '<%=username %>' was not found.</h2>
		</div>
		</div>
	</div>
		      	

</body>
</html>