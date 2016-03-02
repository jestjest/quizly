<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="quizme.database.*" %>
<%@ page import="quizme.links.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.sql.Timestamp" %>

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
        <h1>Announcements</h1>
        <%
       		Map<Timestamp, String> announcements = (Map<Timestamp, String>) request.getAttribute("announcements");
        	for (Map.Entry<Timestamp, String> entry : announcements.entrySet()) {
				String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(entry.getKey());
        		out.println(date + " : " + entry.getValue());
        		out.println("<br>");
        	}
        %>
        
      </div>
    </div>
    
    <hr>

	<div class="container">
		<h2>Popular quizzes</h2>
			<div class="row">
				<%
					List<QuizLink> popularQuizzes = (List<QuizLink>) request.getAttribute("popularQuizzes");
					for (QuizLink quiz : popularQuizzes) {
						String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateCreated());
						out.println(quiz.getQuizLink() + " created on " + date + " by " + quiz.getCreatorLink() + 
								" has been taken " + quiz.getNumTaken() + " times.");
						out.println("<br>");
					}
				%>
		    </div>
		</div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Recently created quizzes</h2>
			<div class="row">
				<%
					List<QuizLink> recentQuizzes = (List<QuizLink>) request.getAttribute("allRecentQuizzesCreated");
					for (QuizLink quiz : recentQuizzes) {
						String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateCreated());
						out.println(quiz.getQuizLink() + " created on " + date + " by " + quiz.getCreatorLink() +
								" has been taken " + quiz.getNumTaken() + " times.");
						out.println("<br>");
					}
				%>
		    </div>
		</div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Your quiz creation activity</h2>
			<div class="row">
				<%
					List<QuizLink> userCreatedQuizzes = (List<QuizLink>) request.getAttribute("myRecentQuizzesCreated");
					for (QuizLink quiz : userCreatedQuizzes) {
						String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateCreated());
						out.println(quiz.getQuizLink() + " created on " + date + 
								" by you has been taken " + quiz.getNumTaken() + " times.");
						out.println("<br>");
					}
				%>
		    </div>
		</div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Achievements</h2>
			<div class="row">
				<%
					List<AchievementLink> achievements = (List<AchievementLink>) request.getAttribute("myAchievements");
					for (AchievementLink achievement : achievements) {
						String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(achievement.getDateAchieved());
						out.println("You have achieved '" + achievement.getName() + "' on " + achievement.getDateAchieved() + ".");
						out.println("<br>");
					}
				%>
		    </div>
		</div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Unread Messages</h2>
			<div class="row">
				Testing messages
		    </div>
		</div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Friends' Recent Activity</h2>
			<div class="row">
				Testing activity
		    </div>
		</div>
	</div> 
	
	<hr>          
</body>
</html>