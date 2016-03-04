<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="quizme.links.QuizSummaryInfo"%>    
<% QuizSummaryInfo quizSummary = (QuizSummaryInfo) request.getAttribute("quizSummaryInfo"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body>
	<jsp:include page="header.jsp"/>
	
	<div class="jumbotron">
		<div class="container">
			<h2><%=quizSummary.getName() %></h2>
			<b>Created by: <%=quizSummary.getCreatorUsername() %> on <%=quizSummary.getDateCreated() %></b>
			<h3>Description:<%=quizSummary.getDescription() %></h3>
			<h3>This quiz has been taken <%=quizSummary.getNumberTaken() %> times.</h3>
		</div>
	</div>
</body>
</html>