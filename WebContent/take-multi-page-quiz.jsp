<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="quizme.quizzes.*" %>
<%@ page import="quizme.links.*"%> 

<% Quiz quiz = (Quiz) request.getSession().getAttribute("quiz"); %>    
<% QuizSummaryInfo quizSummary = (QuizSummaryInfo) request.getSession().getAttribute("quizSummaryInfo"); %>

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
		<div class="container text-center">
			<h2>Quiz: <b><%=quizSummary.getName() %></b></h2>
			<p>Created by: <b><%=quizSummary.getCreatorUsername() %></b>.</p>
			<h3>Number of questions: <%=quiz.numOfQuestionsRemaining()%></h3>
			<h3><b>Go!</b></h3>
		</div>
	</div>
	
	<form action='UpdateQuizServlet' method='POST'>
		<%
			int questionIndex = Integer.parseInt(request.getParameter("questionIndex"));
			Question question = quiz.getQuestion(questionIndex - 1);
			out.println("Question " + questionIndex + "). ");
			question.show(out, questionIndex);
		%>
		
       	<input type='hidden' name='questionIndex' value='<%=questionIndex%>'>
		
		<%
		if (questionIndex == quiz.numOfQuestionsRemaining()) {
        	out.println("<input type='submit' name='submit-quiz' value='Finish' class='btn btn-success'>");
		} else {
			out.println("<input type='submit' name='next-question' value='Next question' class='btn btn-primary'>");
		}
		%>
	</form>

</body>
</html>