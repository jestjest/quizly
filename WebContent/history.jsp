<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="quizme.links.SummaryStat" %>
<%@ page import="java.util.List" %>
<%@ page import="quizme.links.QuizLink" %>
<%@ page import="java.text.SimpleDateFormat" %>

    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe History</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
</head>

<body>
	<jsp:include page="header.jsp"/>
	
	<div class="jumbotron">
      <div class="container text-center">
      	<h1>Your QuizMe history</h1>
      	<%
      	SummaryStat myStats = (SummaryStat) request.getAttribute("myAllSummaryStat");
      	out.println("<p><b>Your overall summary:</b></p>");
      	out.println("<p>Number of quizzes taken: <b>" + myStats.numberTaken + "</b></p>");
      	out.println("<p>Maximum score received: <b>" + myStats.maxScore + "%</b></p>");
      	out.println("<p>Minimum score received: <b>" + myStats.minScore + "%</b></p>");
      	out.println("<p>Average score received: <b>" + myStats.meanScore + "%</b></p>");
      	out.println("<br>");
      	out.println("<p>Fastest quiz time: <b>" + (myStats.minTime / 1000.0) + " seconds</b></p>");
      	out.println("<p>Slowest quiz time: <b>" + (myStats.maxTime / 1000.0) + " seconds</b></p>");
      	out.println("<p>Average quiz time: <b>" + (myStats.meanTime / 1000.0) + " seconds</b></p>");

      	%>
      	</div>
    </div>
    
    <div class="container text-center">
    	<h2>Created quizzes</h2>
    <%
    	List<QuizLink> createdQuizzes = (List<QuizLink>) request.getAttribute("myAllQuizzesCreated");
    	if (createdQuizzes.isEmpty()) {
    		out.println("<p>You have not created any quizzes.</p>");
    	} else {
    		for (QuizLink quiz : createdQuizzes) {
				String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateCreated());
				out.println(quiz.getQuizLink() + " created on " + date + 
						" by you has been taken " + quiz.getNumTaken() + " times.");
				out.println("<br>");
			}
    	}
    %>
    </div>
    
    <hr>
    
    <div class="container text-center">
    <h2>Taken quizzes</h2>
    <%
    	List<QuizLink> takenQuizzes = (List<QuizLink>) request.getAttribute("myAllQuizzesTaken");
    	if (takenQuizzes.isEmpty()) {
    		out.println("<p>You have not taken any quizzes.</p>");
    	} else {
    		for (QuizLink quiz : takenQuizzes) {
				String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateTaken());
				out.println("You took " + quiz.getQuizLink() + " on " + date +
						" and scored " + quiz.getScore() + "%.");
				out.println("<br>");
			}
    	}
    %>
    </div>
    
    <hr>
</body>
</html>