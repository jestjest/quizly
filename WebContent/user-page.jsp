<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% User user = (User) request.getSession().getAttribute("user"); %>

<%@ page import="quizme.User" %>
<%@ page import="quizme.links.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>QuizMe <%=request.getParameter("username")%>'s Page</title>
	<link rel="stylesheet" href="css/bootstrap.min.css">
</head>

<body>
	<jsp:include page="header.jsp"/>
    
    <div class="jumbotron">
      <div class="container text-center">
        <h2>Welcome to <%=request.getParameter("username")%>'s page!</h2>
        <%
        int friendStatus = (int) request.getAttribute("friendStatus");
        if (friendStatus == 0) {
        	out.println("<form role='form' action='FriendRequestServlet' method='POST'>");
        	out.println("<div class='form-group'>");
        	out.println("<input type='hidden' name='target-user' value='" + request.getParameter("username") + "'>");
        	out.println("<input type='submit' name='send-request' value='Add friend' class='btn btn-primary'>");
        	out.println("</div>");
        	out.println("</form>");
        } else if (friendStatus == 1) {
        	out.println("<b><i>Friend request pending</i></b>");
        } else if (friendStatus == 2) {
        	out.println("This user wants to be friends with you.");
        	out.println("<form role='form' action='FriendRequestServlet' method='POST'>");
        	out.println("<div class='form-group'>");
        	out.println("<input type='hidden' name='target-user' value='" + request.getParameter("username") + "'>");
        	out.println("<input type='submit' name='accept-request' value='Accept friend' class='btn btn-success'>");
        	out.println("<input type='submit' name='reject-request' value='Reject friend' class='btn btn-danger'>");
        	out.println("</div>");
        	out.println("</form>");
        } else if (friendStatus == 3) {
        	out.println("You are friends with this user.");
        	out.println("<form role='form' action='FriendRequestServlet' method='POST'>");
        	out.println("<div class='form-group'>");
        	out.println("<input type='hidden' name='target-user' value='" + request.getParameter("username") + "'>");
        	out.println("<input type='submit' name='remove-friend' value='Remove friend' class='btn btn-danger'>");
        	out.println("</div>");
        	out.println("</form>");
        }
        %>
        
        <%
        if (!(user.getName().equals(request.getParameter("username")))) {
        	out.println("<form class='send-message' action='SendMessageServlet' method='POST'>");
        	out.println("<div class='form-group'>");
        	out.println("<p>Send a message to this user</p>");
        	out.println("<label>Subject</label>");
        	out.println("<br>");
        	out.println("<input type='text' name='subject' value=''>");
        	out.println("<br><br>");
        	out.println("<label>Message</label>");
        	out.println("<textarea class='form-control' rows='5' name='message'></textarea>");
        	out.println("<br>");
        	out.println("<input type='submit' name='send-message' value='Send Message' class='btn btn-success'>");
        	out.println("<input type='hidden' name='target-user' value='" + request.getParameter("username") + "'>");
			out.println("</div>");
			out.println("</form>");
		}
		%>
		
      </div>
    </div>
   
    <div class="container">
    	<h2>Created quizzes</h2>
    	<div class="row">
    		<%
    		List<QuizLink> createdQuizzes = (List<QuizLink>) request.getAttribute("pageUserQuizzesCreated");
    		if (createdQuizzes.isEmpty())
    			out.println("No recently created quizzes.");
    		
    		for (QuizLink quiz : createdQuizzes) {
    			String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateCreated());
				out.println(request.getParameter("username") + " created " + quiz.getQuizLink() + " on " + date + 
						" and has been taken " + quiz.getNumTaken() + " times.");
				out.println("<br>");
    		}
    		%>
    	</div>
    </div>
    
    <hr>
    
    <div class="container">
    	<h2>Taken quizzes</h2>
    	<div class="row">
    		<%
    		List<QuizLink> takenQuizzes = (List<QuizLink>) request.getAttribute("pageUserQuizzesTaken");
    		if (takenQuizzes.isEmpty())
    			out.println("No taken quizzes.");
    		
    		for (QuizLink quiz : takenQuizzes) {
    			String date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(quiz.getDateCreated());
    			out.println(request.getParameter("username") + " took " + quiz.getQuizLink() + " on " + date +
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
    		List<AchievementLink> achievements = (List<AchievementLink>) request.getAttribute("pageUserAchievements");
    		if (achievements.isEmpty())
    			out.println("No achievements.");
    		
    		for (AchievementLink achievement : achievements) {
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