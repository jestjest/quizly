<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.util.List" %>
<%@ page import="quizme.links.*" %>
<%@ page import="quizme.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe Quiz Directory</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
</head>

<body>
	<jsp:include page="header.jsp"/>
	<%
	List<QuizLink> allQuizzes = (List<QuizLink>) request.getAttribute("allQuizzes");
	%>
	
	<div class="jumbotron">
		<div class="container text-center">
			<h2>Quiz directory</h2>
			<h3>Total number of quizzes:<b> <%=allQuizzes.size() %></b></h3>
		</div>
	</div>

	<div class='container text-center'>
	<%
	if (allQuizzes.isEmpty()) {
		out.println("<p>There are no quizzes.</p>");
	} else {
		for (QuizLink quiz : allQuizzes) {
			String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateCreated());
			out.println(quiz.getQuizLink() + " created on " + date + " by " + quiz.getCreatorLink() + 
					" has been taken " + quiz.getNumTaken() + " times.");
			out.println("<br>");
		}
	}
	%>
	</div>
	<hr>
	
</body>
</html>