<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="quizme.quizzes.*" %>
<% Quiz quiz = (Quiz) request.getSession().getAttribute("quiz"); %>    
<% int questionIndex = Integer.parseInt(request.getParameter("questionIndex")); %>
<% Question question = quiz.getQuestion(questionIndex - 1); %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe Quiz Feedback</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body>
	<jsp:include page="header.jsp"/>
	
	<div class="jumbotron">
		<div class="container text-center">
		<h2>Feedback for question <%=questionIndex %></h2>
		<%question.answerSummary(out, questionIndex);%>
		
		<%
		if (questionIndex == quiz.numOfQuestions()) {
			out.println("<form action='QuizResultsServlet' method='POST'>");
        	out.println("<input type='submit' name='submit-quiz' value='Finish' class='btn btn-success'>");
			out.println("</form");				
		} else {
			out.println("<form action='take-multi-page-quiz.jsp' method='POST'>");
			out.println("<input type='hidden' name='questionIndex' value='" + (questionIndex+1) + "'>");
			out.println("<input type='submit' name='next-question' value='Next question' class='btn btn-primary'>");
			out.println("</form>");
		}
		%>
		</div>
		
	</div>
	
</body>
</html>