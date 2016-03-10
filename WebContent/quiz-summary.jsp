<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="quizme.links.*"%> 
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="quizme.User"%>
<% QuizSummaryInfo quizSummary = (QuizSummaryInfo) request.getSession().getAttribute("quizSummaryInfo"); %>
<% User user = (User) request.getSession().getAttribute("user"); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe Quiz Summary</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	
	<style>
	#ordering {
		margin: 0 auto;
		width: 150px;
	}
	</style>
</head>
<body>
	<jsp:include page="header.jsp"/>
	
	<div class="jumbotron">
		<div class="container text-center">
			<h1>Quiz: <b><%=quizSummary.getName() %></b></h1>
			<p>Created by: <b><%=quizSummary.getCreatorUsername() %></b></p>
			<p>Last modified on <b><%=quizSummary.getModifiedDate() %></b></p>
			<h3>Description: <%=quizSummary.getDescription() %></h3>
			<h3>Number of questions: <%=quizSummary.numOfQuestions() %></h3>
			<h3><%=quizSummary.onePage() ? "One page" : "Multiple pages" %></h3>
			<h3><%=quizSummary.randomOrder() ? "Random question order" : "Fixed question order" %></h3>
			<h3><%=quizSummary.immediateCorrection() ? "Immediate feedback" : "No immediate feedback" %></h3>
			<br>
			
			<form class="take-quiz" action='TakeQuizServlet' method='POST'>
				<%
				if (quizSummary.hasPractice()) {
					out.println("<label>Practice mode</label>");
					out.println("<input type='checkbox' name='practice-mode' value='practice-mode'>");
					out.println("<br>");
				}
				%>	
				<input type='submit' name='take-quiz' value="Take the quiz!" class='btn btn-primary'>
	        </form>
	        
	        <%
	        if (quizSummary.getCreatorUsername().equals(user.getName())) {
	        	out.println("<br>");
	        	out.println("<form class='edit-quiz' action='EditQuizServlet' method='GET'>");
	        	out.println("<input type='submit' name='edit-quiz' value='Edit quiz' class='btn btn-info'>");
	        	out.println("</form>");
	        }
	        %>
		</div>
	</div>
	
	<div class="container text-center">
		<h2>Challenge a friend!</h2>
		<form action='ChallengeFriendServlet' method='POST'>
			<label for='friend'>Friend name</label>
			<input type='text' name='friend' id='friend'>
		</form>
	</div>
	
   	<% 
   	if (request.getAttribute("error") != null) {
   		out.println("<div class='alert alert-danger text-center'>");
   		out.println(request.getAttribute("error"));
   		out.println("</div>");
   		request.setAttribute("error", "");
   	}
   	%>
	
	<hr>
	
	<div class="container text-center">
		<h2>Your past performances</h2>
		<form class='order-summary' action='QuizSummaryServlet' method='POST'>
			<div class="form-group">
				<label for="ordering">Order your performances by:</label>
				<select class="form-control" name='order' id="ordering">
				    <option value='0'>Most recent</option>
				    <option value='1'>Least recent</option>
				    <option value='2'>Highest score</option>
				    <option value='3'>Lowest score</option>
				    <option value='4'>Slowest performance</option>
				    <option value='5'>Fastest performance</option>
			    </select>
			    <input type='hidden' name='quizID' value='<%=quizSummary.getQuizID()%>'>
			    <br>
			    <input type='submit' name='sort-performances' value='Sort my performances'>
			</div>
		</form>
	
	<%
	List<Performance> myPerformance = quizSummary.getMyPerformances();
	if (myPerformance.isEmpty()) {
		out.println("You have not taken this quiz yet.");
	} else {
		for (Performance performance : myPerformance) {
			String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(performance.dateTaken);
			out.println("<p>On " + date + ", you scored " + performance.score + "% in " + (performance.time / 1000.0) + " seconds.</p>");
		}
	}
	%>
	</div>
	
	<hr>
	
	<div class='container text-center'>
	<h2>Top all time performances</h2>
	<%
	List<Performance> bestPerformances = quizSummary.getBestPerformances();
	if (bestPerformances.isEmpty()) {
		out.println("No one has taken this quiz yet.");
	} else {
		for (Performance performance : bestPerformances) {
			String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(performance.dateTaken);
			out.println("<p>On " + date + ", " + performance.username + " scored " + performance.score + "% in " + (performance.time / 1000.0) + " seconds.</p>");
		}
	}
	%>
	</div>
	
	<hr>
	
	<div class='container text-center'>
		<h2>Top performances in the last day</h2>
		<%
		List<Performance> topDayPerformances = quizSummary.getTopDayPerformances();
		if (topDayPerformances.isEmpty()) {
			out.println("No one has taken this quiz today yet.");
		} else {
			for (Performance performance : topDayPerformances) {
				String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(performance.dateTaken);
				out.println("<p>On " + date + ", " + performance.username + " scored " + performance.score + "% in " + (performance.time / 1000.0) + " seconds.</p>");
			}
		}
		%>
	</div>
	
	<hr>
	
	<div class='container text-center'>
		<h2>Most recent performances</h2>
		<%
		List<Performance> recentPerformances = quizSummary.getRecentPerformances();
		if (recentPerformances.isEmpty()) {
			out.println("No one has recently taken this quiz.");
		} else {
			for (Performance performance : recentPerformances) {
				String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(performance.dateTaken);
				out.println("<p>On " + date + ", " + performance.username + " scored " + performance.score + "% in " + (performance.time / 1000.0) + " seconds.</p>");
			}
		}
		%>
	</div>
	<hr>
</body>
</html>