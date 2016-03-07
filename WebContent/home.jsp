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
      <div class="container text-center">
        <h1>Announcements</h1>
        <%
        List<AnnouncementLink> announcements = (List<AnnouncementLink>) request.getAttribute("announcements");
        if (announcements.isEmpty())
        	out.println("No announcements");
        	
       	for (AnnouncementLink announcement : announcements) {
			String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(announcement.date());
       		out.println(date);
       		out.println("<br>");
       		out.println("Subject: <b>" + announcement.subject() + "</b>");
       		out.println("<br>");
       		out.println("Message: <b>" + announcement.content() + "</b>");
       		out.println("<br>");
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
					if (popularQuizzes.isEmpty()) 
						out.println("No popular quizzes.");
						
					for (QuizLink quiz : popularQuizzes) {
						String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateCreated());
						out.println(quiz.getQuizLink() + " created on " + date + " by " + quiz.getCreatorLink() + 
								" has been taken " + quiz.getNumTaken() + " times.");
						out.println("<br>");
					}
				%>
		    </div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Recently created quizzes</h2>
			<div class="row">
				<%
					List<QuizLink> recentQuizzes = (List<QuizLink>) request.getAttribute("allRecentQuizzesCreated");
					if (recentQuizzes.isEmpty())
						out.println("No recently created quizzes.");
				
					for (QuizLink quiz : recentQuizzes) {
						String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateCreated());
						out.println(quiz.getQuizLink() + " created on " + date + " by " + quiz.getCreatorLink() +
								" has been taken " + quiz.getNumTaken() + " times.");
						out.println("<br>");
					}
				%>
		    </div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Your quiz creation activity</h2>
			<div class="row">
				<%
					List<QuizLink> userCreatedQuizzes = (List<QuizLink>) request.getAttribute("myRecentQuizzesCreated");
					if (userCreatedQuizzes.isEmpty()) 
						out.println("No quizzes created by you.");
					
					for (QuizLink quiz : userCreatedQuizzes) {
						String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateCreated());
						out.println(quiz.getQuizLink() + " created on " + date + 
								" by you has been taken " + quiz.getNumTaken() + " times.");
						out.println("<br>");
					}
				%>
		    </div>
	</div> 
	
	<hr>
	
	<div class="container">
		<h2>Your recent quiz taking activity</h2>
			<div class="row">
				<%
					List<QuizLink> userTakenQuizzes = (List<QuizLink>) request.getAttribute("myRecentQuizzesTaken");
					if (userTakenQuizzes.isEmpty()) 
						out.println("No quizzes taken by you.");
					
					for (QuizLink quiz : userTakenQuizzes) {
						String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateTaken());
						out.println("You took " + quiz.getQuizLink() + " on " + date +
								" and scored " + quiz.getScore() + "%.");
						out.println("<br>");
					}
				%>
		    </div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Achievements</h2>
			<div class="row">
				<%
					List<AchievementLink> achievements = (List<AchievementLink>) request.getAttribute("myAchievements");
					if (achievements.isEmpty()) 
						out.println("No achievements earned yet.");
					for (AchievementLink achievement : achievements) {
						String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(achievement.getDateAchieved());
						out.println("You have achieved '" + achievement.getName() + "' on " + achievement.getDateAchieved() + ".");
						out.println("<br>");
					}
				%>
		    </div>
	</div> 
	
    <hr>
	
	<div class="container">
		<h2>Unread Messages</h2>
			<div class="row">
		    <%
				List<MessageLink> messages = (List<MessageLink>) request.getAttribute("myUnseenMessages");
		    	if (messages.isEmpty()) 
		    		out.println("No unread messages.");
		    	
				for (MessageLink message : messages) {
					String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(message.getDate());
					if (message.getType() == MessageLink.MType.TEXT)
						out.println(date + ": " + message.getSenderLink() + " has sent you a note: '" + message.getSubject() + "'");
					else if (message.getType() == MessageLink.MType.FRIENDSHIP)
						out.println(date + ": " + message.getSenderLink() + "wants to be friends!");
					else if (message.getType() == MessageLink.MType.CHALLENGE) {
						out.println(date + ": " + message.getSenderLink() + " is challenging you to take this quiz: " + message.getSubject());
						out.println("The challenger has a best score of " + message.getContent() + ".");
					}
					out.println("<br>");
				}
			%>
		    </div>
	</div>
	
    <hr>
	
	<div class="container">
		<h2>Friends' recent quizzes created</h2>
			<div class="row">
				<%
					List<QuizLink> friendsCreatedQuizzes = (List<QuizLink>) request.getAttribute("friendsRecentQuizzesCreated");
					if (friendsCreatedQuizzes.isEmpty()) 
						out.println("No quizzes created by friends.");
					
					for (QuizLink quiz : friendsCreatedQuizzes) {
						String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateCreated());
						out.println(quiz.getQuizLink() + " created on " + date + " by " + quiz.getCreatorLink() +
								" has been taken " + quiz.getNumTaken() + " times.");
						out.println("<br>");
					}
				%>
		    </div>
	</div> 
	
	<hr>
	
	<div class="container">
		<h2>Friends' recent quizzes taken</h2>
			<div class="row">
				<%
					List<QuizLink> friendsQuizzesTaken = (List<QuizLink>) request.getAttribute("friendsRecentQuizzesTaken");
					if (friendsQuizzesTaken.isEmpty()) 
						out.println("No quizzes taken by friends.");
					
					for (QuizLink quiz : friendsQuizzesTaken) {
						String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateTaken());
						out.println(quiz.getTakerLink() + " took " + quiz.getQuizLink() + " on " + date +
								" and scored " + quiz.getScore() + "%.");
						out.println("<br>");
					}
				%>
		    </div>
	</div> 
	
	<hr>
	
	<div class="container">
		<h2>Friends' recent achievements</h2>
			<div class="row">
			<%
				List<AchievementLink> friendsAchievements = (List<AchievementLink>) request.getAttribute("friendsRecentAchievements");
				if (friendsAchievements.isEmpty()) 
					out.println("No recent friends achievements.");
				
				for (AchievementLink achievement : friendsAchievements) {
					String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(achievement.getDateAchieved());
					out.println(achievement.getUsername() + " achieved '" + achievement.getName() + "' on " + date);
					out.println("<br>");
				}
			%>
		    </div>
	</div> 
	
	<hr>          
</body>
</html>