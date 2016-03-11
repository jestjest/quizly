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
	<title>QuizMe Admin Panel</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
	<style>
	input {
	width: 500px !important;
	margin: 0 auto;
	}
	</style>
</head>

<body>
	<jsp:include page="header.jsp"/>
	
	<%
	List<AnnouncementLink> announcements = (List<AnnouncementLink>) request.getAttribute("announcements");
	List<User> users = (List<User>) request.getAttribute("users");
	List<QuizLink> quizzes = (List<QuizLink>) request.getAttribute("quizzes");
	WebsiteStats websiteStats = (WebsiteStats) request.getAttribute("websiteStats");
	%>
	
	<div class="jumbotron">
      <div class="container text-center">
      	<h2>QuizMe Admin Panel</h2>
      	<form action='ManageAdminServlet' method='POST'>
      		<p>Add an announcement</p>
      		<div class="form-group">
			        <label for="subject">Subject</label> 
			        <input class="form-control" name="subject" type="text">
			    </div>
			
			    <div class="form-group">
			        <label for="message">Announcement</label> 
			        <input class="form-control" name="message" type="text">
			    </div>
      		<input class='btn btn-primary' type='submit' name='add-announcement' value='Add Announcement'>
      	</form>
		</div>
	</div>
	
	<div class="container text-center">
	
	</div>
	
	<div class="container text-center">
		<h3>Announcements</h3>
		<%
		if (announcements.isEmpty()) {
        	out.println("No announcements");
		} else {
        	out.println("<form action='ManageAdminServlet' method='POST'>");
	       	for (AnnouncementLink announcement : announcements) {
				String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(announcement.date());
	       		out.println(date);
	       		out.println("<br>");
	       		out.println("Subject: <b>" + announcement.subject() + "</b>");
	       		out.println("<br>");
	       		out.println("Message: <b>" + announcement.content() + "</b>");
	       		out.println("<br>");
	       		out.println("<input type='checkbox' name='announcement' value='" + announcement.announcementID() + "'>");
	       		out.println("<div class='row'><label>Delete announcement</label></div>");
	       		out.println("<br><br>");
	       	}
	        out.println("<input type='submit' name='remove-announcement' value='Remove checked announcements' class='btn btn-danger'>");
	       	out.println("</form>");
	     }
		%>
	</div>
	
	<div class="container text-center">
		<h3>Users</h3>
		<%
		if (users.isEmpty()) {
			out.println("No users");
		} else {
			out.println("<form action='ManageAdminServlet' method='POST'>");
	       	for (User user : users) {
	       		out.println("<div class='row'>");
	       		out.println("<b><span name='username'>" + user.getName() + "</span></b> &nbsp;&nbsp;&nbsp;");
	       		out.println("<br><br>");
	       		out.println("<input style='width: 175px !important;' class='btn btn-danger' type='submit' name='remove-user' value='Delete this user'>");
	       		
	       		if (user.isAdmin()) {
		       		out.println("<input style='width: 175px !important;' class='btn btn-warning' type='submit' name='remove-admin-status' value='Remove admin status'>");
	       		} else {
		       		out.println("<input style='width: 175px !important;' class='btn btn-warning' type='submit' name='add-admin-status' value='Add admin status'>");
	       		}
	       		out.println("</div>");
	       		out.println("<br><br>");
	       	}
	       	out.println("</form>");
		}
		%>
	</div>
	
	<div class="container text-center">
      	<h3>Website stats:</h3>
		<p>Number of users: <b><%=websiteStats.numOfUsers %></b></p>
		<p>Number of friend relationships: <b><%=websiteStats.numOfFriendRelationships %></b></p>
		<p>Number of quizzes created all time: <b><%=websiteStats.numOfQuizzesCreated[2] %></b></p>
		<p>Number of quizzes created past week: <b><%=websiteStats.numOfQuizzesCreated[1] %></b></p>
		<p>Number of quizzes created past day: <b><%=websiteStats.numOfQuizzesCreated[0] %></b></p>
		<br>
		<p>Number of quizzes taken all time: <b><%=websiteStats.numOfQuizzesTaken[2] %></b></p>
		<p>Number of quizzes taken past week: <b><%=websiteStats.numOfQuizzesTaken[1] %></b></p>
		<p>Number of quizzes taken past day: <b><%=websiteStats.numOfQuizzesTaken[0] %></b></p>
		<br>
		<p>Number of achievements gained all time: <b><%=websiteStats.numOfAchievements[2] %></b></p>
		<p>Number of achievements gained past week: <b><%=websiteStats.numOfAchievements[1] %></b></p>
		<p>Number of achievements gained past day: <b><%=websiteStats.numOfAchievements[0] %></b></p>
	</div>

	<hr>

</body>
</html>