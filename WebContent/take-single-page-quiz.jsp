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
			<h1>Quiz: <b><%=quizSummary.getName() %></b></h1>
			<p>Created by: <b><%=quizSummary.getCreatorUsername() %></b>.</p>
			<h3>Number of questions: <%=quizSummary.numOfQuestions() %></h3>
			<h3><b>Go!</b></h3>
		</div>
	</div>
	
	<form action='QuizResultsServlet' method='POST'>
		<%
		for (int i = 0; i < quiz.numOfQuestions(); i++) {
			Question question = quiz.getQuestion(i);
			out.println("Question " + (i+1) + ". ");
			question.show(out);
			out.println("<hr>");
		}
		%>
	
        <input type="submit" name="submit-quiz" value="I'm done!" class="btn btn-primary">
	</form>

</body>
</html>