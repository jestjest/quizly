<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="quizme.quizzes.*" %>
<%@ page import="quizme.links.*" %>
<%@ page import="java.util.List" %>
<%@ page import="quizme.quizzes.Achievement.AchievementGuidelinesData" %>

<% Quiz quiz = (Quiz) request.getSession().getAttribute("quiz"); %>    
<% QuizSummaryInfo quizSummaryInfo = (QuizSummaryInfo) request.getSession().getAttribute("quizSummaryInfo");%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe Quiz Results</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body>
	<jsp:include page="header.jsp"/>
	
	<div class="jumbotron">
		<div class="container text-center">
			<h2>Quiz Results for Quiz '<%=quizSummaryInfo.getName() %>'</h2>
			<h3>Score: <%=quiz.getScore() %>%</h3>
			<h3>Time: <%=quiz.getTime() / 1000.0 %> seconds</h3>
			
			 <%
        	List<AchievementGuidelinesData> newAchievements = (List<AchievementGuidelinesData>) request.getAttribute("achievements");        
        	if (newAchievements != null &&  newAchievements.size() != 0) {
	        	out.println("<h2>New achievements!</h2>");
	        	
	        	for (int i = 0; i < newAchievements.size(); i++) {
	        		AchievementGuidelinesData achievement = newAchievements.get(i);
	        		out.println("<img style='max-height: 100px; max-width: 100px;' src='" + achievement.pictureURL + "'>");
	        		out.println("<br>");
	        		out.println("<b>Name</b>: " + achievement.name);
	        		out.println("<br>");
	        		out.println("<b>Description</b>: " + achievement.description);
	        		out.println("<br><br>");
	        	}
        	}
        	%>
			
		</div>
	</div>

	<div class="container text-center">
		<h2>Your quiz results</h2>
		<%
		for (int i = 0; i < quiz.numOfQuestions(); i++) {
			Question question = quiz.getQuestion(i);
			question.answerSummary(out, i+1);
			out.println("<hr>");
		}
		%>	
	</div>
	
	<div class="container text-center">
		<h2>How well you did</h2>		
		<%
		SummaryStat userStats = quizSummaryInfo.mySummaryStat();
		SummaryStat allStats = quizSummaryInfo.allSummaryStat();
		out.println("<p>You've taken this quiz " + (userStats.numberTaken + 1) + " times now.<p>");
		out.println("<br>");
		out.println("<h3>Score</h3>");
		out.println("<p>Your all time highest is " + userStats.maxScore + "%</p>");
		out.println("<p>Your all time worst is " + userStats.minScore + "%</p>");
		out.println("<p>Your mean score is " + userStats.meanScore + "%</p>");
		
		out.println("<br>");
		out.println("<p>The all time highest is " + allStats.maxScore + "%</p>");
		out.println("<p>The all time worst is " + allStats.minScore + "%</p>");
		out.println("<p>The all time mean is " + allStats.meanScore + "%</p>");
		out.println("<br>");
		
		float difference = quiz.getScore() - userStats.maxScore;
		if (difference > 0) {
			out.println("<b><p>Good job! You set a personal best by " + difference + "%.</p></b>");
			
		} else if (quiz.getScore() - userStats.minScore > 0) {
			out.println("<b>Keep trying, at least this wasn't a personal worst score!</b>");
		} else {
			out.println("<b>Tough luck, try again for a higher score!</b>");
		}
		
		out.println("<br>");
		out.println("<h3>Time</h3>");
		out.println("<p>Your fastest completion was " + (userStats.minTime / 1000.0) + " seconds.</p>");
		out.println("<p>Your slowest completion was " + (userStats.maxTime / 1000.0) + " seconds.</p>");
		out.println("<p>Your mean completion was " + (userStats.meanTime / 1000.0) + " seconds.</p>");
		out.println("<br>");
		out.println("<p>The all time fastest completion was " + (allStats.minTime / 1000.0) + " seconds.</p>");
		out.println("<p>The all time slowest completion was " + (allStats.maxTime / 1000.0) + " seconds.</p>");
		out.println("<p>The all time mean completion was " + (allStats.meanTime / 1000.0) + " seconds.</p>");
		out.println("<br>");
					
		double timeDifference = (quiz.getTime() - userStats.minTime) / 1000.0;
		if (timeDifference < 0) {
			out.println("<b><p>Good job! You set a personal best by " + (-1 * timeDifference) + " seconds.</p></b>");
		} else if (userStats.minTime == 0.0) {
			out.println("<b><p>Good job! You set a personal best by " + timeDifference + " seconds.</p></b>");
		} else if (quiz.getTime() - userStats.maxTime < 0) {
			out.println("<b><p>Keep trying, at least this wasn't a personal worst time!</p></b>");
		} else {
			out.println("<b><p>Tough luck, try again for a faster time!</p></b>");
		}
		%>
	</div>

</body>
</html>